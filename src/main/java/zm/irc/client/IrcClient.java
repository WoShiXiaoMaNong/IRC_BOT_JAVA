package zm.irc.client;


import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.message.send.IrcLogonAnyNameMessage;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;
import zm.irc.threads.CommandLineInputThread;
import zm.irc.threads.MsgSendThread;
import zm.irc.threads.RecvMsgCollectThread;
import zm.irc.threads.RecvMsgProcessThread;
import zm.irc.threads.TranslateThread;

import java.io.*;
import java.net.Socket;
import java.util.*;


public class IrcClient {
    private static final Logger log = Logger.getLogger(IrcClient.class);
    public static final int DEFAULT_PORT = 6667;

    /**
     * key : channel  name
     * value : channel
     */
    private Map<String,IrcChannel> channels;
    private LinkedList<IrcChannel> channelList;

    private volatile IrcChannel currentChannel;
    private int currentChannelIndex; // For internal Use only;
    private ServerInfo serverInfo;
    private String nick;

    private boolean shouldPrintChatMsg;

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;

    public IrcClient(ServerInfo serverInfo, String nick){
        this.channels = new HashMap<>();
        this.channelList = new LinkedList<>();
        this.currentChannelIndex = 0;
        this.serverInfo = serverInfo;
        this.nick = nick;
        this.shouldPrintChatMsg = true;

    }

    public boolean isShouldPrintChatMsg() {
        return shouldPrintChatMsg;
    }

    public void setShouldPrintChatMsg(boolean shouldPrintChatMsg) {
        this.shouldPrintChatMsg = shouldPrintChatMsg;
    }

    public void switchChannel(String channelName){
        IrcChannel c = this.channels.get(channelName);
        if(c !=null){
            this.currentChannel = c;
        }
    }

    /**
     * key : channel  name
     * value : channel
     */
    public Map<String,IrcChannel> getAllChannel(){
        return this.channels;
    }

    public IrcChannel getChannel(String channelName){
        return this.channels.get(channelName);
    }

    public ServerInfo getServerInfo(){
        return  this.serverInfo;
    }

    public IrcChannel getCurrentChannel(){
        return this.currentChannel;
    }

    public void start() throws IOException {
        log.info(String.format("Start to connect IRC server(Server:%s, Port:%s)",this.serverInfo.getServer(), this.serverInfo.getPort()));

        Socket socket = new Socket(this.serverInfo.getServer(), this.serverInfo.getPort());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        log.info("Prepare Message Collecting Thread!");
        new Thread(new RecvMsgCollectThread(this,reader)).start();

        log.info("Prepare Message Recv Thread!");
        new Thread(new RecvMsgProcessThread(this)).start();

        log.info("Prepare Message Sender Thread!");
        new Thread(new MsgSendThread(writer)).start();

        log.info("Prepare Command-Line Input Thread！");
        new Thread(new CommandLineInputThread(this)).start();

        log.info("Prepare Translate Thread！");
        new Thread(new TranslateThread()).start();



            log.info("Nick Name :" + nick + " Channel List ：" + this.serverInfo.getChannel());
            this.logon(nick);
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            log.error("Thread Sleep error!",e);
        }
            serverInfo.getChannel().forEach(c->{
                join(c);
            });



    }



    private void logon(String nick){
        IrcLogonAnyNameMessage logonMsg = new IrcLogonAnyNameMessage();
        logonMsg.setNick(nick);
        this.sendMessage(logonMsg);
    }
    /**
     * Connect channel and return the channel object.
     * @param channelName
     * @return
     */
    public IrcChannel join(String channelName){
        IrcChannel channel = this.channels.get(channelName);
        boolean succeed = this.localMemoryMsgQueue.registerReceiveQueue(channelName);

        if( channel != null){
            log.warn("The channel already connected!" + channel);
            return channel;
        }
        if( !succeed){
            log.error("Msg queue register error!");
            return null;
        }

        /**
         * do connect logic.
         */

        channel = new IrcChannel(channelName,this);
        this.channels.put(channelName,channel);
        this.channelList.add((channel));
        log.info("加入频道："  + channelName);
        IrcJoinMessage joinMsg = new IrcJoinMessage();
        joinMsg.setChannel(channelName);
        this.sendMessage(joinMsg);

        this.currentChannel = channel;
        return this.currentChannel;
    }


    public void part(String channelName){
        IrcChannel channel = this.channels.remove(channelName);

        if( channel == null){
            log.warn("The channel already connected!" + channel);
            return ;
        }

        for(Iterator<IrcChannel> it = this.channelList.iterator();it.hasNext();){
           IrcChannel c = it.next();
           if(channelName.equals(c.getChannelName())){
               it.remove();
           }
        }
        this.localMemoryMsgQueue.removeReceiveQueue(channelName);
        channel.part();
    }


    /**
     * 将消息添加到发送队列的末尾。
     * 实际发送时间，根据{@link MsgSendThread}工作情况而定。
     * @param msg
     */
    public void sendMessage(IrcSendMessage msg) {
        localMemoryMsgQueue.addSendQueue(msg);
    }

    public IrcReceiveMessage getSystemMsg(){
       return this.localMemoryMsgQueue.getSystemMsg();
    }



    public void switchToNextChannel() {
        if(CollectionUtils.isEmpty(this.channelList)){
            return;
        }
        if(currentChannelIndex >= this.channelList.size()){
            this.currentChannelIndex = 0;
        }
        this.currentChannel = this.channelList.get(currentChannelIndex);
        currentChannelIndex++;
    }
}

package zm.irc.client;


import org.apache.log4j.Logger;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.message.send.IrcLogonAnyNameMessage;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;
import zm.irc.threads.CommandLineInputThread;
import zm.irc.threads.MsgSendThread;
import zm.irc.threads.RecvMsgCollectThread;
import zm.irc.threads.RecvMsgProcessThread;

import java.io.*;
import java.net.Socket;
import java.util.List;



public class IrcClient {
    private static Logger log = Logger.getLogger(IrcClient.class.getClass());

    public static final int DEFAULT_PORT = 6667;
    private ServerInfo serverInfo;

    private String nick;


    private int currentChannelIndex;

    private BufferedWriter writer;
    private  BufferedReader reader;



    private MsgSendThread msgSendThread;

    private RecvMsgProcessThread recvMsgProcessThread;

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;

    public IrcClient(ServerInfo serverInfo, String nick){
        this.serverInfo = serverInfo;
        this.nick = nick;
        this.msgSendThread = new MsgSendThread(this);

        this.recvMsgProcessThread = new RecvMsgProcessThread(this);

        this.currentChannelIndex = 0;
    }

    public void logon(String nick){
        IrcLogonAnyNameMessage logonMsg = new IrcLogonAnyNameMessage();
        logonMsg.setNick(nick);
        this.sendMessage(logonMsg);
    }

    /**
     * 加入指定频道
     * @param channel
     */
    private void join(String channel){
        log.info("加入频道："  + channel);
        IrcJoinMessage joinMsg = new IrcJoinMessage();
        joinMsg.setChannel(channel);
        this.sendMessage(joinMsg);
    }

    /**
     * 用于认证昵称，暂时未启用
     * @param pwd
     */
    public void identify(String pwd) {
        throw new RuntimeException("TBD");
    }


    public void start() throws IOException {
        log.info(String.format("Start to connect IRC server(Server:%s, Port:%s)",this.serverInfo.getServer(), this.serverInfo.getPort()));

        Socket socket = new Socket(this.serverInfo.getServer(), this.serverInfo.getPort());
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        log.info("Prepare Message Collecting Thread!");
        new Thread(new RecvMsgCollectThread(this)).start();

        log.info("Prepare Message Recv Thread!");
        new Thread(new RecvMsgProcessThread(this)).start();

        log.info("Prepare Message Sender Thread!");
        new Thread(this.msgSendThread).start();

        log.info("准备消息输入线程！");
        new Thread(new CommandLineInputThread(this)).start();


        try {
            log.info("使用昵称:" + nick + " 登录频道：" + this.serverInfo.getChannel());
            this.logon(nick);
            Thread.sleep(3000);
            serverInfo.getChannel().forEach(c->{
                join(c);
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 读取消息
     * @return
     * @throws IOException
     */
    public String readNewMsgLine() throws IOException {
        return this.reader.readLine();
    }

    /**
     * 直接将消息发送出去，跳过发送等待队列
     * @param msg
     * @throws IOException
     */
    public synchronized void sendMessageDirect(IrcSendMessage msg) throws IOException {
        writer.write(msg.getMessage());
        writer.flush();
    }

    /**
     * 将消息添加到发送队列的末尾。
     * 实际发送时间，根据{@link MsgSendThread}工作情况而定。
     * @param msg
     */
    public void sendMessage(IrcSendMessage msg) {
        localMemoryMsgQueue.addSendQueue(msg);
    }

    public List<String> getChannel(){
        return this.serverInfo.getChannel();
    }
    public String getCurrentChannel(){
        return this.serverInfo.getChannel().get(this.currentChannelIndex);
    }

    /**
     * 用于切换当前频道
     * @param index
     */
    public void changeChannel(int index){
        if(index < 0 ){
            index = 0;
        }
        if(index >=this.serverInfo.getChannel().size()){
            index =this.serverInfo.getChannel().size() - 1;
        }

        this.currentChannelIndex = index;
    }


    public String getServer(){
        return this.serverInfo.getServer();
    }

    public int getPort(){
        return this.serverInfo.getPort();
    }

}

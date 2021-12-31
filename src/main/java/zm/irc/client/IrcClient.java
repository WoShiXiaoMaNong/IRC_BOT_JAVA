package zm.irc.client;


import org.apache.log4j.Logger;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.message.send.IrcLogonAnyNameMessage;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.threads.CommandLineInputThread;
import zm.irc.threads.MsgSendThread;
import zm.irc.threads.RecvMsgProcessThread;

import java.io.*;
import java.net.Socket;


public class IrcClient {
    private static Logger log = Logger.getLogger(IrcClient.class.getClass());

    public static final int DEFAULT_PORT = 6667;
    private String server;
    private int port;

    private String nick;

    private String channel;

    private BufferedWriter writer;
    private  BufferedReader reader;



    private MsgSendThread msgSendThread;

    private RecvMsgProcessThread recvMsgProcessThread;

    public IrcClient(String server,int port,String nick,String channel){
        this.server = server;
        this.port = port;
        this.nick = nick;
        this.msgSendThread = new MsgSendThread(this);

        this.recvMsgProcessThread = new RecvMsgProcessThread(this);
        this.channel = channel;
    }

    public void logon(String nick){
        IrcLogonAnyNameMessage logonMsg = new IrcLogonAnyNameMessage();
        logonMsg.setNick(nick);
        this.sendMessage(logonMsg);
    }

    public void join(String channel){
        IrcJoinMessage joinMsg = new IrcJoinMessage();
        joinMsg.setChannel(channel);
        this.sendMessage(joinMsg);
    }

    public void identify(String pwd){

    }


    public void start() throws IOException {
        log.info(String.format("Start to connect IRC server(Server:%s, Port:%s)",this.server,this.port));

        Socket socket = new Socket(this.server, this.port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        log.info("Prepare Message Recv Thread!");
        new Thread(new RecvMsgProcessThread(this)).start();

        log.info("Prepare Message Sender Thread!");
        new Thread(this.msgSendThread).start();

        log.info("准备消息输入线程！");
        new Thread(new CommandLineInputThread(this)).start();


        try {
            log.info("使用昵称:" + nick + " 登录频道：" + this.channel);
            this.logon(nick);
            Thread.sleep(3000);
            join(channel);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public String readNewMsgLine() throws IOException {
        return this.reader.readLine();
    }

    public void sendMessageDirect(IrcSendMessage msg) throws IOException {
        writer.write(msg.getMessage());
        writer.flush();
    }


    public void sendMessage(IrcSendMessage msg) {
        this.msgSendThread.sendMessage(msg);
    }

    public String getChannel(){
        return this.channel;
    }

}

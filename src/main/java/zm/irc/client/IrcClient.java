package zm.irc.client;


import org.apache.log4j.Logger;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.message.send.IrcLogonAnyNameMessage;
import zm.irc.message.send.IrcSendMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;



public class IrcClient {
    private static Logger log = Logger.getLogger(IrcClient.class.getClass());

    public static final int DEFAULT_PORT = 6667;
    private String server;
    private int port;
    private ConcurrentLinkedQueue<IrcSendMessage> messagePendingSend;
    private String nick;

    private MessageProcessorCenter msgProcessorCenter;

    public IrcClient(String server,int port,String nick){
        this.server = server;
        this.port = port;
        this.nick = nick;
        this.messagePendingSend = new ConcurrentLinkedQueue();
        this.msgProcessorCenter = MessageProcessorCenter.build(this);
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

    public void sendMessage(IrcSendMessage msg){
        if(msg == null){
            return ;
        }
        this.messagePendingSend.add(msg);
    }
    public void start() throws IOException {
        log.info(String.format("Start to connect IRC server(Server:%s, Port:%s)",this.server,this.port));

        Socket socket = new Socket(this.server, this.port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            try{
                log.info("Listener Thread started.");
                String msg = reader.readLine();
                while(msg != null){
                    onMessage(msg);
                    msg = reader.readLine();
                }
            }catch (Exception e){
                log.error("error",e);
            }
        }).start();

        new Thread(() -> {

                log.info("Sender Thread started.");
                IrcSendMessage msg = messagePendingSend.poll();
                while(true){
                    try{
                        if(msg == null){
                            msg = messagePendingSend.poll();
                            continue;
                        }
                        sendMessage(writer,msg);
                        msg = messagePendingSend.poll();
                    }catch (Exception e){
                        log.error("error",e);
                    }
                }

        }).start();
    }

    private void onMessage(String message){
        IrcReceiveMessage receivedMsg = IrcReceiveMessage.build(message);
        this.msgProcessorCenter.process(receivedMsg);
    }

    private void sendMessage(BufferedWriter writer, IrcSendMessage msg) throws IOException {
        writer.write(msg.getMessage());
        writer.flush();
    }
}

package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.receive.IrcReceiveMessage;
import java.util.concurrent.ConcurrentLinkedQueue;


public class RecvMsgProcessThread implements Runnable{
    private static Logger log = Logger.getLogger(RecvMsgProcessThread.class);

    private ConcurrentLinkedQueue<IrcReceiveMessage> recvMessageQueue;

    private IrcClient ircClient;

    private MessageProcessorCenter msgProcessorCenter;


    public RecvMsgProcessThread(IrcClient ircClient){
        this.ircClient = ircClient;
        this.recvMessageQueue = new ConcurrentLinkedQueue();
        this.msgProcessorCenter = MessageProcessorCenter.build(ircClient);
    }

    @Override
    public void run() {
        try{
            log.info("Message Recv Thread Started!");
            String msg = this.ircClient.readNewMsgLine();
            while(msg != null){
                onMessage(msg);
                msg = this.ircClient.readNewMsgLine();
            }
        }catch (Exception e){
            log.error("error",e);
        }
    }


    public void putMessage(IrcReceiveMessage msg){

        this.recvMessageQueue.add(msg);
    }

    public void onMessage(String message){
        IrcReceiveMessage receivedMsg = IrcReceiveMessage.build(message);
        this.msgProcessorCenter.process(receivedMsg);
    }

}

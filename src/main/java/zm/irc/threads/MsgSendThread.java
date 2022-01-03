package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;

import java.util.concurrent.ConcurrentLinkedQueue;


public class MsgSendThread implements Runnable{
    private static Logger log = Logger.getLogger(MsgSendThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;


    private IrcClient ircClient;

    public MsgSendThread(IrcClient ircClient){
        this.ircClient = ircClient;
    }

    @Override
    public void run() {
        log.info("Sender Thread started.");
        IrcSendMessage msg = localMemoryMsgQueue.getMsgFromSendQueue();
        while(true){
            try{
                if(msg == null){
                    Thread.sleep(50);
                    msg =  localMemoryMsgQueue.getMsgFromSendQueue();
                    continue;
                }
                this.ircClient.sendMessageDirect(msg);
                msg = localMemoryMsgQueue.getMsgFromSendQueue();
            }catch (Exception e){
                log.error("error",e);
            }
        }
    }

}

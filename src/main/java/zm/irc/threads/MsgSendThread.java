package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.send.IrcSendMessage;

import java.util.concurrent.ConcurrentLinkedQueue;


public class MsgSendThread implements Runnable{
    private static Logger log = Logger.getLogger(MsgSendThread.class);

    private ConcurrentLinkedQueue<IrcSendMessage> messagePendingSend;

    private IrcClient ircClient;

    public MsgSendThread(IrcClient ircClient){
        this.ircClient = ircClient;
        this.messagePendingSend = new ConcurrentLinkedQueue();
    }

    @Override
    public void run() {
        log.info("Sender Thread started.");
        IrcSendMessage msg = messagePendingSend.poll();
        while(true){
            try{
                if(msg == null){
                    msg = messagePendingSend.poll();
                    continue;
                }
                this.ircClient.sendMessageDirect(msg);
                msg = messagePendingSend.poll();
            }catch (Exception e){
                log.error("error",e);
            }
        }
    }


    public void sendMessage(IrcSendMessage msg){
        if(msg == null){
            return ;
        }
        this.messagePendingSend.add(msg);
    }
}

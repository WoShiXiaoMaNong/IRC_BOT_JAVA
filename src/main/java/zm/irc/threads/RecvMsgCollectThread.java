package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;


public class RecvMsgCollectThread implements Runnable{
    private static Logger log = Logger.getLogger(RecvMsgCollectThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;
    private IrcClient ircClient;

    private MessageProcessorCenter msgProcessorCenter;


    public RecvMsgCollectThread(IrcClient ircClient){
        this.ircClient = ircClient;
        this.msgProcessorCenter = MessageProcessorCenter.build(ircClient);
    }

    @Override
    public void run() {
        try{
            log.info("Receive Message Collecting Thread Started!");
            String msg = this.ircClient.readNewMsgLine();
            while(true){
                if(msg == null){
                    msg = this.ircClient.readNewMsgLine();
                    continue;
                }
                IrcReceiveMessage receivedMsg = IrcReceiveMessage.build(msg);
                this.localMemoryMsgQueue.addIntoReceiveQueue(receivedMsg);
                msg = this.ircClient.readNewMsgLine();
            }
        }catch (Exception e){
            log.error("error",e);
        }
    }






}

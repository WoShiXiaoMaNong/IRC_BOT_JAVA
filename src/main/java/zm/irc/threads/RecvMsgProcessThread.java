package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;



public class RecvMsgProcessThread implements Runnable{
    private static Logger log = Logger.getLogger(RecvMsgProcessThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;
    private IrcClient ircClient;

    private MessageProcessorCenter msgProcessorCenter;


    public RecvMsgProcessThread(IrcClient ircClient){
        this.ircClient = ircClient;
        this.msgProcessorCenter = MessageProcessorCenter.build(ircClient);
    }

    @Override
    public void run() {
        try{
            log.info("Receive Message Process Thread Started!");
            IrcReceiveMessage receivedMsg = this.localMemoryMsgQueue.getMsgFromSReceiveQueue();
            this.doProcess(receivedMsg);
        }catch (Exception e){
            log.error("error",e);
        }
    }

    private void doProcess( IrcReceiveMessage receivedMsg){

            while(true){
                try{
                    if(receivedMsg == null){
                        receivedMsg = this.localMemoryMsgQueue.getMsgFromSReceiveQueue();
                        continue;
                    }
                    this.msgProcessorCenter.process(receivedMsg);
                    receivedMsg = this.localMemoryMsgQueue.getMsgFromSReceiveQueue();
                }catch (Exception e){
                    log.error("error",e);
                }
            }


    }
}

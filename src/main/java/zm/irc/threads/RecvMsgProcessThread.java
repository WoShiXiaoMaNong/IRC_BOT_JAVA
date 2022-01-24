package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcChannel;
import zm.irc.client.IrcClient;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.receive.IrcReceiveMessage;

public class RecvMsgProcessThread implements Runnable{
    private static Logger log = Logger.getLogger(RecvMsgProcessThread.class);
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

            this.doProcess();
        }catch (Exception e){
            log.error("error",e);
        }
    }

    private void doProcess( ) {
        IrcChannel currentChannel;
        IrcReceiveMessage receivedMsg;
        IrcReceiveMessage systemMsg;
        while (true) {
            try {
                currentChannel = this.ircClient.getCurrentChannel();
                if(currentChannel == null){
                    Thread.sleep(50);
                    continue;
                }
                receivedMsg = currentChannel.receiveMsg();
                systemMsg = this.ircClient.getSystemMsg();
                if (receivedMsg == null && systemMsg == null) {
                    Thread.sleep(50);
                    continue;
                }
                this.msgProcessorCenter.process(systemMsg);
                this.msgProcessorCenter.process(receivedMsg);
            } catch (Exception e) {
                log.error("error", e);
            }
        }


    }
}

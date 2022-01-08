package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.processor.MessageProcessorCenter;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;

import java.io.BufferedReader;


public class RecvMsgCollectThread implements Runnable{
    private static Logger log = Logger.getLogger(RecvMsgCollectThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;
    private IrcClient ircClient;



    private BufferedReader reader;
    public RecvMsgCollectThread(IrcClient ircClient, BufferedReader reader){
        this.ircClient = ircClient;
        this.reader = reader;
    }

    @Override
    public void run() {
        try{
            log.info("Receive Message Collecting Thread Started!");
            String msg = this.reader.readLine();
            while(true){
                if(msg == null){
                    msg = this.reader.readLine();
                    continue;
                }
                IrcReceiveMessage receivedMsg = IrcReceiveMessage.build(msg,this.ircClient);
                String channelName = receivedMsg.getChannel();
                this.localMemoryMsgQueue.receiveMsg(channelName,receivedMsg);

                msg = this.reader.readLine();
            }
        }catch (Exception e){
            log.error("error",e);
        }
    }






}

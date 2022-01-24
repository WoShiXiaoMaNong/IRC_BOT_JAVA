package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;

import java.io.BufferedWriter;
import java.io.IOException;


public class MsgSendThread implements Runnable{
    private static Logger log = Logger.getLogger(MsgSendThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;

    private  BufferedWriter writer;

    public MsgSendThread( BufferedWriter writer){
        this.writer = writer;
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
                this.sendMessageDirect(msg);
                msg = localMemoryMsgQueue.getMsgFromSendQueue();
            }catch (Exception e){
                log.error("error",e);
            }
        }
    }


    /**
     * <pre>
     * Please do not send message by this method!
     * Only for {@link MsgSendThread}.
     * </pre>
     * @param msg
     * @throws IOException
     */
    public synchronized void sendMessageDirect(IrcSendMessage msg) throws IOException {
        writer.write(msg.getMessage());
        writer.flush();
    }
}

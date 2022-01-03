package zm.irc.msgqueue;


import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.send.IrcSendMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalMemoryMsgQueue {

    public static final LocalMemoryMsgQueue localMemoryMsgQueue;
    static{
        localMemoryMsgQueue = new LocalMemoryMsgQueue();
    }

    private ConcurrentLinkedQueue<IrcSendMessage> sendQueue;

    private ConcurrentLinkedQueue<IrcReceiveMessage> receiveQueue;

    private LocalMemoryMsgQueue(){
        this.sendQueue = new ConcurrentLinkedQueue<>();
        this.receiveQueue = new ConcurrentLinkedQueue<>();
    }


    public void addSendQueue(IrcSendMessage msg){
        this.sendQueue.add(msg);
    }

    public IrcSendMessage getMsgFromSendQueue(){
        return this.sendQueue.poll();
    }

    public void addIntoReceiveQueue(IrcReceiveMessage msg){
        this.receiveQueue.add(msg);
    }

    public IrcReceiveMessage getMsgFromSReceiveQueue(){
        return this.receiveQueue.poll();
    }

}

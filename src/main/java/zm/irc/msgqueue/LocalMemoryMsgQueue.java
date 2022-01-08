package zm.irc.msgqueue;


import org.apache.log4j.Logger;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.send.IrcSendMessage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalMemoryMsgQueue {
    private static final Logger log = Logger.getLogger(LocalMemoryMsgQueue.class);
    public static final LocalMemoryMsgQueue localMemoryMsgQueue;
    static{
        localMemoryMsgQueue = new LocalMemoryMsgQueue();
    }

    private ConcurrentLinkedQueue<IrcSendMessage> sendQueue;



    private ConcurrentHashMap<String, ConcurrentLinkedQueue> receiveQueues;

    private ConcurrentLinkedQueue<IrcReceiveMessage> systemReceiveQueue;

    private LocalMemoryMsgQueue(){
        this.sendQueue = new ConcurrentLinkedQueue<>();
        this.systemReceiveQueue = new ConcurrentLinkedQueue<>();
        this.receiveQueues = new ConcurrentHashMap<>();
    }

    public ConcurrentLinkedQueue<IrcReceiveMessage> registerReceiveQueue(String queueName){
        if(this.receiveQueues.containsKey(queueName)){
            log.error("Queue Name exist!" + queueName);
            return null;
        }
        ConcurrentLinkedQueue<IrcReceiveMessage> queue = new ConcurrentLinkedQueue<>();
        this.receiveQueues.put(queueName,queue);
        return queue;
    }


    public void receiveMsg(String channelName,IrcReceiveMessage recvMsg){
        if(channelName == null){
            log.debug("The message was added into system msg queue.");
            this.systemReceiveQueue.add(recvMsg);
            return;
        }
        ConcurrentLinkedQueue<IrcReceiveMessage> receiveQueue = this.receiveQueues.get(channelName);
        if(receiveQueue == null){
            log.debug("There is no receive buffer named :" + channelName + " . The message was added into system msg queue.");
            this.systemReceiveQueue.add(recvMsg);
        }else{
            receiveQueue.add(recvMsg);
        }

    }

    public IrcReceiveMessage getSystemMsg(){
        if(this.systemReceiveQueue.isEmpty()){
            return null;
        }
        return this.systemReceiveQueue.poll();
    }

    public void addSendQueue(IrcSendMessage msg){
        if(msg == null){
            return;
        }
        this.sendQueue.add(msg);
    }

    public IrcSendMessage getMsgFromSendQueue(){
        return this.sendQueue.poll();
    }



}

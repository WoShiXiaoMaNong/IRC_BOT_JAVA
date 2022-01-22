package zm.irc.msgqueue;


import org.apache.commons.collections.CollectionUtils;
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



    private ConcurrentHashMap<String, ConcurrentLinkedQueue<IrcReceiveMessage>> receiveQueues;

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> commonQueues;

    private ConcurrentLinkedQueue<IrcReceiveMessage> systemReceiveQueue;

    private LocalMemoryMsgQueue(){
        this.sendQueue = new ConcurrentLinkedQueue<>();
        this.systemReceiveQueue = new ConcurrentLinkedQueue<>();
        this.receiveQueues = new ConcurrentHashMap<>();
        this.commonQueues = new ConcurrentHashMap<>();
    }

    public synchronized boolean  registerCommonQueue(String queueName){
        if(this.commonQueues.containsKey(queueName)){
            log.error("Queue Name exist!" + queueName);
            return false;
        }
        ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        this.commonQueues.put(queueName,queue);
        return true;
    }

    public synchronized boolean  registerReceiveQueue(String queueName){
        if(this.receiveQueues.containsKey(queueName)){
            log.error("Queue Name exist!" + queueName);
            return false;
        }
        ConcurrentLinkedQueue<IrcReceiveMessage> queue = new ConcurrentLinkedQueue<>();
        this.receiveQueues.put(queueName,queue);
        return true;
    }

    public void removeReceiveQueue(String queueName){
        this.receiveQueues.remove(queueName);
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

    public void addIntoCommonQueue(String queueName,Object msg){
        if(queueName == null){
            log.debug("Queue name must be not null.");
            return;
        }
        ConcurrentLinkedQueue<Object> commonQueue = this.commonQueues.get(queueName);
        if(commonQueue == null){
            log.debug("There is no common queue named :" + queueName + " . The message was be dropped.");
        }else{
            commonQueue.add(msg);
        }

    }

    public IrcReceiveMessage getSystemMsg(){
        if(this.systemReceiveQueue.isEmpty()){
            return null;
        }
        return this.systemReceiveQueue.poll();
    }

    public IrcReceiveMessage getMsgFromBuffer(String channelName){
        ConcurrentLinkedQueue<IrcReceiveMessage> buffer = this.receiveQueues.get(channelName);
        if(CollectionUtils.isEmpty(buffer)){
            return null;
        }
        return buffer.poll();
    }

    public Object getMsgFromCommonQueue(String channelName){
        ConcurrentLinkedQueue<Object> buffer = this.commonQueues.get(channelName);
        if(CollectionUtils.isEmpty(buffer)){
            return null;
        }
        return buffer.poll();
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

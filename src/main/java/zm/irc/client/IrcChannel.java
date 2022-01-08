package zm.irc.client;

import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.send.IrcSendMessage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IrcChannel {

    private String channelName;
    private IrcClient client;
    private ConcurrentLinkedQueue<IrcReceiveMessage> receiveBuffer;

    public IrcChannel(String channelName, IrcClient client, ConcurrentLinkedQueue<IrcReceiveMessage> receiveBuffer){
        this.channelName = channelName;
        this.client = client;
        this.receiveBuffer = receiveBuffer;
    }


    public String getChannelName(){
        return this.channelName;
    }


    public IrcClient getClient(){
        return this.client;
    }


    public void send(String msgStr){
        IrcSendMessage msg = IrcSendMessage.build(this.channelName,msgStr);
        this.client.sendMessage(msg);
    }

    public IrcReceiveMessage receiveMsg(){
        if(this.receiveBuffer.isEmpty()){
            return null;
        }
        return this.receiveBuffer.poll();
    }

}

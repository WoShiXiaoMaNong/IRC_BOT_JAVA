package zm.irc.client;

import zm.irc.consts.IrcCommand;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.send.IrcSendMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;



public class IrcChannel {

    private String channelName;
    private IrcClient client;
    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;

    public IrcChannel(String channelName, IrcClient client){
        this.channelName = channelName;
        this.client = client;
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

        return this.localMemoryMsgQueue.getMsgFromBuffer(this.channelName);
    }


    public void part(){
        this.send(IrcCommand.PART);
    }
}

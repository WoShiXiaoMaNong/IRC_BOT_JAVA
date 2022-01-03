package zm.irc.message.processor;

import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveMessage;

import java.util.LinkedList;

public class MessageProcessorCenter {

    public static MessageProcessorCenter build(IrcClient client) {
        MessageProcessorCenter mpc = new MessageProcessorCenter();
        mpc.setClient(client);
        mpc.add(new PingMessageProcessor());
        mpc.add(new PrintProcessor());
        mpc.add(new CmdMsgProcessor());
        mpc.add(new DbLogProcessor());
        return mpc;
    }

    private IrcClient client;

    private LinkedList<IrcMessageProcessor> impList;

    private void add(IrcMessageProcessor ip){
        this.impList.add(ip);
    }

    public MessageProcessorCenter(){
        this.impList = new LinkedList<>();
    }

    public void setClient(IrcClient client) {
        this.client = client;
    }

    public void process(IrcReceiveMessage msg){
        for( IrcMessageProcessor imp : this.impList){
            boolean shouldContinue = imp.processor(this.client,msg);
            if( !shouldContinue){
                break;
            }
        }
    }
}

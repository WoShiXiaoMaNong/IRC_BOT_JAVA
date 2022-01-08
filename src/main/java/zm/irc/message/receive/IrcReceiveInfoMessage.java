package zm.irc.message.receive;

import zm.irc.client.IrcClient;

public class IrcReceiveInfoMessage extends IrcReceiveMessage{

    private String channel;

    public IrcReceiveInfoMessage(String originMsg, IrcClient ircClient) {
        super(originMsg,ircClient);


    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public boolean shouldPrint() {
        return true;
    }

    @Override
    public String getMessageForPrint() {
        return this.getOriginMsg();
    }
}

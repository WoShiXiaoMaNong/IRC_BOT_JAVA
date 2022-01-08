package zm.irc.message.receive;

import zm.irc.client.IrcClient;

public class IrcReceivePingMessage extends IrcReceiveMessage{


    public IrcReceivePingMessage(String originMsg, IrcClient ircClient) {
        super(originMsg,ircClient);
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    public String getMessageForPrint() {
        return null;
    }

    @Override
    public String getChannel() {
        return "system";
    }
}

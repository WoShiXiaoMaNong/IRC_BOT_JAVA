package zm.irc.message.send;

import zm.irc.consts.IrcMessageType;

public class IrcJoinMessage implements IrcSendMessage {


    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String getMessage() {
        return IrcMessageType.JOIN_MESSAGE + channel + "\r\n";
    }
}

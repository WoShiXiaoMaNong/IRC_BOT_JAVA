package zm.irc.message.send;

import zm.irc.consts.IrcMessageType;


public class IrcPartMessage implements IrcSendMessage {


    private String channel;

    public String getChannel() {
        return channel;
    }

    @Override
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String getMessage() {
        return IrcMessageType.PART_MESSAGE + channel + "\r\n";
    }
}

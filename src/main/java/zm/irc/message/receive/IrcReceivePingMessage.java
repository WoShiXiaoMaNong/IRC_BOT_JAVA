package zm.irc.message.receive;

public class IrcReceivePingMessage extends IrcReceiveMessage{


    public IrcReceivePingMessage(String originMsg) {
        super(originMsg);
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    public String getMessageForPrint() {
        return null;
    }
}

package zm.irc.message.receive;

public class IrcReceiveInfoMessage extends IrcReceiveMessage{


    public IrcReceiveInfoMessage(String originMsg) {
        super(originMsg);


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

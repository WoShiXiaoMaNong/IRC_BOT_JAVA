package zm.irc.message.send;

public class IrcPongMessage implements IrcSendMessage {


    private String originPingMessage;



    public void setOriginPingMessage(String originPingMessage) {
        this.originPingMessage = originPingMessage;
    }

    @Override
    public String getMessage() {
        return "PONG " + originPingMessage.substring(5) + "\r\n";
    }
}

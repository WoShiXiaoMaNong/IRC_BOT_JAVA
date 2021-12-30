package zm.irc.message.send;

public class IrcLogonAnyNameMessage implements IrcSendMessage {


    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String getMessage() {
        return "NICK  " + nick + "\r\n" + "USER anyName 8 * : Hello\r\n";
    }
}

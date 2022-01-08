package zm.irc.message.send;

import zm.irc.consts.IrcMessageType;

public class IrcChatMessage implements IrcSendMessage {
    private String targetChanel;

    private String msg;

    public String getChannel() {
        return targetChanel;
    }

    @Override
    public void setChannel(String targetChanel) {
        this.targetChanel = targetChanel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String getMessage() {
        return IrcMessageType.CHAT_MESSAGE + targetChanel + " :" + msg + "\r\n";
    }
}

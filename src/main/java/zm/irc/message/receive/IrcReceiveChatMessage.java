package zm.irc.message.receive;

public class IrcReceiveChatMessage extends IrcReceiveMessage{

    private String channel;
    private String fromName;
    private String fromIp;
    private String messageBody;
    /**
     * :B_fd!~root@39.103.207.190 PRIVMSG #zmtest :34234
     * Format->  :{from-name}!~{from-ip} PRIVMSG {channel} :{msg}
     * @param originMsg
     */
    public IrcReceiveChatMessage(String originMsg) {
        super(originMsg);

        this.parseMsg(originMsg);
    }

    public static boolean isChatMessage(String msgStr) {
        if(msgStr == null){
            return false;
        }
        String[] splitMsg = msgStr.split(" ");
        if(splitMsg.length < 2){
            return false;
        }

        return "PRIVMSG".equals(splitMsg[1]);
    }

    private void parseMsg(String originMsg){
        String[] splitBySpace = originMsg.split(" ");

        this.setChannel(splitBySpace[2]);
        this.setFromName(originMsg.substring(1,originMsg.indexOf('!')));
        this.setMessageBody(originMsg.substring(originMsg.lastIndexOf(':')) );
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public boolean shouldPrint() {
        return true;
    }

    @Override
    public String getMessageForPrint() {
        return this.getFromName() + "[" + this.getChannel() +"]" + " \t-> " + this.getMessageBody();
    }

    @Override
    public String toString() {
        return "IrcReceiveChatMessage{" +
                "channel='" + channel + '\'' +
                ", fromName='" + fromName + '\'' +
                ", fromIp='" + fromIp + '\'' +
                ", messageBody='" + messageBody + '\'' +
                '}';
    }
}

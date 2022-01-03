package zm.irc.message.receive;


import com.mysql.cj.util.StringUtils;

/**
 * <pre>
 * For Chat message:
 * 1. Real Chat message.
 * 2. IRC_BOT Command message.(Due to , ALl commands are sent via IRC Chat .)
 * </pre>
 */
public class IrcReceiveChatMessage extends IrcReceiveMessage{
    public static final String COMMAND_MSG_PREFIX = "zlang";
    public static final int FROM_IP_LENGTH = 32;
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
        String separator = " ";
        String[] splitBySpace = originMsg.split(separator);

        this.setChannel(splitBySpace[2]);
        this.setFromName(originMsg.substring(1,originMsg.indexOf('!')));
        this.setFromIp(splitBySpace[0].substring(splitBySpace[0].lastIndexOf('@') + 1));
        this.setMessageBody(splitBySpace,3, separator);
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
        if(fromIp.length() > FROM_IP_LENGTH){
            return fromIp.substring(0,FROM_IP_LENGTH);
        }else{
            return fromIp;
        }

    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String[] messageBody,int startIndex,String separator) {
        StringBuilder sb = new StringBuilder();
        if(messageBody != null) {
            for(int i = startIndex;i < messageBody.length;i++){
                sb.append(messageBody[i]);
                sb.append(separator);
            }
            if(sb.length() > 1){
                sb = sb.deleteCharAt(0);
            }
        }

        this.messageBody = sb.toString();
    }

    public boolean isCmdMes(){
        return !StringUtils.isEmptyOrWhitespaceOnly(this.messageBody)
                && this.messageBody.startsWith(COMMAND_MSG_PREFIX)
                && this.messageBody.trim().length() > COMMAND_MSG_PREFIX.length();
    }

    public IrcReceiveCmdMessage convertTo(){
        return new IrcReceiveCmdMessage(this);
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

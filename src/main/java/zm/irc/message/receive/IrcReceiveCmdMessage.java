package zm.irc.message.receive;




/**
 * <pre>
 * For Chat message:
 * 1. Real Chat message.
 * 2. IRC_BOT Command message.(Due to , ALl commands are sent via IRC Chat .)
 * </pre>
 */
public class IrcReceiveCmdMessage extends IrcReceiveChatMessage{

    private String[] cmdParams;
    private String cmdName;


    public IrcReceiveCmdMessage(IrcReceiveChatMessage chatMsg){
        this(chatMsg.getOriginMsg());
        this.init(chatMsg);
    }

    private void init(IrcReceiveChatMessage chatMsg){
        String originCmdString = chatMsg.getMessageBody();
        String[] cmdArray = originCmdString.split(" ");
        this.cmdName = cmdArray[1];
        if(cmdArray.length > 2){
            this.cmdParams = new String[cmdArray.length - 2];
            for(int i = 2; i < cmdArray.length; i ++){
                this.cmdParams[i-2] = cmdArray[i];
            }
        }else{
            cmdParams = null;
        }
    }

    public String getCmdName(){
        return this.cmdName;
    }

    public String[] getCmdParams(){
        return this.cmdParams;
    }


    private IrcReceiveCmdMessage(String originMsg) {
        super(originMsg);
    }
}

package zm.irc.message.receive;

import org.apache.log4j.Logger;

public abstract class IrcReceiveMessage {
    private static final Logger log = Logger.getLogger(IrcReceiveMessage.class)
;
    public static IrcReceiveMessage build(String msgStr){

        if(msgStr.startsWith("PING")){
            log.debug("PING message Received!");
            return new IrcReceivePingMessage(msgStr);
        }else if(IrcReceiveChatMessage.isChatMessage(msgStr)){
            return new IrcReceiveChatMessage(msgStr);
        }else{
            return new IrcReceiveInfoMessage(msgStr);
        }

    }




    private String originMsg;
    public IrcReceiveMessage(String originMsg){
        this.originMsg = originMsg;
    }


    public abstract boolean  shouldPrint();

    public abstract String getMessageForPrint();


    public String getOriginMsg(){
        return this.originMsg;
    }
}

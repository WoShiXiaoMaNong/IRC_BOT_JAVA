package zm.irc.message.receive;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;

public abstract class IrcReceiveMessage {
    private static final Logger log = Logger.getLogger(IrcReceiveMessage.class);


    public static IrcReceiveMessage build(String msgStr, IrcClient ircClient){

        if(msgStr.startsWith("PING")){
            log.debug("PING message Received!");
            return new IrcReceivePingMessage(msgStr,ircClient);
        }else if(IrcReceiveChatMessage.isChatMessage(msgStr)){
            return new IrcReceiveChatMessage(msgStr,ircClient);
        }else{
            return new IrcReceiveInfoMessage(msgStr,ircClient);
        }

    }




    private String originMsg;
    private IrcClient ircClient;

    public IrcReceiveMessage(String originMsg,IrcClient ircClient){
        this.originMsg = originMsg;
        this.ircClient = ircClient;
    }

    public IrcClient getIrcClient(){
        return ircClient;
    }

    public abstract boolean  shouldPrint();

    public abstract String getMessageForPrint();

    public abstract String getChannel();

    public String getOriginMsg(){
        return this.originMsg;
    }
}

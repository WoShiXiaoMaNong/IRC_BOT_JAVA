package zm.irc.message.send;

import zm.irc.consts.IrcCommand;

import java.util.Locale;

public interface IrcSendMessage {

    static IrcSendMessage build(String channel,String msgStr){
        if( msgStr == null || msgStr.trim().length() == 0){
            return null;
        }

        IrcSendMessage msg = null;
        if( msgStr.toUpperCase(Locale.ROOT).startsWith(IrcCommand.JOIN)){
            msg = new IrcJoinMessage();
            ((IrcJoinMessage)msg).setChannel(msgStr.split(" ")[1]);
        }else if( msgStr.toUpperCase(Locale.ROOT).startsWith(IrcCommand.PART)){
            msg = new IrcPartMessage();
            ((IrcPartMessage)msg).setChannel(channel);
        }else{
            msg = new IrcChatMessage();
            ((IrcChatMessage)msg).setMsg(msgStr);
            ((IrcChatMessage)msg).setChannel(channel);
        }

        return msg;
    }

    default void setChannel(String channelName){
        //Do nothing.
    }

    String getMessage();
}

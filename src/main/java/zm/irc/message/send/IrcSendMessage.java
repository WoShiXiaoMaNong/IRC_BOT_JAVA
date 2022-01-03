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
        }else{
            msg = new IrcChatMessage();
            ((IrcChatMessage)msg).setMsg(msgStr);
            ((IrcChatMessage)msg).setTargetChanel(channel);
        }






        return msg;
    }

    String getMessage();
}

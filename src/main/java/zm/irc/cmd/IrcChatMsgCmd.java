package zm.irc.cmd;

import zm.irc.message.receive.IrcReceiveCmdMessage;

/**
 * Some special Chat Message will be processed as a Command.
 * Such as the chat message is start as {@link zm.irc.message.receive.IrcReceiveChatMessage#COMMAND_MSG_PREFIX}
 */
public interface IrcChatMsgCmd {

    /**
     * return true: Continue process pipe-line
     * return false: Stop process pipe-line
     * @param cmd
     * @return
     */
    boolean execute(IrcReceiveCmdMessage cmd);
}

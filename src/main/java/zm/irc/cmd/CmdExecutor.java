package zm.irc.cmd;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveCmdMessage;

public class CmdExecutor {
    private static final Logger log = Logger.getLogger(CmdExecutor.class);

    /**
     * return true: Continue process pipe-line
     * return false: Stop process pipe-line
     * @param cmd
     * @return
     */
    public boolean executor(IrcReceiveCmdMessage cmd, IrcClient client){

        if(cmd == null){
            return true;
        }

        String cmdName = cmd.getCmdName();
        IrcChatMsgCmd ircChatMsgCmd = null;
        if(CommandNameConst.CMD_TOP10.equals(cmdName)){
            ircChatMsgCmd = new CmdTop10();
        }

        if(ircChatMsgCmd != null){
            return ircChatMsgCmd.execute(cmd,client);
        }else{
            return true;
        }


    }
}

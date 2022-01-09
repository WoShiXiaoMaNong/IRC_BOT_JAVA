package zm.irc.cmd;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.message.send.IrcPartMessage;

/**
 * <pre>
 * This command is used to part target channel.
 * Usage :
 * zlang Part channel1 [channel2] ...
 * </pre>
 */
public class CmdPart implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdPart.class);


    public CmdPart(){
    }


    @Override
    public boolean execute(IrcReceiveCmdMessage cmd, IrcClient client) {
        String[]  params = cmd.getCmdParams();
        if(params == null){
            log.warn("Param empty!");
            return true;
        }
        this.doPartChannel(cmd,params);
        return true;
    }

    /**
     * <pre>
     * 1. Build {@link IrcPartMessage} base on channel name.
     * 2. Send those Part Message out.
     * </pre>
     * @param cmd
     * @param channelNames
     */
    private void doPartChannel(IrcReceiveCmdMessage cmd,Object[] channelNames){
        if(channelNames == null){
            return;
        }
        for(Object channelName : channelNames) {
            if(channelName == null){
                continue;
            }
            if( ! (channelName instanceof String)){
                continue;
            }
            IrcPartMessage msg = new IrcPartMessage();
            msg.setChannel((String) channelName);
            cmd.getIrcClient().sendMessage(msg);
        }
    }
}

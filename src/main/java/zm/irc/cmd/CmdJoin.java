package zm.irc.cmd;


import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.send.IrcJoinMessage;

/**
 * <pre>
 * This command is used to join target channel.
 * Usage :
 * zlang Join channel1 [channel2] ...
 * </pre>
 */
public class CmdJoin implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdJoin.class);


    public CmdJoin(){
    }


    @Override
    public boolean execute(IrcReceiveCmdMessage cmd, IrcClient client) {
        String[]  params = cmd.getCmdParams();
        if(params == null){
            log.warn("Param empty!");
            return true;
        }
        this.doJoinChannel(cmd,params, client);
        return true;
    }

    /**
     * <pre>
     * 1. Build {@link IrcJoinMessage} base on channel name.
     * 2. Send those Join Message out.
     * </pre>
     * @param cmd
     * @param channelNames
     */
    private void doJoinChannel(IrcReceiveCmdMessage cmd,Object[] channelNames, IrcClient client){
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
            client.join((String) channelName);
        }
    }
}

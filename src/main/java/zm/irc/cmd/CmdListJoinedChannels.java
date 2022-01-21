package zm.irc.cmd;


import org.apache.log4j.Logger;
import zm.irc.client.IrcChannel;
import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.send.IrcChatMessage;

import java.util.Map;

/**
 * <pre>
 * This command is used to part target channel.
 * Usage :
 * zlang ListChannel
 * </pre>
 */
public class CmdListJoinedChannels implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdListJoinedChannels.class);


    public CmdListJoinedChannels(){
    }


    /**
     * <pre>
     * 1. Build {@link IrcChatMessage} base on channel name.
     * 2. Send those Part Message out.
     * </pre>
     */
    @Override
    public boolean execute(IrcReceiveCmdMessage cmd, IrcClient client) {
        String[]  params = cmd.getCmdParams();

        Map<String,IrcChannel> allChannels = client.getAllChannel();
        if(allChannels == null || allChannels.isEmpty()){
            return true;
        }
        IrcChannel targetChannel = client.getChannel(cmd.getChannel());

        allChannels.forEach((key,channel)->{
            String msgStr = String.format(" >> %s : Currently, I'm Listening on :%s",cmd.getFromName(),key );
            targetChannel.send(msgStr);

        });

        return true;
    }


}

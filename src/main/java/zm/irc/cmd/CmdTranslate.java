package zm.irc.cmd;


import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.dao.BaiduTransDao;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.send.IrcChatMessage;
import zm.irc.message.send.IrcJoinMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;
import zm.irc.service.MsgSummaryInfoService;

/**
 * <pre>
 * This command is used to join target channel.
 * Usage :
 * zlang Tr en(or ch) msg
 * </pre>
 */
public class CmdTranslate implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdTranslate.class);
    private static BaiduTransDao dao = new BaiduTransDao();

    private LocalMemoryMsgQueue localMemoryMsgQueue;

    public CmdTranslate(){
        this.localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;
    }


    @Override
    public boolean execute(IrcReceiveCmdMessage cmd, IrcClient client) {
        String[]  params = cmd.getCmdParams();
        if(params == null){
            log.warn("Param empty!");
            return true;
        }
        StringBuilder targetMsg = new StringBuilder();
        for(int i = 1 ; i < params.length;i++){
            targetMsg.append(" ").append(params[i]);
        }

        this.doTrans(cmd,params[0],targetMsg.toString().trim(), client);
        return true;
    }

    /**
     * <pre>
     * 1. Build {@link IrcJoinMessage} base on channel name.
     * 2. Send those Join Message out.
     * </pre>
     * @param cmd
     */
    private void doTrans(IrcReceiveCmdMessage cmd,String distLanguage, String msg, IrcClient client){
        String dist = dao.transTo(msg,distLanguage);

        IrcChatMessage chatMsg = new IrcChatMessage();
        chatMsg.setChannel(cmd.getChannel());
        String msgBody = String.format(" >> %s : %s",cmd.getFromName(),dist);
        chatMsg.setMsg(msgBody);
        this.localMemoryMsgQueue.addSendQueue(chatMsg);

    }
}

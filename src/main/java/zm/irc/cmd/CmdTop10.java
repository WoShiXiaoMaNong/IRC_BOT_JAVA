package zm.irc.cmd;

import org.apache.log4j.Logger;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.service.MsgSummaryInfoService;

public class CmdTop10 implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdTop10.class);
    private MsgSummaryInfoService msgSummaryInfoService;

    public CmdTop10(){
        this.msgSummaryInfoService = new MsgSummaryInfoService();
    }


    @Override
    public boolean execute(IrcReceiveCmdMessage cmd) {
        log.error("tbd");
        return true;
    }
}

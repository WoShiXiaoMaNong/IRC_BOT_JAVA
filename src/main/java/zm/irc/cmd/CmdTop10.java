package zm.irc.cmd;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.dto.ChannelRankingInfo;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.send.IrcChatMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;
import zm.irc.service.MsgSummaryInfoService;

import java.util.Comparator;
import java.util.List;

public class CmdTop10 implements IrcChatMsgCmd{
    private static final Logger log = Logger.getLogger(CmdTop10.class);
    private MsgSummaryInfoService msgSummaryInfoService;

    private LocalMemoryMsgQueue localMemoryMsgQueue;

    public CmdTop10(){
        this.msgSummaryInfoService = new MsgSummaryInfoService();
        this.localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;
    }


    @Override
    public boolean execute(IrcReceiveCmdMessage cmd, IrcClient client) {

        List<ChannelRankingInfo> top10 = msgSummaryInfoService.listTop10();
        if(CollectionUtils.isNotEmpty(top10)){
            /** Sort */
            top10.sort(Comparator.comparingInt(ChannelRankingInfo::getMsgCount).reversed());




           for(int i = 0 ; i < top10.size();i++){
               ChannelRankingInfo top = top10.get(i);
               String msgBody = String.format("Top %d : %s",i + 1, top.getTopMsg());
               IrcChatMessage msg = new IrcChatMessage();
               msg.setChannel(cmd.getChannel());
               msg.setMsg(msgBody);
               this.localMemoryMsgQueue.addSendQueue(msg);
            }



        }

        return true;
    }
}

package zm.irc.service;

import org.apache.log4j.Logger;
import zm.irc.dao.MessageDao;
import zm.irc.dto.ChannelRankingInfo;
import zm.irc.dto.ChatSummary;


import java.util.List;

public class MsgSummaryInfoService {
    private static final Logger log = Logger.getLogger(MsgSummaryInfoService.class);

    private MessageDao messageDao;

    public MsgSummaryInfoService(){
        this.messageDao = new MessageDao();
    }
    /**
     * <pre>
     * Query History chat record in the {channel} for {userName}
     * order by time;
     * </pre>
     * @param userName
     * @param channel
     * @return
     */
    public ChatSummary queryMyChatInfo(String userName, String channel){
        ChatSummary chatSummary = new ChatSummary(userName,channel);

        log.error("tbd");

        return chatSummary;
    }


    public List<ChannelRankingInfo>  listTop10(){


        return this.messageDao.queryTop10();
    }
}

package zm.irc.service;

import org.apache.log4j.Logger;
import zm.irc.dto.ChatSummary;

public class MsgSummaryInfoService {
    private static final Logger log = Logger.getLogger(MsgSummaryInfoService.class);


    /**
     * Query History chat record in the {channel} for {userName}
     * @param userName
     * @param channel
     * @return
     */
    public ChatSummary queryMyChatInfo(String userName, String channel){
        ChatSummary chatSummary = new ChatSummary(userName,channel);

        log.error("tbd");

        return chatSummary;
    }
}

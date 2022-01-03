package zm.irc.dto;

import zm.irc.message.receive.IrcReceiveChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatSummary {
    private String channel;
    private String userName;
    private List<IrcReceiveChatMessage> chatHistory;

    public ChatSummary(String userName,String channel){
        this.chatHistory = new ArrayList<>();
        this.userName = userName;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int countChatRecords(){
        return this.chatHistory.size();
    }

    public void addChatHistory(IrcReceiveChatMessage msg){
        this.chatHistory.add(msg);
    }

    public List<IrcReceiveChatMessage> getChatHistory(){
        return this.chatHistory;
    }

}

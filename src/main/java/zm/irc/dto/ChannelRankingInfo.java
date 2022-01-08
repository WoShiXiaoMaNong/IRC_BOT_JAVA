package zm.irc.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChannelRankingInfo {

    private String channel;
    private int msgCount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    @Override
    public String toString() {
        return "{" +
                "Channel='" + channel + '\'' +
                ", Message Count=" + msgCount +
                ", Start At=" + startAt +
                ", End At=" + endAt +
                '}';
    }

    public String getTopMsg(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%-15s(Messages:%d)   [%s - %s]",channel,msgCount,startAt.format(dtf),endAt.format(dtf));
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(now.format(dtf));
    }
}

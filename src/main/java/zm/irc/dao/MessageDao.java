package zm.irc.dao;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.connpool.DbConnectionPool;
import zm.irc.dto.ChannelRankingInfo;
import zm.irc.message.receive.IrcReceiveChatMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;



public class MessageDao {
    Logger log = Logger.getLogger(MessageDao.class);

    public void saveMessage(IrcReceiveChatMessage msg){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
                conn = DbConnectionPool.getConnection();
                IrcClient ircClient = msg.getIrcClient();
                ps = conn.prepareCall("insert into IRC_LOG (msg_type," +
                        "from_name," +
                        "from_ip," +
                        "content," +
                        "channel," +
                        "host) values(?,?,?,?,?,?)");
                ps.setString(1,"chat_msg");
                ps.setString(2,msg.getFromName());
                ps.setString(3,msg.getFromIp());
                ps.setString(4,msg.getMessageBody());
                ps.setString(5,msg.getChannel());
                ps.setString(6, ircClient.getServerInfo().getServer() + ":" +ircClient.getServerInfo().getPort());
                ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbConnectionPool.close(ps);
            DbConnectionPool.close(conn);
        }
    }


    public List<ChannelRankingInfo> queryTop10(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ChannelRankingInfo> top10= new ArrayList<>();
        try {
                conn = DbConnectionPool.getConnection();
                ps = conn.prepareCall("select channel," +
                        "count(1)," +
                        "min(time)," +
                        "max(time)" +
                        "from IRC_LOG " +
                        "group by channel");
                rs = ps.executeQuery();

                while(rs.next()){
                    ChannelRankingInfo info = new ChannelRankingInfo();
                    info.setChannel(rs.getString(1));
                    info.setMsgCount(rs.getInt(2));

                    Timestamp startAt = (Timestamp)rs.getObject(3);
                    Timestamp endAt = (Timestamp)rs.getObject(4);

                    info.setStartAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(startAt.getTime()), ZoneId.systemDefault()));
                    info.setEndAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(endAt.getTime()), ZoneId.systemDefault()));
                    top10.add(info);
                }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbConnectionPool.close(rs);
            DbConnectionPool.close(ps);
            DbConnectionPool.close(conn);
        }
        return top10;
    }
}

package zm.irc.dao;

import org.apache.log4j.Logger;
import zm.irc.message.receive.IrcReceiveChatMessage;
import zm.irc.message.receive.IrcReceiveMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class MessageDao {
    Logger log = Logger.getLogger(MessageDao.class);

    public void saveMessage(IrcReceiveChatMessage msg){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
                conn = DbConnectionPool.getConnection();

                ps = conn.prepareCall("insert into IRC_LOG (msg_type," +
                        "from_name," +
                        "from_ip," +
                        "content," +
                        "channel) values(?,?,?,?,?)");
                ps.setString(1,"chat_msg");
                ps.setString(2,msg.getFromName());
                ps.setString(3,msg.getFromIp());
                ps.setString(4,msg.getMessageBody());
                ps.setString(5,msg.getChannel());
                ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbConnectionPool.close(ps);
            DbConnectionPool.close(conn);
        }
    }

    public void saveMessage(IrcReceiveMessage msg){
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            if (msg instanceof IrcReceiveChatMessage) {
                conn = DbConnectionPool.getConnection();
                IrcReceiveChatMessage chatMessage = (IrcReceiveChatMessage) msg;
                ps = conn.prepareCall("insert into IRC_LOG (msg_type," +
                        "from_name," +
                        "from_ip," +
                        "content," +
                        "channel) values(?,?,?,?,?)");
                ps.setString(1,"chat_msg");
                ps.setString(2,chatMessage.getFromName());
                ps.setString(3,chatMessage.getFromIp());
                ps.setString(4,chatMessage.getMessageBody());
                ps.setString(5,"0dev");
                ps.execute();
                log.error("测试 聊天消息：" + msg);
            } else {
                log.error("测试：" + msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DbConnectionPool.close(ps);
            DbConnectionPool.close(conn);
        }
    }
}

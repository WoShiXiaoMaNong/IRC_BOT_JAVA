package zm.irc.connpool;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import zm.irc.Irc;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DbConnectionPool {
    private static final Logger log = Logger.getLogger(DbConnectionPool.class);
    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String dbUrl = "jdbc:mysql://z-lang.top:3306/irc?useSSL=false";

    private static final ConcurrentLinkedQueue<Connection> freeConn = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<Integer,Connection> inUsedConn = new ConcurrentHashMap<>();

    public static void init(int poolSize) {
        log.info("INIT : DB Conn pool. Size is :" + poolSize);
        for(int i = 0; i< poolSize;i++){
            log.info("INIT : DB Conn pool. Index " + (i+1));
            freeConn.add(getConnection(Irc.dbUserName,Irc.dbPwd));
        }
        regShutdownHock();
        log.info("INIT finished");
    }

    private static void regShutdownHock(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("Shutting down!");
            shutdown();
        }));
    }

    private static void shutdown(){
        if(CollectionUtils.isNotEmpty(freeConn)){
            freeConn.forEach(con-> {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
       if( !inUsedConn.isEmpty()){
           inUsedConn.forEach((key,value)-> {
               try {
                   value.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           });
       }
    }
    private static Connection getConnection(String user,String pwd){
        try {
            Class.forName(driverName).getDeclaredConstructor().newInstance();
            return DriverManager.getConnection(dbUrl, user, pwd);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(){
       Connection conn = freeConn.poll();
       if(conn != null) {
           inUsedConn.put(conn.hashCode(), conn);
       }
       return conn;
    }


    public static void close(Connection conn){
        if(conn == null){
            return;
        }

        try {
            if(!conn.getAutoCommit()){
                conn.commit();
            }

            inUsedConn.remove(conn.hashCode());
            freeConn.add(conn);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void close(Statement stat){
        if(stat == null){
            return;
        }

        try {
            stat.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void close(ResultSet rs){
        if(rs == null){
            return;
        }

        try {
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

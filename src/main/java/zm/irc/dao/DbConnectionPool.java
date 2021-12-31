package zm.irc.dao;

import zm.irc.Irc;

import java.sql.*;

public class DbConnectionPool {

    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String dbUrl = "jdbc:mysql://z-lang.top:3306/irc&userSSL=false";




    public static Connection getConnection(String user,String pwd){
        try {
            Class.forName(driverName).getDeclaredConstructor().newInstance();
            return DriverManager.getConnection(dbUrl, user, pwd);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(){
        return getConnection(Irc.dbUserName,Irc.dbPwd);
    }


    public static void close(Connection conn){
        if(conn == null){
            return;
        }

        try {
            conn.close();
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

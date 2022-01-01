package zm.irc;

import zm.irc.client.IrcClient;
import zm.irc.dao.DbConnectionPool;
import zm.irc.message.send.IrcSendMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Irc {

    public static String dbUserName="";
    public static String dbPwd="";

    public static void main(String[] args) throws IOException, InterruptedException {
        dbUserName = args[0];
        dbPwd = args[1];
        DbConnectionPool.close(DbConnectionPool.getConnection());
        String server = "irc.libera.chat";
        String nick = "B_FD3";
        String login = "anyName";
        List<String> channel = new ArrayList<>();
        channel.add("#linuxba");
        channel.add("#0dev");
        channel.add("#c_lang_cn");
        channel.add("#linux");

        IrcClient client = new IrcClient(server,IrcClient.DEFAULT_PORT,nick,channel);
        client.start();
    }



}

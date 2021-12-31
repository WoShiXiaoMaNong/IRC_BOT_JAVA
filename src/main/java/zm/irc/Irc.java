package zm.irc;

import zm.irc.client.IrcClient;
import zm.irc.message.send.IrcSendMessage;

import java.io.*;

public class Irc {

    public static String dbUserName="";
    public static String dbPwd="";

    public static void main(String[] args) throws IOException, InterruptedException {
        dbUserName = args[0];
        dbPwd = args[1];
        String server = "irc.libera.chat";
        String nick = "B_FD2";
        String login = "anyName";
        String channel = "#0dev";

        IrcClient client = new IrcClient(server,IrcClient.DEFAULT_PORT,nick,channel);
        client.start();
    }



}

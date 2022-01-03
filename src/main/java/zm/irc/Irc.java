package zm.irc;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.connpool.DbConnectionPool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Irc {
    private static final Logger log = Logger.getLogger(Irc.class);
    public static String dbUserName="";
    public static String dbPwd="";
    public static String nick = "bfd_bot";

    /**
     * Startup : java -jar ./target/{Jar file name}.jar {db username} {db pwd}
     * Startup backend : nohup java -jar ./target/{Jar file name}.jar {db username} {db pwd} >> ./console.log 2>&1 &
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */

    public static void main(String[] args) throws IOException, InterruptedException {

        if(args == null || args.length !=2 ){
            throw new RuntimeException("Invalid args!" + args);
        }
        dbUserName = args[0];
        dbPwd = args[1];

        log.info("DB CONN POOL Init...");
        DbConnectionPool.init(5);

        log.info("DB CONN Self Test...");
        DbConnectionPool.close(DbConnectionPool.getConnection());

        String server = "irc.libera.chat";

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

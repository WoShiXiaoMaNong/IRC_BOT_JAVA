package zm.irc;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.client.ServerInfo;
import zm.irc.connpool.DbConnectionPool;

import java.io.*;

public class RunIrcClientBackEnd {
    private static final Logger log = Logger.getLogger(RunIrcClientBackEnd.class);
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

        init(dbUserName,dbPwd);
        IrcClient libera = initClient();

        /**
         * Loop all channel and clean their message buffer.
         * The client will consume message from current channel's buffer.
         */
        while(true){
            try {
                Thread.sleep(50);
                libera.switchToNextChannel(); // Switch current channel one by one.
            }catch (Exception e){
                log.error("Switch channel error！",e);
            }
        }

    }


    private static void init(String dbUserName,String dbPwd){
        log.info("DB CONN POOL Init...");
        DbConnectionPool.init(5);

        log.info("DB CONN Self Test...");
        DbConnectionPool.close(DbConnectionPool.getConnection());
    }

    private static IrcClient initClient() throws IOException {
        ServerInfo liberaServerInfo = new ServerInfo("irc.libera.chat", IrcClient.DEFAULT_PORT);
        liberaServerInfo.addChannel("#linuxba");
        liberaServerInfo.addChannel("#c_lang_cn");
        liberaServerInfo.addChannel("#zmtest");
     //   liberaServerInfo.addChannel("#linux");
        liberaServerInfo.addChannel("#0dev");

        IrcClient libera = new IrcClient(liberaServerInfo, nick);
        libera.start();

        return libera;







    }
}



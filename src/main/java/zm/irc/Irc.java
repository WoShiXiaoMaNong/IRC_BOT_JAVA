package zm.irc;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.client.ServerInfo;
import zm.irc.client.SubscribeInfo;
import zm.irc.connpool.DbConnectionPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Irc {
    private static final Logger log = Logger.getLogger(Irc.class);
    public static String dbUserName="";
    public static String dbPwd="";
    public static String nick = "bfd_bot11";

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
        doStart();

    }


    private static void init(String dbUserName,String dbPwd){
        log.info("DB CONN POOL Init...");
        DbConnectionPool.init(5);

        log.info("DB CONN Self Test...");
        DbConnectionPool.close(DbConnectionPool.getConnection());
    }

    private static void doStart() throws IOException {


        /**Key host+channel : irc.2600.net#zmtest, value the Subscribes of this channel*/
        Map<String, SubscribeInfo> subscribeInfoMap = new HashMap();

        SubscribeInfo s = new SubscribeInfo("irc.libera.chat","#zmtest",null);

        ServerInfo liberaServerInfo = new ServerInfo("irc.libera.chat",IrcClient.DEFAULT_PORT);
        liberaServerInfo.addChannel("#linuxba");
        liberaServerInfo.addChannel("#0dev");
        liberaServerInfo.addChannel("#c_lang_cn");
        liberaServerInfo.addChannel("#zmtest");

        IrcClient libera = new IrcClient(liberaServerInfo,nick);
        new Thread(()->{
            try {
                libera.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        ServerInfo irc2600ServerInfo = new ServerInfo("irc.2600.net",IrcClient.DEFAULT_PORT);

        irc2600ServerInfo.addChannel("#zmtest");

        IrcClient irc2600 = new IrcClient(irc2600ServerInfo,nick);
        new Thread(()->{
            try {
                irc2600.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


}



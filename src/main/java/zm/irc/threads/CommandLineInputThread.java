package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcChannel;
import zm.irc.client.IrcClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CommandLineInputThread implements Runnable{
    private static Logger log = Logger.getLogger(CommandLineInputThread.class);

    private IrcClient ircClient;

    public CommandLineInputThread(IrcClient ircClient){
        this.ircClient = ircClient;
    }

    @Override
    public void run() {
        try {
            log.info("消息输入线程启动！");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                String msgStr = br.readLine();
                if(msgStr.startsWith("/zc")){
                    this.processAsCommand(msgStr);
                    continue;
                }

                IrcChannel channel = ircClient.getCurrentChannel();
                if(channel != null){
                    channel.send(msgStr);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void processAsCommand(String command){
        String param[] = command.split(" ");
        if("SC".equals(param[1])){
            String channelName = param[2];
            this.ircClient.switchChannel(channelName);
        }else if("join".equals(param[1])){
            String channelName = param[2];
            this.ircClient.join(channelName);
        }
    }
}

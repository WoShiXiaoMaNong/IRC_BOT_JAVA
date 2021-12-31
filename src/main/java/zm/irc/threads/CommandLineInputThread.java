package zm.irc.threads;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.send.IrcSendMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;


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
                IrcSendMessage msg = IrcSendMessage.build(ircClient.getChannel(), msgStr);
                this.ircClient.sendMessage(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

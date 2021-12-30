package zm.irc.message.processor;

import org.apache.log4j.Logger;
import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveMessage;
import zm.irc.message.receive.IrcReceivePingMessage;
import zm.irc.message.send.IrcPongMessage;



public class PingMessageProcessor implements IrcMessageProcessor{
    private static final Logger log = Logger.getLogger(PingMessageProcessor.class);
    @Override
    public boolean processor(IrcClient client, IrcReceiveMessage receivedMsg) {

        /* If this is a Ping message */
        /* Response a PONG message to server*/
        /* Else skip this processor */
        if( receivedMsg instanceof IrcReceivePingMessage){
            IrcPongMessage pong = new IrcPongMessage();
            pong.setOriginPingMessage(receivedMsg.getOriginMsg());
            client.sendMessage(pong);
            log.debug("PONG message sent");
            return false;
        }else{
            return true;
        }

    }
}

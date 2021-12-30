package zm.irc.message.processor;

import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveMessage;

public class PrintProcessor implements IrcMessageProcessor{
    @Override
    public boolean processor(IrcClient client, IrcReceiveMessage receivedMsg) {
        if( receivedMsg.shouldPrint()){
            System.out.println(receivedMsg.getMessageForPrint());
        }
        return true;
    }
}

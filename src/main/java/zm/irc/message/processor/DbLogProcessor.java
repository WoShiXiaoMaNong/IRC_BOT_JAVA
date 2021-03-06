package zm.irc.message.processor;

import zm.irc.client.IrcClient;
import zm.irc.dao.MessageDao;
import zm.irc.message.receive.IrcReceiveChatMessage;
import zm.irc.message.receive.IrcReceiveMessage;

public class DbLogProcessor implements IrcMessageProcessor{

    private MessageDao messageDao;

    public DbLogProcessor(){
        this.messageDao = new MessageDao();
    }

    @Override
    public boolean processor(IrcClient client, IrcReceiveMessage receivedMsg) {

        if(receivedMsg instanceof  IrcReceiveChatMessage) {
            this.messageDao.saveMessage((IrcReceiveChatMessage)receivedMsg);
        }else{
            //TBD
        }

        return true;
    }
}

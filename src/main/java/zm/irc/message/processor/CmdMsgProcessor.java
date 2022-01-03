package zm.irc.message.processor;

import zm.irc.client.IrcClient;
import zm.irc.cmd.CmdExecutor;
import zm.irc.message.receive.IrcReceiveChatMessage;
import zm.irc.message.receive.IrcReceiveCmdMessage;
import zm.irc.message.receive.IrcReceiveMessage;

public class CmdMsgProcessor implements IrcMessageProcessor{

    private CmdExecutor cmdExecutor;

    public CmdMsgProcessor(){
        this.cmdExecutor = new CmdExecutor();
    }

    @Override
    public boolean processor(IrcClient client, IrcReceiveMessage receivedMsg) {
        boolean shouldContinuePipeLine = true;
        if( receivedMsg instanceof IrcReceiveChatMessage){
            IrcReceiveChatMessage chatMessage = (IrcReceiveChatMessage) receivedMsg;
            if( chatMessage.isCmdMes()){
                IrcReceiveCmdMessage cmd = chatMessage.convertTo();
                shouldContinuePipeLine = this.cmdExecutor.executor(cmd,client);
            }
        }
        return shouldContinuePipeLine;
    }

}

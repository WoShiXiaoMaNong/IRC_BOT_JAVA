package zm.irc.message.processor;

import zm.irc.client.IrcClient;
import zm.irc.message.receive.IrcReceiveMessage;

public interface IrcMessageProcessor {

    /**
     * 返回true,继续下一个处理器
     * 返回fasel，接触当前消息的处理
     * @param receivedMsg
     * @return
     */
    boolean processor(IrcClient client, IrcReceiveMessage receivedMsg);
}

package zm.irc.threads;

import org.apache.log4j.Logger;

import zm.irc.dao.BaiduTransDao;
import zm.irc.dto.TranslateInfo;
import zm.irc.message.send.IrcChatMessage;
import zm.irc.msgqueue.LocalMemoryMsgQueue;


public class TranslateThread implements Runnable{
    public static final String queueName = "TranslateQueue_";
    private static Logger log = Logger.getLogger(MsgSendThread.class);

    private LocalMemoryMsgQueue localMemoryMsgQueue = LocalMemoryMsgQueue.localMemoryMsgQueue;

    private  BaiduTransDao dao =BaiduTransDao.dao;

    public TranslateThread( ){
        localMemoryMsgQueue.registerCommonQueue(queueName);
    }

    @Override
    public void run() {
        log.info("Transalte Thread Start.");
        Object msg = localMemoryMsgQueue.getMsgFromCommonQueue(queueName);
        while(true){
            try{
                if(msg != null && (msg instanceof TranslateInfo)){
                    TranslateInfo translateInfo = (TranslateInfo)msg;
                    String dist = dao.transTo(translateInfo.getMsg(),translateInfo.getDistLanguage());
                    IrcChatMessage chatMsg = new IrcChatMessage();
                    chatMsg.setChannel(translateInfo.getChannelName());
                    String msgBody = String.format(" >> %s : %s",translateInfo.getRequesterName(),dist);
                    chatMsg.setMsg(msgBody);
                    this.localMemoryMsgQueue.addSendQueue(chatMsg);
                    Thread.sleep(1200);//Due to the translate api's QRS is only 1.
                }else{
                    Thread.sleep(50);//Due to the translate api's QRS is only 1.
                }
        
                msg =  localMemoryMsgQueue.getMsgFromCommonQueue(queueName);
               
            }catch (Exception e){
                log.error("error",e);
            }
        }
    }
}

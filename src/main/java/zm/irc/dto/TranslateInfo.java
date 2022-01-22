package zm.irc.dto;

public class TranslateInfo {
    
    private String msg;
    private String distLanguage;
    private String channelName;
    private String requesterName;

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getDistLanguage() {
        return distLanguage;
    }
    public void setDistLanguage(String distLanguage) {
        this.distLanguage = distLanguage;
    }
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    
}

package zm.irc.client;

import zm.irc.Irc;

public class SubscribeInfo {
    private String server;
    private String channel;
    private IrcClient client;
    public SubscribeInfo(String server, String channel,IrcClient client) {
        this.server = server;
        this.channel = channel;
        this.client = client;
    }

    public IrcClient getClient() {
        return client;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}

package zm.irc.client;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerInfo {
    private String server;
    private int port;
    private List<String> channels;

    public ServerInfo(String server,int port) {
        this.server = server;
        this.port = port;
        this.channels = new ArrayList<>();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public List<String> getChannel() {
        return channels;
    }

    public void addChannel(String channel) {
        this.channels.add(channel);
    }
}

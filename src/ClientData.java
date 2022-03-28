public class ClientData {
    private String clientName;
    private String channelName;

    public ClientData(String clientName, String channelName) {
        this.clientName = clientName;
        this.channelName = channelName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

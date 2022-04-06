import java.util.ArrayList;
import java.util.List;

public class ClientData {
    private final String clientName;
    private final List<String> channels = new ArrayList<>();
    private String currentChannel;

    public ClientData(String clientName) {
        this.clientName = clientName;
        this.channels.add("main");
        this.currentChannel = channels.get(0);
    }

    public String getClientName() {
        return clientName;
    }

    public String getCurrentChannel() {
        return currentChannel.substring(0, 1).toUpperCase() + currentChannel.substring(1).toLowerCase();
    }

    public void setCurrentChannel(String currentChannel) {
        String styledName = currentChannel
                .replaceAll("\\s", "")
                .substring(0, 1)
                .toUpperCase()
                + currentChannel
                .replaceAll("\\s", "")
                .substring(1)
                .toLowerCase();
        this.currentChannel = styledName;
        if (!channels.contains(styledName)) {
            this.channels.add(currentChannel.toLowerCase());
        }
    }

    public List<String> getChannels() {
        return channels;
    }
}

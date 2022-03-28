import java.net.Socket;

public class SocketData {
    private String socketName;

    public SocketData(Socket socket) {
        setSocketName(socket.toString());
    }

    public String getSocketName() {
        return socketName;
    }

    public void setSocketName(String socketName) {
        this.socketName = socketName;
    }
}

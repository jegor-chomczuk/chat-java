import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String SERVER_MESSAGE_COLOR = "\u001B[32m";
    private static final String COLOR_RESET = "\u001B[0m";
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        File historyFile = new File("history.txt");
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(8000);
        Server server = new Server(serverSocket);
        server.startServer();
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println(SERVER_MESSAGE_COLOR + "User [" + clientHandler.getClientData().getClientName() + "] has connected to the server" + COLOR_RESET);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
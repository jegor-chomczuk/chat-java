import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final String SYSTEM_MESSAGE_COLOR = "\u001B[34m";
    private static final String LOGO_COLOR = "\u001B[33m";
    private static final String SYSTEM_INFO_COLOR = "\u001B[37m";
    private static final String COLOR_RESET = "\u001B[0m";
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;
    private String randomColor;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.clientName = userName;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.randomColor = randomColor(Color.class);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        printLogo();

        System.out.println(SYSTEM_MESSAGE_COLOR
                + "### Enter your username for a group chat ###\n"
                + COLOR_RESET);

        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 8000);
        Client client = new Client(socket, userName);

        System.out.println(SYSTEM_INFO_COLOR
                + "\nType "
                + COLOR_RESET
                + "help: "
                + SYSTEM_INFO_COLOR
                + "to see available commands or simply start chatting with other people\n"
                + COLOR_RESET);

        System.out.println(SYSTEM_MESSAGE_COLOR
                + "### Welcome to channel: "
                + COLOR_RESET
                + "Main "
                + SYSTEM_MESSAGE_COLOR
                + "###\n"
                + COLOR_RESET);

        client.listenForMessage();
        client.sendMessage();
    }

    public static String randomColor(Class<Color> color) {
        Random random = new Random();
        int randomNumber = random.nextInt(color.getEnumConstants().length);
        return color.getEnumConstants()[randomNumber].getLabel();
    }

    public static void printLogo() {
        System.out.println(LOGO_COLOR +
                "\n,-_/               ,--. .       .  \n" +
                  "'  | ,-. .  , ,-. | `-' |-. ,-. |- \n" +
                  "   | ,-| | /  ,-| |   . | | ,-| |  \n" +
                  "   | `-^ `'   `-^ `--'  ' ' `-^ `' \n" +
                  "/` |                               \n" +
                  "`--'\n"
                + COLOR_RESET
        );
    }

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);

        try {
            bufferedWriter.write(this.clientName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();

                if (messageToSend.contains("exit:")) {
                    System.exit(0);
                } else {
                    bufferedWriter.write(this.randomColor + "[" + this.clientName + "]" + COLOR_RESET + ": " + messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String messageFromGroupChat;

            while (socket.isConnected()) {
                try {
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
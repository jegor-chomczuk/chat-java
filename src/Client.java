import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ClientData clientData;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.clientData = new ClientData(userName, socket.toString());
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.userName = userName;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for a group chat: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 8000);
        Client client = new Client(socket, userName);
        System.out.println("Welcome to main socket: " + client.clientData.getChannelName());
        client.listenForMessage();
        client.sendMessage();
    }

    public void sendMessage() {
        Scanner scanner = new Scanner(System.in);

        try {
            bufferedWriter.write(this.clientData.getClientName());
            bufferedWriter.newLine();
            bufferedWriter.flush();


            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(this.clientData.getClientName() + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);

                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public String writeToChatArchive(String message, BufferedWriter bufferedWriter) throws IOException {

//            File file = new File("chatArchive.txt");
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileWriter fileWriter = new FileWriter(file);
//            BufferedWriter archiveWriter = new BufferedWriter(fileWriter);
//            archiveWriter.write(message);
//            System.out.println("File written Successfully");

//                Date date = new Date(System.currentTimeMillis());
//                String messageFromGroupChat;
//
//                        Writer writer = new FileWriter( "chatArchive.txt", true);
//                        writer.write(date + " " + clientData.getClientName() + message + "\n");

        Date date = new Date(System.currentTimeMillis());
        BufferedWriter bw = bufferedWriter;

        File file = new File("chatArchive.txt");
        if (!file.exists()) {
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter.write(date + " " + this.clientData.getClientName() + " :" + message + "\n");
            System.out.println("File written Successfully");
        }
        return "File written Successfully";
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

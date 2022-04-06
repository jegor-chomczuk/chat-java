import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;

public class ClientHandler implements Runnable {
    private static final ArrayList<ClientHandler> CLIENTS = new ArrayList<>();
    private static final String SERVER_MESSAGE_COLOR = "\u001B[32m";
    private static final String SYSTEM_MESSAGE_COLOR = "\u001B[34m";
    private static final String SYSTEM_CRITICAL_MESSAGE_COLOR = "\u001B[35m";
    private static final String SYSTEM_INFO_COLOR = "\u001B[37m";
    private static final String COLOR_RESET = "\u001B[0m";
    private static History history;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ClientData clientData;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientData = new ClientData(bufferedReader.readLine());
            CLIENTS.add(this);
            history = new History();
            try (Formatter formatter = new Formatter()) {
                broadcastMessage(formatter.format("%s### User%s %s %shas entered the chat ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , clientData.getClientName()
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public ClientData getClientData() {
        return clientData;
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();

                if (messageFromClient.toLowerCase().contains("channel:")) {
                    switchChannel(messageFromClient);

                } else if (messageFromClient.toLowerCase().contains("history:")) {
                    printChannelHistory(messageFromClient.toLowerCase());

                } else if (messageFromClient.toLowerCase().contains("showmychannels:")) {
                    showChannels();

                } else if (messageFromClient.toLowerCase().contains("sendfile:")) {
                    downloadFile(messageFromClient);

                } else if (messageFromClient.toLowerCase().contains("help:")) {
                    printCommandsAvailableInChat();

                } else {
                    writeToChatHistory(messageFromClient);
                    broadcastMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void useBufferWriter(String text) throws IOException {
        bufferedWriter.write(text);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void useClientBufferWriter(ClientHandler client, String text) throws IOException {
        client.bufferedWriter.write(text);
        client.bufferedWriter.newLine();
        client.bufferedWriter.flush();
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler client : CLIENTS) {
            try {
                //Prevents from displaying client's own messages
                if (!clientData.getClientName().equals(client.clientData.getClientName())
                        //Prevents from displaying messages to clients on different channels
                        && client.clientData.getCurrentChannel().equals(clientData.getCurrentChannel())) {
                    useClientBufferWriter(client, messageToSend);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    private void showChannels() throws IOException {
        for (String channelName : this.clientData.getChannels()) {
            useBufferWriter(upperCaseStyling(channelName));
        }
    }

    public String upperCaseStyling(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public int getSubstringValue(int value) {
        return clientData.getClientName().length() + value;
    }

    public void switchChannel(String newChannelName) {
        Formatter formatter1 = new Formatter();
        Formatter formatter2 = new Formatter();

        try (formatter1; formatter2) {
            try {
                int messageSubstringValue = getSubstringValue(21);
                this.clientData.setCurrentChannel(newChannelName.substring(messageSubstringValue));

                //Log to server about user's current channel switch
                System.out.println(formatter1.format("%sUser [%s] has switched his/her current channel to %s%s"
                        , SERVER_MESSAGE_COLOR
                        , clientData.getClientName()
                        , clientData.getCurrentChannel()
                        , COLOR_RESET));

                useBufferWriter(formatter2.format("%s### Welcome to channel: %s%s%s ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , this.clientData.getCurrentChannel()
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());

                broadcastMessage(formatter2.format("%s### User %s%s%s has entered the channel ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , clientData.getClientName()
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void writeToChatHistory(String message) throws IOException {
        String historyRecord;
        try (Formatter formatter = new Formatter()) {
            int messageSubstringValue = getSubstringValue(13);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            historyRecord = formatter.format("[%s], [%s], [%s]: %s%n"
                            , this.clientData.getCurrentChannel()
                            , this.clientData.getClientName()
                            , dtf.format(now)
                            , message.substring(messageSubstringValue))
                    .toString();
        }
        history.writeToChatHistory(historyRecord);
    }

    public void printChannelHistory(String historyRequest) throws IOException {
        int historyRequestSubstringValue = getSubstringValue(21);
        String channelName = upperCaseStyling(historyRequest.substring(historyRequestSubstringValue));
        InputStream inputStream = new FileInputStream("history.txt");
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Formatter formatter = new Formatter();

        try (formatter; reader) {
            String line = reader.readLine();
            if (this.clientData.getChannels().contains(channelName.toLowerCase())) {

                useBufferWriter(formatter.format("%s### History of the channel %s%s%s: ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , channelName
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());

                history.printChannelHistory(line, channelName, SYSTEM_INFO_COLOR, bufferedWriter, reader);

                useBufferWriter(COLOR_RESET);
            } else {
                useBufferWriter(formatter.format("%s### History of the channel %s%s%s is not available ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , channelName
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void downloadFile(String fileSendRequest) throws IOException {
        Formatter formatter = new Formatter();
        int fileSendRequestSubstringValue = clientData.getClientName().length() + 22;
        String filePath = fileSendRequest.substring(fileSendRequestSubstringValue);
        String fileName = getFileName(filePath);

        try {
            if (CLIENTS.size() < 2) {
                useBufferWriter(formatter.format("%n%s### There are no other users on the channel to receive a file ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());
            } else if (!filePath.equals("")) {
                for (ClientHandler client : CLIENTS) {
                    //  Prevents from downloading client's own file
                    if (!client.clientData.getClientName().equals(clientData.getClientName())
                            //  Prevents from downloading file by clients on different channels
                            && client.clientData.getCurrentChannel().equals(clientData.getCurrentChannel())) {

                        FileInputStream fileInputStream = new FileInputStream(filePath);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                        FileOutputStream fileOutputStream = new FileOutputStream("[" + client.clientData.getClientName() + "] " + fileName);

                        //  Creates buffer for sending data of 1024 bytes
                        byte[] data = new byte[1024];
                        int count;
                        while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
                            fileOutputStream.write(data, 0, count);
                        }

                        fileInputStream.close();
                        bufferedInputStream.close();
                        fileOutputStream.close();
                    }
                }
                broadcastMessage(formatter.format("%s### File sent by %s%s%s has been downloaded ###%s%n"
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET
                                , clientData.getClientName()
                                , SYSTEM_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());

            } else {
                useBufferWriter(formatter.format("%n%s### File does not exist. Check if the file path is correct ###%s%n"
                                , SYSTEM_CRITICAL_MESSAGE_COLOR
                                , COLOR_RESET)
                        .toString());
            }
        } catch (FileNotFoundException e) {
            useBufferWriter(formatter.format("%n%s### File does not exist. Check if the file path is correct ###%s%n"
                            , SYSTEM_CRITICAL_MESSAGE_COLOR
                            , COLOR_RESET)
                    .toString());
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String getFileName(String filePath) {
        int i = filePath.lastIndexOf('\\');
        String fileName = null;
        if (i > 0) {
            fileName = filePath.substring(i + 1);
        }
        return fileName;
    }

    public void printCommandsAvailableInChat() throws IOException {
        try (Formatter formatter = new Formatter()) {
            useBufferWriter(formatter.format("%s### Available commands: ###%s%n" +
                                    "- channel:{channel name} - %sswitch to a new channel%s%n" +
                                    "- showmychannels: - %scheck channels you have visited%s%n" +
                                    "- histoty:{channel name} - %scheck the history of one of the channels you have visited%s%n" +
                                    "- sendfile:{file path} - %ssend a file to other members of your current channel%s%n" +
                                    "- help: - %scheck available commands%s%n" +
                                    "- exit: - %squit the chat%s%n"
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET
                            , SYSTEM_MESSAGE_COLOR
                            , COLOR_RESET)
                    .toString());
        }
    }

    public void removeClientHandler() {
        Formatter formatter1 = new Formatter();
        Formatter formatter2 = new Formatter();
        try (formatter1; formatter2) {
            CLIENTS.remove(this);
            broadcastMessage(formatter1.format("%s### User %s%s%s has left the chat ###%s%n"
                            , SYSTEM_CRITICAL_MESSAGE_COLOR
                            , COLOR_RESET
                            , clientData.getClientName()
                            , SYSTEM_CRITICAL_MESSAGE_COLOR, COLOR_RESET)
                    .toString());

            //Log to server about user's leave from chat
            System.out.println(formatter2.format("%sUser [%s] has left he chat%s"
                    , SERVER_MESSAGE_COLOR
                    , clientData.getClientName()
                    , COLOR_RESET));
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
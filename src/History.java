import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class History {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void writeToChatHistory(String historyRecord) throws IOException {
        lock.writeLock().lock();
        try {
            try (FileWriter fileWriter = new FileWriter("history.txt", true)) {
                fileWriter.write(historyRecord);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void printChannelHistory(String line, String channelName, String SYSTEM_INFO_COLOR, BufferedWriter bufferedWriter, BufferedReader reader) throws IOException {
        lock.readLock().lock();
        try {
            while (line != null) {
                if (line.startsWith("[" + channelName)) {
                    bufferedWriter.write(SYSTEM_INFO_COLOR + line);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                line = reader.readLine();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
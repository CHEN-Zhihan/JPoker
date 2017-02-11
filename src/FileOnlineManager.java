import java.io.*;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhihan on 2/7/17.
 */
public class FileOnlineManager extends FileManager implements OnlineManager {
    private HashSet<String> users;
    FileOnlineManager() {
        file = new File("./OnlineUser.txt");
        try {
            boolean result = (!file.exists() || file.delete()) && file.createNewFile();
            file.deleteOnExit();
            users = new HashSet<>();
        } catch (IOException e) {
            System.err.println("Error creating " + file.getName() + " "+ e);
        }
    }

    protected void subWrite() throws IOException {
        output.writeObject(users);
    }

    public boolean isOnline(String username) {
        read.lock();
        try {
            return users.contains(username);
        } finally {
            read.unlock();
        }

    }

    public void remove(String username) {
        write.lock();
        try {
            users.remove(username);
            writeAll();
        } finally {
            write.unlock();
        }

    }
    public void add(String username) {
        write.lock();
        try {
            users.add(username);
            writeAll();
        } finally {
            write.unlock();
        }
    }

    public HashSet<String> getUsers() {
        return users;
    }
}

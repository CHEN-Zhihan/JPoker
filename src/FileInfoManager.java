import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhihan on 2/7/17.
 */
public class FileInfoManager extends FileManager implements InfoManager {
    private HashMap<String, User> users;
    private HashMap<String, char[]> keys;
    private PasswordManager pm;
    private ObjectInputStream input;
    FileInfoManager() {
        file = new File("./UserInfo.txt");
        readAll();
        pm = PasswordManager.getInstance();
    }

    private void readAll() {
        try {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            users = (HashMap<String, User>)input.readObject();
            keys = (HashMap<String, char[]>)input.readObject();
            input.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found while reading " + file.getName() + " " + e);
        } catch (EOFException e) {
            users = new HashMap<>();
            keys = new HashMap<>();
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
                users = new HashMap<>();
                keys = new HashMap<>();
            } catch (IOException ex) {
                System.err.println("Error creating new file: " + ex);
            }
        } catch (IOException e) {
            System.err.println("Reading UserInfo.txt error: " + e);
        }
    }

    protected void subWrite() throws IOException{
        output.writeObject(users);
        output.writeObject(keys);
    }

    public void update(String username, User user) {
        write.lock();
        try {
            users.replace(username, user);
            writeAll();
        } finally {
            write.unlock();
        }

    }
    public User register(String username, char[] password) {
        write.lock();
        try {
            User newUser = new User(username);
            users.put(username, newUser);
            keys.put(username, pm.encrypt(password));
            writeAll();
            return newUser;
        } finally {
            write.unlock();
        }

    }
    public User getUser(String username, char[] password) {
        read.lock();
        try {
            if (!users.containsKey(username)) {
                System.err.println("Not Exist User");
                return new NotExistUser();
            }
            if (!pm.authenticate(password, keys.get(username))) {
                System.err.println("Incorrect password");
                return new IncorrectUser();
            }
            System.out.println("login successful");
            return users.get(username);
        } finally {
            read.unlock();
        }

    }
    public HashMap<String, User> getUsers() {
        read.lock();
        try {
            return users;
        } finally {
            read.unlock();
        }
    }

    public boolean exists(String username) {
        read.lock();
        try {
            return users.containsKey(username);
        } finally {
            read.unlock();
        }
    }
}

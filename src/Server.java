import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by zhihan on 2/7/17.
 */
public class Server extends UnicastRemoteObject implements UserManager{

    private InfoManager infoManager;
    private OnlineManager onlineManager;
    Server() throws RemoteException {
        infoManager = new FileInfoManager();
        onlineManager = new FileOnlineManager();
    }

    public User login(String username, char[] password) throws RemoteException {
        System.out.println("receive login request: " + username);
        if (onlineManager.isOnline(username)) {
            return new LoggedInUser();
        }
        User u = infoManager.getUser(username, password);
        if (!(u instanceof IncorrectUser) && !(u instanceof NotExistUser)) {
            onlineManager.add(username);
        }
        return u;
    }
    public User register(String username, char[] password) throws RemoteException {
        System.out.println("receive register request: " + username);
        if (infoManager.exists(username)) {
            return new ExistUser();
        }
        User newUser = infoManager.register(username, password);
        onlineManager.add(username);
        System.out.println("registration successful: " + username);
        return new User(newUser);
    }
    public void logout(String username) throws RemoteException {
        System.out.println("receive logout request: " + username);
        onlineManager.remove(username);
        System.out.println("logout successful: " + username);
    }

    public Iterable<User> getAllUsers() {
        ArrayList<User> l = new ArrayList<>(infoManager.getUsers().values());
        Collections.sort(l);
        return l;
    }

    public Iterable<User> getOnlineUsers() {
        ArrayList<User> users = new ArrayList<>();
        HashMap<String, User> allUsers = infoManager.getUsers();
        for (String s : onlineManager.getUsers()) {
            users.add(allUsers.get(s));
        }
        Collections.sort(users);
        return users;
    }
}
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by zhihan on 2/9/17.
 */
class Client {
    private final String hostIP;
    private UserManager userManager;
    private int id = -1;
    private JMSClient jms;
    private GamePanel g;
    private int gameID;
    private final int port;
    private String username;
    Client(String hostIP, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostIP);
            userManager = (UserManager)registry.lookup("userManager");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("[ERROR] Cannot find userManager at " + hostIP + " " + e);
            e.printStackTrace();
            System.exit(-1);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::logout));
        this.hostIP = hostIP;
        this.port = port;
    }

    User getUser() {
        try {
            User u = userManager.getUser(id);
            if (u != null && username == null) {
                username = u.getUsername();
            }
            return u;
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get user " + e);
            e.printStackTrace();
            return null;
        }
    }

    int getRank() {
        try {
            return userManager.getRank(id);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get rank of " + id + " " + e);
            e.printStackTrace();
            return -1;
        }
    }

    ArrayList<User> getAllUsers() {
        try {
            return userManager.getAllUsers();
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get all users " + e);
            e.printStackTrace();
            return null;
        }
    }

    int login(String username, char[] password) {
        password = PasswordManager.encrypt(password);
        if (password == null) {
            return PasswordManager.ENCRYPT_ERROR;
        }
        int i;
        try {
            i = userManager.login(username, password);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot login " + e);
            e.printStackTrace();
            return UserManager.REMOTE_ERROR;
        }
        if (i >= 0) {
            id = i;
            setJMS();
        }
        return UserManager.VALID;
    }

    int register(String username, char[] password) {
        password = PasswordManager.encrypt(password);
        if (password == null) {
            return PasswordManager.ENCRYPT_ERROR;
        }
        int result;
        try {
            result = userManager.register(username, password);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot register " + e);
            e.printStackTrace();
            return UserManager.REMOTE_ERROR;
        }
        if (result > 0) {
            id = result;
            setJMS();
        }
        return UserManager.VALID;
    }

    void logout() {
        try {
            if (id != -1) {
                userManager.logout(id);
                id = -1;
            }
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot logout " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void onStart(StartMessage m) {
        gameID = m.getGameID();
        g.start(m.getCards(), m.getUsers());
    }

    void onEnd(EndMessage m) {
        g.end(m.getWinner(), m.getSolution());
    }

    void onMessage(ServerMessage m) {

        m.execute(this);
    }

    void setObserver(GamePanel g) {
        this.g = g;
    }

    void request() {
        jms.sendMessage(new RequestMessage(id));
        System.out.println("[INFO] Sent game request");
    }

    void complete(String solution) {
        jms.sendMessage(new FinishedMessage(id, username, gameID, solution));
    }

    private void setJMS() {
        try {
            jms = new JMSClient(hostIP, port, id, this);
        } catch (NamingException | JMSException e) {
            System.err.println("[ERROR] Cannot setup JMS Client: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

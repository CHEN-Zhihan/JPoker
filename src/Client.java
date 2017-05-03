import javax.jms.JMSException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by zhihan on 2/9/17.
 */
class Client {
    private String hostIP;
    private UserManager userManager;
    private int id = -1;
    private JMSClient jms;
    private GamePanel g;
    private int gameID;
    private int port;
    Client(String hostIP, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostIP);
            userManager = (UserManager)registry.lookup("userManager");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logout();
            }));
        } catch (Exception e) {
            System.err.println("Failed accessing RMI: " + e);
            System.exit(-1);
        }
        this.hostIP = hostIP;
        this.port = port;
    }

    User getUser() {
        try {
            return userManager.getUser(id);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get user " + e);
            return null;
        }
    }

    int getRank() {
        try {
            return userManager.getRank(id);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get rank of " + id + " " + e);
            return -1;
        }
    }

    ArrayList<User> getAllUsers() {
        try {
            return userManager.getAllUsers();
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get all users " + e);
            return new ArrayList<>();
        }
    }

    int login(String username, char[] password) throws RemoteException{
        password = PasswordManager.getInstance().encrypt(password);
        int i;
        try {
            i = userManager.login(username, password);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot login " + e);
            return UserManager.DATABASE_ERROR;
        }
        if (i >= 0) {
            id = i;
            setJMS();
        }
        return i;
    }

    int register(String username, char[] password) {
        password = PasswordManager.getInstance().encrypt(password);
        int result;
        try {
            result = userManager.register(username, password);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot register " + e);
            return UserManager.DATABASE_ERROR;
        }
        if (result > 0) {
            id = result;
            setJMS();
        }
        return result;
    }

    void logout() {
        try {
            if (id != -1) {
                userManager.logout(id);
                id = -1;
            }
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot logout " + e);
            System.exit(-1);
        }
    }

    void onStart(StartMessage m) {
        gameID = m.getGameID();
        System.out.println("Starting game!!!");
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
        System.out.println("Client sent!!");
    }

    void complete(String solution) {
        jms.sendMessage(new FinishedMessage(id, gameID, solution));
    }

    private void setJMS() {
        try {
            jms = new JMSClient(hostIP, port, id, this);
        } catch (NamingException | JMSException e) {
            System.err.println("[ERROR] Cannot setup JMS Client: " + e);
            System.exit(-1);
        }
    }

}

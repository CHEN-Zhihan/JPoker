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
    private User user;
    private JMSClient jms;
    private GamePanel g;
    private Game game;
    private int port;
    Client(String hostIP, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostIP);
            userManager = (UserManager)registry.lookup("userManager");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logout();
                } catch (RemoteException e) {
                    System.err.println("Failed logout: ");
                    System.exit(-1);
                }
            }));
        } catch (Exception e) {
            System.err.println("Failed accessing RMI: " + e);
            System.exit(-1);
        }
        this.hostIP = hostIP;
        this.port = port;
    }

    User getUser() {
        return user;
    }

    int getRank() throws RemoteException {
        return userManager.getRank(user.getUsername());
    }

    ArrayList<User> getAllUsers() throws RemoteException{
        return userManager.getAllUsers();
    }

    int login(String username, char[] password) throws RemoteException{
        password = PasswordManager.getInstance().encrypt(password);
        User u = userManager.login(username, password);
        password = null;
        int result = u.getValidFlag();
        if (result == UserManager.VALID) {
            user = u;
        }
        setJMS();
        return result;
    }

    int register(String username, char[] password) throws RemoteException {
        password = PasswordManager.getInstance().encrypt(password);
        int result = userManager.register(username, password);
        password = null;
        if (result > 0) {
            user = new User(result, username);
        }
        setJMS();
        return result;
    }

    void logout() throws RemoteException{
        if (user != null) {
            userManager.logout(user.getUsername());
            user = null;
        }
    }

    void onStart(StartMessage m) {
        game = m.getGame();
        g.start(game);
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
        jms.sendMessage(new RequestMessage(user));
    }

    void complete(String solution) {
        jms.sendMessage(new FinishedMessage(user, game.getID(), solution));
    }

    private void setJMS() {
        try {
            jms = new JMSClient(hostIP, port, user.getID(), this);
        } catch (NamingException | JMSException e) {
            System.err.println("[ERROR] Cannot setup JMS Client: " + e);
            System.exit(-1);
        }
    }

}

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Created by Zhihan CHEN on 2/9/17.
 * Client is responsible to communicate to server on the client side.
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
    private boolean terminated = false;

    /**
     * setup RMI and add logout shutdown hook.
     * @param hostIP
     * @param port
     */
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

    /**
     *
     * @return User object from server using local user ID.
     */
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

    /**
     *
     * @return user rank with local ID.
     */
    int getRank() {
        try {
            return userManager.getRank(id);
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get rank of " + id + " " + e);
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @return all Users registered.
     */
    ArrayList<User> getAllUsers() {
        try {
            return userManager.getAllUsers();
        } catch (RemoteException e) {
            System.err.println("[ERROR] Cannot get all users " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypt password and send to server. If result is valid, setup JMS.
     * @param username user input
     * @param password user input
     * @return login result generated by server.
     */
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
        return i;
    }

    /**
     * Encrype and send to server. If result is valid, setup JMS.
     * @param username user input
     * @param password user input
     * @return register result generated by server.
     */
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
        return result;
    }

    /**
     * inform server to logout. shutdown JMS.
     */
    void logout() {
    	try {
            if (id != -1) {
               userManager.logout(id);
               id = -1;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Cannot logout " + e);
        }
    	if (jms != null) {
    		jms.shutdown();
    	}
    }

    /**
     * Inform GamePanel to start game.
     * @param m StartMessage received from JMSServer.
     */
    void onStart(StartMessage m) {
        gameID = m.getGameID();
        g.start(m.getCards(), m.getUsers());
    }

    /**
     * Inform GamePanel to end game.
     * @param m EndMessage recevied from JMSServer
     */
    void onEnd(EndMessage m) {
        g.end(m.getWinner(), m.getSolution());
    }

    /**
     * call m's execute method.
     * @param m ServerMessage received from server.
     */
    void onMessage(ServerMessage m) {
        m.execute(this);
    }

    /**
     * set GamePanel as observer.
     * @param g GamePanel.
     */
    void setObserver(GamePanel g) {
        this.g = g;
    }

    /**
     * send request game message to JMSServer.
     */
    void request() {
        jms.sendMessage(new RequestMessage(id));
        System.out.println("[INFO] Sent game request");
    }

    /**
     * send complete game message to JMSServer
     * @param solution solution provided by player.
     */
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

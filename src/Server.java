import java.rmi.RemoteException;

/**
 * Created by zhihan on 5/3/17.
 */
public class Server {
    private UserManager userManager;
    private GameManager gameManager;

    Server(int port) {
        try {
            DBManager db = new DBManager();
            userManager = db;
            gameManager = new GameManager(port, db);
        } catch (RemoteException e) {
            System.err.println("Error setting up userManagerImpl: " + e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }
}

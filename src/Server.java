import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by zhihan on 5/3/17.
 */
class Server {
    private GameManager gm;
    private DBManager db;
    private Server(int port) {
        try {
            gm = new GameManager(port);
            System.out.println("[INFO] GameManager Initialized");
            db = new DBManager(gm);
            gm.setInfoManager(db);
            System.out.println("[INFO] DBManager Initialized");
        } catch (RemoteException e) {
            System.err.println("Error setting up Server: " + e);
            System.exit(-1);
        }
    }

    private void shutdown() {
        gm.shutdown();
        db.shutdown();
    }

    public static void main(String[] args) {
        Server server = new Server(Integer.parseInt(args[0]));
        Scanner s = new Scanner(System.in);
        while (!"exit".equals(s.next()));
        server.shutdown();
        System.exit(0);
    }
}

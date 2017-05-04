import java.rmi.RemoteException;

/**
 * Created by zhihan on 5/3/17.
 */
class Server {

    private Server(int port) {
        try {
            GameManager gm = new GameManager(port);
            gm.setInfoManager(new DBManager(gm));
        } catch (RemoteException e) {
            System.err.println("Error setting up userManagerImpl: " + e);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }
}

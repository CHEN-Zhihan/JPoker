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
    private Game game;
    Client(String hostIP) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostIP);
            userManager = (UserManager)registry.lookup("server");
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
    }

    String getUsername() {
        return user.getUsername();
    }

    int getNumGames() {
        return user.getNumGames();
    }

    int getNumWins() {
        return user.getNumWins();
    }

    double getAverage() {
        return user.getAverageTime();
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
        return result;
    }

    int register(String username, char[] password) throws RemoteException {
        password = PasswordManager.getInstance().encrypt(password);
        int result = userManager.register(username, password);
        password = null;
        if (result == UserManager.VALID) {
            user = new User(username);
        }
        return result;
    }

    void logout() throws RemoteException{
        if (user != null) {
            userManager.logout(user.getUsername());
            user = null;
        }
    }

    Game startGame() {
        game = new Game(null);
        return game;
    }

    void complete() {
        game.complete();
    }

}

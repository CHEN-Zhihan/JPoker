import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by zhihan on 2/9/17.
 */
public class Client {
    private String hostIP;
    private UserManager userManager;
    private User user;
    public Client(String hostIP) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostIP);
            userManager = (UserManager)registry.lookup("server");
        } catch (Exception e) {
            System.err.println("Failed accessming RMI: " + e);
            System.exit(-1);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> logout()));
    }

    public static void main(String[] args) {
        Client client = new Client(args[0]);
        client.login("abcd", "efg".toCharArray());
//        client.register("abcd", "efg".toCharArray());
//        client.logout();
//        client.login("abcd", "ef".toCharArray());
//        client.login("abcd", "efg".toCharArray());
//        client.login("abcd", "efg".toCharArray());
    }
    void login(String username, char[] password) {

        if (username.length() == 0 || password.length == 0) {
            System.err.println("Incomplete information");
            return;
        }
        try {
            User u = userManager.login(username, password);
            if (u instanceof IncorrectUser) {
                System.err.println("password incorrect");
            } else if (u instanceof NotExistUser) {
                System.err.println("User not exist");
            } else if (u instanceof LoggedInUser) {
                System.err.println("User logged in");
            } else {
                if (user != null) {
                    logout();
                }
                user = u;
                System.out.println("login successful");
            }
        } catch (RemoteException e) {
            System.err.println("Error invoking RMI: " + e);
        }
    }

    void register(String username, char[] password) {
        if (username.length() == 0 || password.length == 0) {
            System.err.println("Incomplete information");
            return;
        }
        try {
            User u = userManager.register(new String(username), password);
            if (u instanceof ExistUser) {
                System.err.println("User exist");
            }
            user = u;
        } catch (RemoteException e) {
            System.err.println("Error register: " + e);
        }
    }

    void logout() {
        if (user != null) {
            try {
                userManager.logout(user.getUsername());
                System.out.println("Log out successful");
                user = null;
            } catch (RemoteException e) {
                System.err.println("Error logout: " + e);
            }
        } else {
            System.out.println("Bye");
        }
    }
}

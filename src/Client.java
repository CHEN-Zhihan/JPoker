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
    public static final int LOGIN_SUCCESS = 1;
    public static final int INCORRECT_PASSWORD = 2;
    public static final int USER_NOT_EXIST = 3;
    public static final int USER_LOGGED_IN = 4;
    public static final int USER_EXIST = 1;
    public static final int REGISTER_SUCCESS = 2;
    public static final int LOGOUT_SUCCESS = 1;
    public static final int LOGOUT_FAILURE = 2;
    public Client(String hostIP) {
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
            System.err.println("Failed accessming RMI: " + e);
            System.exit(-1);
        }
    }

    int login(String username, char[] password) throws RemoteException{
        password = PasswordManager.getInstance().encrypt(password);
        User u = userManager.login(username, password);
        password = null;
        int result = 0;
        if (u instanceof CorrectUser) {
            user = u;
            result = LOGIN_SUCCESS;
        } else if (u instanceof IncorrectUser) {
            result = INCORRECT_PASSWORD;
        } else if (u instanceof LoggedInUser) {
            result = USER_LOGGED_IN;
        } else if (u instanceof NotExistUser) {
            result = USER_NOT_EXIST;
        }
        return result;
    }

    int register(String username, char[] password) throws RemoteException{
        password = PasswordManager.getInstance().encrypt(password);
        User u = userManager.register(username, password);
        password = null;
        int result = 0;
        if (u instanceof CorrectUser) {
            user = u;
            result = REGISTER_SUCCESS;
        } else if (u instanceof ExistUser) {
            result = USER_EXIST;
        }
        return result;
    }

    void logout() throws RemoteException{
        userManager.logout(user.getUsername());
        user = null;
    }
}

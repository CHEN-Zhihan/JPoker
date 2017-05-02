import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by zhihan on 2/6/17.
 */
public interface UserManager extends Remote {
    int USER_HAS_LOGGEDIN = -1;
    int USER_NOT_EXIST = -2;
    int USER_INCORRECT_PASSWORD = -3;
    int VALID = 4;
    int HAS_REGISTERED = -4;
    int DATABASE_ERROR = -1;
    User login(String username, char[] password) throws RemoteException;
    int register(String username, char[] password) throws RemoteException;
    void logout(String username) throws RemoteException;
    int getRank(String username) throws RemoteException;
    ArrayList<User> getAllUsers() throws RemoteException;
}

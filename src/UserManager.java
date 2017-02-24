import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by zhihan on 2/6/17.
 */
public interface UserManager extends Remote {
    public User login(String username, char[] password) throws RemoteException;
    public User register(String username, char[] password) throws RemoteException;
    public void logout(String username) throws RemoteException;
    public int getRank(String username) throws RemoteException;
}

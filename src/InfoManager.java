import java.util.HashMap;

/**
 * Created by zhihan on 2/7/17.
 */
public interface InfoManager {
    public void update(String username, User user);
    public User register(String username, char[] password);
    public User getUser(String username, char[] password);
    public HashMap<String, User> getUsers();
    public boolean exists(String username);
}

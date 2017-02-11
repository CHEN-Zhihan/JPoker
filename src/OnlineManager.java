import java.util.HashSet;

/**
 * Created by zhihan on 2/7/17.
 */
public interface OnlineManager {
    public void remove(String username);
    public void add(String username);
    public boolean isOnline(String username);
    public HashSet<String> getUsers();
}

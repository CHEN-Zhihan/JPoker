import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by zhihan on 2/9/17.
 */
public class ServerUI {
    private Server server;

    ServerUI() {
        try {
            server = new Server();
            System.setSecurityManager(new SecurityManager());
            Naming.rebind("server", server);
        } catch (Exception e) {
            System.err.println("Error setting up RMI: " + e);
            System.exit(-1);
        }
    }

    private void showOnlineUsers() {
        System.out.println("show online users: ");
        for (User u : server.getOnlineUsers()) {
            System.out.println(u.getUsername());
        }
    }

    private void showAllUsers() {
        System.out.println("show all users: ");
        for (User u : server.getAllUsers()) {
            System.out.println(u.getUsername());
        }
    }

    public static void main(String[] args) {
        ServerUI ui = new ServerUI();
        Scanner s = new Scanner(System.in);
        String x;
        while (!(x = s.nextLine()).equals("exit")) {
            if (x.equals("all")) {
                ui.showAllUsers();
            }
            if (x.equals("online")) {
                ui.showOnlineUsers();
            }
        }
    }
}

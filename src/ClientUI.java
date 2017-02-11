import java.util.Scanner;

/**
 * Created by zhihan on 2/9/17.
 */
public class ClientUI {
    private Client client;
    ClientUI(String hostIP) {
        client = new Client(hostIP);
    }

    private void login(String username, char[] password) {
        client.login(username, password);
    }

    private void logout() {
        client.logout();
    }

    private void register(String username, char[] password) {
        client.register(username, password);
    }

    public static void main(String[] args) {
        ClientUI ui = new ClientUI(args[0]);
        if (args[1].equals("login")) {
            ui.login(args[2], args[3].toCharArray());
        } else if(args[1].equals("register")) {
            ui.register(args[2], args[3].toCharArray());
        }
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }
}

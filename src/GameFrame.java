import javax.swing.*;
import java.rmi.RemoteException;

/**
 * Created by zhihan on 2/24/17.
 */
public class GameFrame extends JFrame {
    private Client client;
    private JRootPane rootPane;
    private Game game;
    public GameFrame(Client client) {
        this.client = client;
        initializeAppearance();
    }

    private void initializeAppearance() {
        rootPane = this.getRootPane();
        this.setSize(500, 400);

    }

    private void logout() {
        try {
            client.logout();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(rootPane, "Logout Failed: " + e, "Logout Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

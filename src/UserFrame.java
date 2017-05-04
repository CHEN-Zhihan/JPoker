import javax.swing.*;

/**
 * Created by zhihan on 5/4/17.
 */
abstract class UserFrame extends JFrame {
    final Client client;
    boolean checkAndEnter(int result) {
        if (result != UserManager.VALID) {
            return false;
        }
        this.setVisible(false);
        this.dispose();
        new GameFrame(client);
        return true;
    }
    UserFrame(Client c) {
        client = c;
    }
}

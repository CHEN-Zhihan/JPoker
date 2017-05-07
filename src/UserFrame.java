import javax.swing.*;

/**
 * Created by zhihan on 5/4/17.
 */
abstract class UserFrame extends JFrame {
    final Client client;
    UserFrame(Client c) {
        client = c;
    }
    /**
     * check user validity. If valid, create GameFrame.
     * @param result
     * @return
     */
    boolean checkAndEnter(int result) {
        if (result <= 0) {
            return false;
        }
        this.setVisible(false);
        this.dispose();
        new GameFrame(client);
        return true;
    }
}

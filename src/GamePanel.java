import javax.swing.*;

/**
 *
 * Created by zhihan on 2/24/17.
 */
public class GamePanel extends JPanel {
    private Client client;

    public GamePanel (Client client) {
        this.client = client;
        initializeAppearance();
    }

    private void initializeAppearance() {
        this.add(new JLabel("game panel"));
    }
}

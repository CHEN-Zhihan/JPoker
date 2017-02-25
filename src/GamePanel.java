import javax.swing.*;

/**
 *
 * Created by zhihan on 2/24/17.
 */
public class GamePanel extends ObserverPanel {
    private Client client;

    public GamePanel (Client client) {
        this.client = client;
        initializeAppearance();
    }

    protected void initializeAppearance() {
        this.add(new JLabel("game panel"));
    }
}

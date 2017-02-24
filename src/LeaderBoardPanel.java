import javax.swing.*;

/**
 * Created by zhihan on 2/24/17.
 */
public class LeaderBoardPanel extends JPanel {
    private Client client;

    public LeaderBoardPanel(Client client) {
        this.client = client;
        initializeAppearance();
    }

    private void initializeAppearance() {
        this.add(new JLabel("Leader Board Panel"));
    }
}

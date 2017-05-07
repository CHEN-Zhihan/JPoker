import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by zhihan on 2/24/17.
 */
class GameFrame extends JFrame {
    private final Client client;
    private ObserverPanel panel;
    private final ProfilePanel profilePanel;
    private final LeaderBoardPanel leaderBoardPanel;
    private final GamePanel gamePanel;
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenuItem profile = new JMenuItem("User Profile");
    private final JMenuItem play = new JMenuItem("Play Game");
    private final JMenuItem leader = new JMenuItem("LeaderBoard");
    private final JMenuItem logout = new JMenuItem("Logout");
    GameFrame(Client client) {
        this.client = client;
        profilePanel = new ProfilePanel(client);
        leaderBoardPanel = new LeaderBoardPanel(client);
        gamePanel = new GamePanel(client, this);
        initializeAppearance();
    }

    private void initializeAppearance() {
        menuBar.add(profile);
        menuBar.add(play);
        menuBar.add(leader);
        menuBar.add(logout);
        this.setJMenuBar(menuBar);
        logout.addActionListener((ActionEvent e) -> logout());
        play.addActionListener((ActionEvent e) -> setPanel(gamePanel));
        profile.addActionListener((ActionEvent e) -> setPanel(profilePanel));
        leader.addActionListener((ActionEvent e) -> setPanel(leaderBoardPanel));
        this.setPanel(profilePanel);
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

    private void setPanel(ObserverPanel panel) {
        if (this.panel != panel) {
            if (this.panel != null) {
                this.remove(this.panel);
            }
            this.add(panel);
            this.panel = panel;
            this.panel.update();
            this.panel.repaint();
            this.repaint();
            this.revalidate();
        }
    }
    private void logout() {
        client.logout();
        this.setVisible(false);
        this.dispose();
    }

    /**
     * disable menuBar during a game.
     */
    void freeze() {
        menuBar.setEnabled(false);
        leader.setEnabled(false);
        profile.setEnabled(false);
        play.setEnabled(false);
        this.revalidate();
        this.repaint();
    }

    /**
     * enable menuBar after a game.
     */
    void defreeze() {
        menuBar.setEnabled(true);
        leader.setEnabled(true);
        profile.setEnabled(true);
        play.setEnabled(true);
        this.revalidate();
        this.repaint();
    }
}

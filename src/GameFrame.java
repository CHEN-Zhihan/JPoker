import javax.swing.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

/**
 * Created by zhihan on 2/24/17.
 */
class GameFrame extends JFrame {
    private Client client;
    private JRootPane rootPane;
    private ObserverPanel panel;
    private ProfilePanel profilePanel;
    private LeaderBoardPanel leaderBoardPanel;
    private GamePanel gamePanel;
    private JMenuBar menuBar = new JMenuBar();
    private JMenuItem profile = new JMenuItem("User Profile");
    private JMenuItem play = new JMenuItem("Play Game");
    private JMenuItem leader = new JMenuItem("LeaderBoard");
    private JMenuItem logout = new JMenuItem("Logout");
    GameFrame(Client client) {
        this.client = client;
        profilePanel = new ProfilePanel(client);
        leaderBoardPanel = new LeaderBoardPanel(client);
        gamePanel = new GamePanel(client, this);
        initializeAppearance();
    }

    private void initializeAppearance() {
        rootPane = this.getRootPane();
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

    void freeze() {
        menuBar.setEnabled(false);
        leader.setEnabled(false);
        profile.setEnabled(false);
        play.setEnabled(false);
        this.revalidate();
        this.repaint();
    }

    void defreeze() {
        menuBar.setEnabled(true);
        leader.setEnabled(true);
        profile.setEnabled(true);
        play.setEnabled(true);
        this.revalidate();
        this.repaint();
    }
}

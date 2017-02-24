import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

/**
 * Created by zhihan on 2/24/17.
 */
public class GameFrame extends JFrame {
    private Client client;
    private JRootPane rootPane;
    private JPanel panel;
    private ProfilePanel profilePanel;
    private LeaderBoardPanel leaderBoardPanel;
    private GamePanel gamePanel;
    public GameFrame(Client client) {
        this.client = client;
        profilePanel = new ProfilePanel(client);
        leaderBoardPanel = new LeaderBoardPanel(client);
        gamePanel = new GamePanel(client);
        initializeAppearance();
    }

    private void initializeAppearance() {
        rootPane = this.getRootPane();
        JMenuBar menuBar = new JMenuBar();
        JMenuItem profile = new JMenuItem("User Profile");
        JMenuItem play = new JMenuItem("Play Game");
        JMenuItem leader = new JMenuItem("LeaderBoard");
        JMenuItem logout = new JMenuItem("Logout");
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
        this.setVisible(true);
        this.setResizable(false);
    }

    private void setPanel(JPanel panel) {
        if (this.panel != panel) {
            if (this.panel != null) {
                this.remove(this.panel);
            }
            this.add(panel);
            this.panel = panel;
            this.panel.repaint();
            this.repaint();
            this.revalidate();
        }
    }
    private void logout() {
        try {
            client.logout();
            this.setVisible(false);
            this.dispose();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(rootPane, "Logout Failed: " + e, "Logout Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

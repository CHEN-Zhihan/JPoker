import javax.swing.*;

/**
 * Created by zhihan on 5/4/17.
 */
class PlayerPanel extends JPanel {
    private String username;
    private int numWins;
    private int numGames;
    private double average;
    PlayerPanel(User u) {
        username = u.getUsername();
        numGames = u.getNumGames();
        numWins = u.getNumWins();
        average = u.getAverageTime();
        initializeAppearance();
    }

    private void initializeAppearance() {
        JLabel name = new JLabel("Player: " + username);
        JLabel rate = new JLabel("Wins: " + Integer.toString(numWins) + "/" + Integer.toString(numGames));
        JLabel avg = new JLabel(" Avg: " + ((average <0)?"N/A":String.format("%.2f s", average)));
        this.add(name);
        this.add(rate);
        this.add(avg);
    }
}

import sun.util.cldr.CLDRLocaleDataMetaInfo;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Created by zhihan on 2/24/17.
 */
public class ProfilePanel extends JPanel {
    private Client client;
    private String[] columnNames = {"Username", "Number of wins", "Number of games", "Average time to win", "Rank"};
    public ProfilePanel(Client client) {
        this.client = client;
        this.initializeAppearance();
    }

    private void initializeAppearance() {


        JLabel username = new JLabel(client.getUsername());
        JLabel wins = new JLabel("Number of wins: " + Integer.toString(client.getNumWins()));
        JLabel games = new JLabel("Number of games: " + Integer.toString(client.getNumGames()));
        JLabel average = new JLabel("Average time to win: " + Double.toString(client.getAverage()));
        int rank = -1;
        try {
            rank = client.getRank();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), "Failed to get rank: " + e, "Failed to get rank", JOptionPane.ERROR_MESSAGE);
        }
        Object[][] data = {{client.getUsername(), client.getNumWins(), client.getNumGames(), client.getAverage(), rank}};
        JTable table = new JTable(data, columnNames);
        this.add(table);
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by zhihan on 2/24/17.
 */
public class LeaderBoardPanel extends ObserverPanel {
    private Client client;
    private ArrayList<User> users;
    public LeaderBoardPanel(Client client) {
        this.client = client;
        initializeAppearance();
    }

    protected void initializeAppearance() {
        String[] columnNames = {"Rank", "Username", "Games won", "Games Played", "Average winning time"};
        try {
            users = client.getAllUsers();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this.getRootPane(), "Failed to retrieve leader board: " + e, "Failed to retrieve leader board", JOptionPane.ERROR_MESSAGE);
            users = new ArrayList<>();
        }
        Object[][] data = new Object[users.size()][5];
        for (int i = 0; i != users.size(); ++i) {
            data[i] = new Object[5];
            data[i][0] = i + 1;
            User u = users.get(i);
            data[i][1] = u.getUsername();
            data[i][2] = u.getNumWins();
            data[i][3] = u.getNumGames();
            data[i][4] = u.getAverageTime();
        }
        TableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int column, int row) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(5);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(56);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        for (int i = 0; i != columnNames.length; ++i) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        this.add(new JScrollPane(table));
    }

}

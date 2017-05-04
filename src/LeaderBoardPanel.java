import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zhihan on 2/24/17.
 */
public class LeaderBoardPanel extends ObserverPanel {
    private final Client client;
    private ArrayList<User> users;
    LeaderBoardPanel(Client client) {
        this.client = client;
        initializeAppearance();
    }

    protected void initializeAppearance() {
        String[] columnNames = {"Rank", "Username", "Games won", "Games Played", "Average winning time"};
        users = client.getAllUsers();
        Collections.sort(users);
        Object[][] data = new Object[users.size()][5];
        int[] ranks = getRanks();
        for (int i = 0; i != users.size(); ++i) {
            data[i] = new Object[5];
            data[i][0] = ranks[i];
            User u = users.get(i);
            data[i][1] = u.getUsername();
            data[i][2] = u.getNumWins();
            data[i][3] = u.getNumGames();
            data[i][4] = u.getAverageTime()>0?u.getAverageTime():"N/A";
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
    private int[] getRanks() {
        int[] ranks = new int[users.size()];
        int cumulation = 1;
        int index = 0;
        while (index < users.size() - 1) {
            int j = 0;
            while (index + j + 1 != users.size() && users.get(index + j).compareTo(users.get(index + j + 1)) == 0) {
                ++j;
            }
            ++j;
            for (int i = index; i != index + j; ++i) {
                ranks[i] = cumulation;
            }
            cumulation += j;
            index += j;
        }
        ranks[users.size() - 1] = (users.size() == 1) ? 1 :
                ((users.get(users.size() - 1).compareTo(users.get(users.size() - 2)) == 0) ? ranks[users.size() - 2] :
                        (ranks[users.size() - 2] + 1));
        return ranks;
    }
}

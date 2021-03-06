

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by zhihan on 2/24/17.
 */
public class ProfilePanel extends ObserverPanel {
    private final Client client;
    ProfilePanel(Client client) {
        this.client = client;
        this.initializeAppearance();
    }

    protected void initializeAppearance() {
        String[] columnNames = {"Rank", "Username", "Games won", "Games Played", "Average winning time(s)"};
        int rank = client.getRank();
        User u = client.getUser();
        Object[][] data = {{rank, u.getUsername(), u.getNumWins(), u.getNumGames(),
                u.getAverageTime()>0?String.format("%.2f", u.getAverageTime()):"N/A"}};
        TableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int column, int row) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(4);
        table.getColumnModel().getColumn(1).setPreferredWidth(40);
        table.getColumnModel().getColumn(2).setPreferredWidth(40);
        table.getColumnModel().getColumn(3).setPreferredWidth(56);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i != columnNames.length; ++i) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        this.add(new JScrollPane(table));
        this.setVisible(true);
    }
}

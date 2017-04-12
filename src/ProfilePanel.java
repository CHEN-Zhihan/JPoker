import sun.util.cldr.CLDRLocaleDataMetaInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Created by zhihan on 2/24/17.
 */
public class ProfilePanel extends ObserverPanel {
    private Client client;
    public ProfilePanel(Client client) {
        this.client = client;
        this.initializeAppearance();
    }

    protected void initializeAppearance() {
        String[] columnNames = {"Rank", "Username", "Games won", "Games Played", "Average winning time"};
        int rank = -1;
        try {
            rank = client.getRank();
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this.getRootPane(), "Failed to get rank: " + e, "Failed to get rank", JOptionPane.ERROR_MESSAGE);
        }
        Object[][] data = {{rank, client.getUsername(), client.getNumWins(), client.getNumGames(), client.getAverage()>0?client.getAverage():"N/A"}};
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
        table.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i != columnNames.length; ++i) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }
        this.add(new JScrollPane(table));
        this.setVisible(true);
    }
}

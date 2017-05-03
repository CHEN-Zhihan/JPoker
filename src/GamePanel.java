import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * Created by zhihan on 2/24/17.
 */
class GamePanel extends ObserverPanel {
    private Client client;
    private JButton start;
    private JTextField input = new JTextField();
    private JLabel result = new JLabel();
    private JLabel winner = new JLabel();
    private JLabel solution = new JLabel();
    private JLabel waiting = new JLabel();
    private ArrayList<Integer> cards;
    private ArrayList<User> users;
    private boolean completed = false;
    private GameFrame frame;
    GamePanel (Client client, GameFrame frame) {
        this.client = client;
        initializeAppearance();
        client.setObserver(this);
        this.frame = frame;

    }

    protected void initializeAppearance() {
        start = new JButton("New Game");
        this.setLayout(null);
        this.add(start);
        start.setBounds(150, 0, 150, 30);
        waiting.setText("Waiting for players");
        waiting.setBounds(150, 100, 150, 30);
        start.addActionListener((ActionEvent e) -> this.ready());
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public  void insertUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public  void removeUpdate(DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
   //             check();
            }
            private  void check() {
                int outcome = Calculator.calculate(input.getText(), cards);
                if (outcome == Calculator.ILLEGAL) {
                    result.setText("=");
                    return;
                }
                if (outcome == 24 && Calculator.allUsed()) {
                	if (!completed) {
                		client.complete(input.getText());
                		completed = true;
                	}
                    
                    return;
                }
                result.setText("=" + outcome);
            }
        });
    }

    void end(String winner, String solution) {
        this.removeAll();
        this.add(start);
        this.add(this.winner);
        this.add(this.solution);
        this.winner.setBounds(50, 50, 200, 100);
        this.solution.setBounds(50, 300, 200, 100);
        this.winner.setText(winner);
        this.solution.setText(solution);
        start.setEnabled(true);
        this.frame.defreeze();
        this.revalidate();
        this.repaint();
    }

    void ready() {
        client.request();
        start.setEnabled(false);
        this.frame.freeze();
        this.add(waiting);
        this.revalidate();
        this.repaint();
    }

    void start(ArrayList<Integer> cards, ArrayList<User> users) {
        completed = false;
        this.cards = cards;
        this.users = users;
        this.removeAll();
        this.add(start);
        int i = 0;
        for (Integer card : cards) {
            ImageIcon image = new ImageIcon("images/card_" + card + ".gif");
            JLabel label = new JLabel("", image, JLabel.CENTER);
            this.add(label);
            label.setBounds(80 * (i++) , 200, 80, 100);
        }
        this.add(input);
        this.add(result);
        input.setText("");
        input.setBounds(150, 300, 100, 30);
        result.setBounds(250, 300, 50, 30);
        this.revalidate();
        this.repaint();
    }
}

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * Created by zhihan on 2/24/17.
 */
class GamePanel extends ObserverPanel {
    private final Client client;
    private JButton start;
    private final JTextField input = new JTextField();
    private final JLabel result = new JLabel();
    private final JLabel winner = new JLabel();
    private final JLabel solution = new JLabel();
    private final JLabel waiting = new JLabel();
    private ArrayList<Integer> cards;
    private boolean completed = false;
    private final GameFrame frame;
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
        start.setBounds(150, 20, 150, 30);
        waiting.setText("Waiting for players");
        waiting.setBounds(150, 150, 150, 30);
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

    /**
     * show winner and his/her solution. Prepare for next game.
     * @param winner winner username
     * @param solution solution provided by winner.
     */
    void end(String winner, String solution) {
        this.removeAll();
        this.add(start);
        this.add(this.winner);
        this.add(this.solution);
        this.winner.setBounds(150, 50, 200, 30);
        this.solution.setBounds(150, 80, 200, 30);
        this.winner.setText("Winner: " + winner);
        this.solution.setText("Solution: " + solution);
        start.setEnabled(true);
        this.frame.defreeze();
        this.revalidate();
        this.repaint();
    }

    /**
     * send request to server. prepare appearance for game.
     */
    private void ready() {
        client.request();
        start.setEnabled(false);
        this.removeAll();
        this.frame.freeze();
        this.add(waiting);
        this.revalidate();
        this.repaint();
    }

    /**
     * Setup GUI for game.
     * @param cards 4 cards received from server.
     * @param users players in the game.
     */
    void start(ArrayList<Integer> cards, ArrayList<User> users) {
        completed = false;
        this.cards = cards;
        this.removeAll();
        for (int i = 0; i != cards.size(); ++i) {
        	JLabel label;
        	try{
        		URL url = getClass().getResource("images/card_" + cards.get(i) + ".gif").toURI().toURL();
        		label = new JLabel(new ImageIcon(url));
        	} catch (MalformedURLException | URISyntaxException e) {
        		System.err.println("[ERROR] Cannot read image");
        		label = new JLabel();
        	}
            this.add(label);
            label.setBounds(80 * i , 100, 80, 100);
        }
        for (int i = 0; i != users.size(); ++i) {
            PlayerPanel p = new PlayerPanel(users.get(i));
            this.add(p);
            p.setBounds(350, 80*i + 5, 120, 80);
        }
        this.add(input);
        this.add(result);
        input.setText("");
        input.setBounds(100, 250, 100, 30);
        result.setBounds(200, 250, 50, 30);
        this.revalidate();
        this.repaint();
    }
}

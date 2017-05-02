import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
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
    private Game game;
    private boolean completed = false;
    GamePanel (Client client) {
        this.client = client;
        initializeAppearance();
        client.setObserver(this);
    }

    protected void initializeAppearance() {
        start = new JButton("New Game");
        this.setLayout(null);
        this.add(start);
        start.setBounds(250, 0, 150, 30);
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
                int outcome = Calculator.calculate(input.getText(), game.getCards());
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


    void start(Game g) {
    	completed = false;
        game = g;
        prepareGameView(game);
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
    }

    void ready() {
        client.request();
        start.setEnabled(false);
    }

    private void prepareGameView(Game g) {
        this.removeAll();
        this.add(start);
        HashSet<Integer> cards = g.getCards();
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

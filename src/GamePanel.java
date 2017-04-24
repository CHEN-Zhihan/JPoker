import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * Created by zhihan on 2/24/17.
 */
class GamePanel extends ObserverPanel {
    private Client client;
    private JButton start;
    private JTextField input = new JTextField();
    private JLabel result = new JLabel();
    private JLabel target = new JLabel();
    private Game game;
    private JRootPane rootPane;
    private volatile boolean completed = false;
    GamePanel (Client client) {
        this.client = client;
        initializeAppearance();
        rootPane = this.getRootPane();
    }

    protected void initializeAppearance() {
        start = new JButton("New Game");
        this.setLayout(null);
        this.add(start);
        start.setBounds(250, 0, 150, 30);
        start.addActionListener((ActionEvent e) -> this.start());
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
                if (outcome == game.getTarget() && Calculator.allUsed()) {
                    client.complete();
                    complete();
                    return;
                }
                result.setText("=" + outcome);
            }
        });
    }


    private void complete() {
        if (!completed) {
            JOptionPane.showMessageDialog(rootPane, "Calculation Complete! Takes " +
                            new DecimalFormat("#.###").format(game.getDuration()) + " seconds total",
                    "Calculation Complete!", JOptionPane.INFORMATION_MESSAGE);
            this.removeAll();
            this.add(start);
            this.revalidate();
            this.repaint();
        }
        completed = true;
    }

    private void start() {
        completed = false;
        game = client.startGame();
        prepareGameView(game);
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
        target.setText("Target: " + g.getTarget());
        this.add(target);
        input.setBounds(150, 300, 100, 30);
        result.setBounds(250, 300, 50, 30);
        target.setBounds(350, 300, 100, 30);
        this.revalidate();
        this.repaint();
    }
}

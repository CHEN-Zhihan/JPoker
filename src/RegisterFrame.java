import javax.swing.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Created by zhihan on 2/24/17.
 */
public class RegisterFrame extends JFrame{
    private Client client;
    private JRootPane rootPane;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JTextField usernameField;
    public RegisterFrame(Client client) {
        this.client = client;
        this.initializeAppearance();
    }

    private void initializeAppearance() {
        rootPane = this.getRootPane();
        this.setSize(310, 200);
        this.setTitle("Register");
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        usernameField = new JTextField();
        JLabel passwordLabel1 = new JLabel("Password: ");
        JLabel passwordLabel2 = new JLabel("Confirm Password: ");
        JLabel usernameLabel = new JLabel("Username: ");
        JButton cancel = new JButton("cancel");
        JButton register = new JButton("register");
        register.addActionListener((ActionEvent e) -> {
            char[] password1 = passwordField1.getPassword();
            char[] password2 = passwordField2.getPassword();
            String username = usernameField.getText();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: Username cannot be empty", "Register Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password1.length == 0) {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: password cannot be empty", "Register Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password2.length == 0) {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: password confirm cannot be empty", "Register Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Arrays.equals(password1, password2)) {
                this.register(username, password1);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: password not match", "Register Failed", JOptionPane.ERROR_MESSAGE);
                passwordField1.setText("");
                passwordField2.setText("");
            }
        });
        cancel.addActionListener((ActionEvent e) ->{
            this.setVisible(false);
            this.dispose();
            new LoginFrame(client);
        });
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel1);
        this.add(passwordField1);
        this.add(passwordLabel2);
        this.add(passwordField2);
        this.add(register);
        this.add(cancel);
        this.setLayout(null);
        usernameLabel.setBounds(10, 5, 150, 30);
        passwordLabel1.setBounds(10, 50, 150, 30);
        passwordLabel2.setBounds(10, 95, 150, 30);
        usernameField.setBounds(150, 5, 150, 30);
        passwordField1.setBounds(150, 50, 150, 30);
        passwordField2.setBounds(150, 95, 150, 30);
        register.setBounds(10, 150, 100, 30);
        cancel.setBounds(200, 150, 100, 30);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void register(String username, char[] password) {
        try{
            int result = client.register(username, password);
            if (result == UserManager.VALID) {
                this.setVisible(false);
                this.dispose();
                new GameFrame(client);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: User exist","Register Failed", JOptionPane.ERROR_MESSAGE);
                passwordField1.setText("");
                passwordField2.setText("");
                usernameField.setText("");
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(rootPane, "Register Failed: " + e, "Register Failed", JOptionPane.ERROR_MESSAGE);
            passwordField1.setText("");
            passwordField2.setText("");
            usernameField.setText("");
        }
    }
}

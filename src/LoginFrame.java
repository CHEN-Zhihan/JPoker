import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

/**
 * Created by zhihan on 2/9/17.
 */
public class LoginFrame extends JFrame {
    private Client client;
    private JRootPane rootPane;
    private JPasswordField passwordField;
    private JTextField usernameField;
    LoginFrame(String hostIP) {
        this(new Client(hostIP));
    }

    LoginFrame(Client client) {
        this.client = client;
        initializeAppearance();
    }

    private void initializeAppearance() {
        rootPane = this.getRootPane();
        this.setSize(260, 150);
        this.setTitle("Login");
        passwordField = new JPasswordField();
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel usernameLabel = new JLabel("Username: ");
        JButton login = new JButton("login");
        JButton register = new JButton("register");
        login.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            char[] password = passwordField.getPassword();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Username cannot be empty", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length == 0) {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: password cannot be empty", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            login(username, password);
        });
        register.addActionListener((ActionEvent e) -> {
            this.setVisible(false);
            this.dispose();
            new RegisterFrame(client);
        });
        this.setLayout(null);
        this.add(passwordLabel);
        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordField);
        this.add(login);
        this.add(register);
        usernameLabel.setBounds(10, 5, 150, 30);
        passwordLabel.setBounds(10, 50, 150, 30);
        usernameField.setBounds(100, 5, 150, 30);
        passwordField.setBounds(100, 50, 150, 30);
        login.setBounds(10, 100, 90, 30);
        register.setBounds(150, 100, 100, 30);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void login(String username, char[] password) {
        try {
            int result = client.login(username, password);
            if (result == Client.LOGIN_SUCCESS) {
                this.setVisible(false);
                this.dispose();
                new GameFrame(client);
                return;
            } else if (result == Client.USER_NOT_EXIST) {

                JOptionPane.showMessageDialog(rootPane, "Login Failed: User Not Exist", "Login Failed", JOptionPane.ERROR_MESSAGE);
            } else if (result == Client.USER_LOGGED_IN) {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: User Logged In Already", "Login Failed", JOptionPane.ERROR_MESSAGE);
            } else if (result == Client.INCORRECT_PASSWORD) {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Incorrect Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(rootPane, "Login Failed: " + e, "Login Failed", JOptionPane.ERROR_MESSAGE);
        } finally {
            passwordField.setText("");
            usernameField.setText("");
        }
    }


    public static void main(String[] args) {
        LoginFrame ui = new LoginFrame(args[0]);
    }
}



import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by zhihan on 2/9/17.
 */
class LoginFrame extends UserFrame {
    private JRootPane rootPane;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private static String username;
    private LoginFrame(String hostIP, int port) {
        this(new Client(hostIP, port));
    }

    LoginFrame(Client client) {
        super(client);
        initializeAppearance();
        login(username, username.toCharArray());
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
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void login(String username, char[] password) {
        int result = client.login(username, password);
        if (checkAndEnter(result)) {
            return;
        }
        switch (result) {
            case UserManager.USER_NOT_EXIST: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: User Not Exist", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }case UserManager.DATABASE_ERROR: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Database Error", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }case UserManager.USER_HAS_LOGGEDIN: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: User Logged In Already", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }case UserManager.REMOTE_ERROR: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Remote Error", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }case UserManager.USER_INCORRECT_PASSWORD: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Incorrect Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }case PasswordManager.ENCRYPT_ERROR: {
                JOptionPane.showMessageDialog(rootPane, "Login Failed: Encrypt error", "Login Failed", JOptionPane.ERROR_MESSAGE);
                break;
            }
        }
        passwordField.setText("");
        usernameField.setText("");
    }

    public static void main(String[] args) {
        username = args[2];
        new LoginFrame(args[0], Integer.parseInt(args[1]));
    }
}

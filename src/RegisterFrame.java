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
        this.setSize(320, 250);
        this.setTitle("Register");
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        usernameField = new JTextField();
        JLabel passwordLabel1 = new JLabel("Password: ");
        JLabel passwordLabel2 = new JLabel("Password: ");
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
    }

    private void register(String username, char[] password) {
        try{
            int result = client.register(username, password);
            if (result == Client.REGISTER_SUCCESS) {
                this.setVisible(false);
                this.dispose();
                new GameFrame(client);
            } else if (result == Client.USER_EXIST) {
                JOptionPane.showMessageDialog(rootPane, "Register Failed: User exist","Register Failed", JOptionPane.ERROR_MESSAGE);

            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(rootPane, "Register Failed: " + e, "Register Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package gui;
import Model.StudentDatabase;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class LoginPanel extends JFrame {
    private JPanel Container;
    private JLabel username;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JLabel password;
    private JButton LoginBtn;


    public LoginPanel(StudentDatabase db) {
        setContentPane(Container);
        setTitle("Login");
        setMinimumSize(new java.awt.Dimension(300, 200));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                if(username.isEmpty() || password.isEmpty()){
                    //Incomplete Credentials
                    JOptionPane.showMessageDialog(Container, "Please Enter Username and Password!");
                    return;
                }
                if(isLoginValid(username, password)){
                    //Successful Login
                    setVisible(false);
                    new Dashboard(db, username);

                }
                else {
                    // Failed Login
                    JOptionPane.showMessageDialog(Container, "Login Credentials are Incorrect!\nPlease Try Again!!");
                }
            }
        });
    }

    public boolean isLoginValid(String username, String password){
        try(BufferedReader reader = new BufferedReader(new FileReader("src/gui/users.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].trim().equals(username) && parts[1].trim().equals(password)){
                    return true;
                }
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    private void createUIComponents() {
        usernameTextField = new JTextField();
        usernameTextField.setColumns(20);
    }
}
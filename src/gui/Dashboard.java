package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.StudentDatabase;

public class Dashboard extends JFrame{
    private JLabel Title;
    private JLabel welcomeMessage;
    private JButton AddBtn;
    private JButton viewStudentsButton;
    private JButton searchOrUpdateButton;
    private JButton deleteStudentButton;
    private JPanel Container;
    private JButton logOutButton;
    private JButton exitButton;

    public Dashboard(StudentDatabase db, String name){
        setContentPane(Container);
        setTitle("Student Management System");
        setMinimumSize(new java.awt.Dimension(300, 200));
        pack();
        welcomeMessage.setText("Welcome, " + name + "!");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        AddBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AddPanel(db, Dashboard.this).setVisible(true);
            }
        });
        viewStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new ViewPanel(db, Dashboard.this).setVisible(true);
            }
        });
        searchOrUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new SearchUpdatePanel(db, Dashboard.this).setVisible(true);
            }
        });
        deleteStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new DeletePanel(db, Dashboard.this).setVisible(true);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                JOptionPane.showMessageDialog(Container, "You have successfully logged out!");
                new LoginPanel(db).setVisible(true);
                dispose();
                System.gc();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(Container,
                        "Are you sure you want to exit?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    System.exit(0);
                }
            }
        });
    }
}

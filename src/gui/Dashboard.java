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

    private StudentDatabase db;

    public Dashboard(StudentDatabase db){

        this.db = db;

        setContentPane(Container);
        setTitle("Student Management System");
        setMinimumSize(new java.awt.Dimension(300, 200));
        pack();
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
                //Create object from view Panel and set it to true
            }
        });
        searchOrUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //Create object from Search & Update Panel and set it to true
            }
        });
        deleteStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //Create object from Delete Panel and set it to true
            }
        });

    }
}

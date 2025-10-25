package gui;

import Model.StudentDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeletePanel extends JFrame {
    private JPanel Container;
    private JTextField idDeleteField;
    private JButton confirmDeleteButton;
    private JButton goBackButton;
    private StudentDatabase db;
    private Dashboard dashboard;

    public DeletePanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("Delete Student");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(Container);
        setVisible(true);

        confirmDeleteButton.addActionListener(e -> {
            String id = idDeleteField.getText().trim();


            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(Container, "Please enter a Student ID!");
                return;
            }

            if(!db.validateID(id)){
                JOptionPane.showMessageDialog(Container, "Invalid ID!");
                return;
            }
            if (!db.containsID(id)) {
                JOptionPane.showMessageDialog(Container, "Student ID not found!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(Container,
                    "Are you sure you want to delete this student?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                db.deleteStudent(id);
                JOptionPane.showMessageDialog(Container, "Student deleted successfully!");
                idDeleteField.setText("");
            }
        });

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dashboard.setVisible(true);
                dispose();
                System.gc();
            }
        });
    }
}


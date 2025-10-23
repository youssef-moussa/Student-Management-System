package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;



public class AddPanel extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField departField;
    private JTextField gpaField;
    private JPanel Container;
    private JButton saveButton;
    private StudentDatabase db;
    private Dashboard dashboard;

    public AddPanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        // Window settings
        setTitle("Add Student");
        setContentPane(Container);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Save button action
        saveButton.addActionListener(e -> {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = genderField.getText().trim();
            String dept = departField.getText().trim();
            String gpa = gpaField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || age.isEmpty() || gender.isEmpty() || dept.isEmpty() || gpa.isEmpty()) {
                JOptionPane.showMessageDialog(Container, "Please fill in all fields!");
                return;
            }

            if (db.contains(id)) {
                JOptionPane.showMessageDialog(Container, "A student with this ID already exists!");
                return;
            }

            // Create and add student
            Student s = new Student(id, name, age, gender, dept, gpa);
            db.insertRecord(s);
            db.saveToFile();

            JOptionPane.showMessageDialog(Container, "Student added successfully!");

            // Clear fields
            idField.setText("");
            nameField.setText("");
            ageField.setText("");
            genderField.setText("");
            departField.setText("");
            gpaField.setText("");
        });

    }
}

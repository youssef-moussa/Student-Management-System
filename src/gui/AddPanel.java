package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;



public class AddPanel extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField departField;
    private JTextField gpaField;
    private JPanel Container;
    private JButton saveButton;
    private JRadioButton MaleBtn;
    private JRadioButton FemaleBtn;
    private JComboBox DepartmentList;

    public AddPanel(StudentDatabase db, Dashboard dashboard) {

        // Window settings
        setTitle("Add Student");
        setContentPane(Container);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(MaleBtn);
        genderGroup.add(FemaleBtn);
        DepartmentList.addItem("CCE");
        DepartmentList.addItem("MRE");
        DepartmentList.addItem("BME");
        DepartmentList.addItem("EME");
        DepartmentList.addItem("CAE");

// Optional: set default selection
        DepartmentList.setSelectedItem("Engineering");



        // Save button action
        saveButton.addActionListener(e -> {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = MaleBtn.isSelected() ? "Male" : "Female";
            String dept = (String) DepartmentList.getSelectedItem();
            String gpa = gpaField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || age.isEmpty() || gender.isEmpty() || dept.isEmpty() || gpa.isEmpty()) {
                JOptionPane.showMessageDialog(Container, "Please fill in all fields!");
                return;
            }

            if(!db.validateID(id)){
                JOptionPane.showMessageDialog(Container, "Invalid ID!");
                return;
            }
            if (db.contains(id)) {
                JOptionPane.showMessageDialog(Container, "A student with this ID already exists!");
                return;
            }

            if(!db.validateName(name)){
                JOptionPane.showMessageDialog(Container, "Invalid name!");
                return;
            }


            if(!db.validateAge(age)){
                JOptionPane.showMessageDialog(Container, "Invalid age!");
                return;
            }

            if(!db.validateGPA(gpa)){
                JOptionPane.showMessageDialog(Container, "Invalid GPA!");
                return;
            }


            // Create and add student
            Student s = new Student(id, name, age, gender, dept, gpa);
            db.insertRecord(s);
            db.SortByID();
            db.saveToFile();

            JOptionPane.showMessageDialog(Container, "Student added successfully!");

            // Clear fields
            idField.setText("");
            nameField.setText("");
            ageField.setText("");
            if (MaleBtn.isSelected())
                MaleBtn.setSelected(false);
            else
                FemaleBtn.setSelected(false);
            DepartmentList.setSelectedItem("CCE");
            gpaField.setText("");
        });

    }
}

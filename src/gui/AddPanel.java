package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
    private JButton goBackButton;

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


        // Save button action
        saveButton.addActionListener(e -> {

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = MaleBtn.isSelected() ? "Male" : "Female";
            String dept = (String) DepartmentList.getSelectedItem();
            String gpa = String.format("%.2f", Double.parseDouble(gpaField.getText().trim()));

            if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || dept.isEmpty() || gpa.isEmpty()) {
                JOptionPane.showMessageDialog(Container, "Please fill in Required fields!");
                return;
            }
            if(!id.isEmpty()){
            if(!db.validateID(id)){
                JOptionPane.showMessageDialog(Container, "Invalid ID!");
                return;
            }
            if (db.containsID(id)) {
                JOptionPane.showMessageDialog(Container, "A student with this ID already exists!");
                return;
            }}

            if(!db.validateName(name)){
                JOptionPane.showMessageDialog(Container, "Invalid name!");
                return;
            }


            if(!db.validateAge(age)){
                JOptionPane.showMessageDialog(Container, "Invalid age!");
                return;
            }

            if(!MaleBtn.isSelected()&&!FemaleBtn.isSelected()){
                JOptionPane.showMessageDialog(Container,"Please choose a gender!");
                return;
            }

            if(!db.validateGPA(gpa)){
                JOptionPane.showMessageDialog(Container, "Invalid GPA!");
                return;
            }

            // Create and add student
            db.AddStudent(id,name,age,gender,dept,gpa);

            JOptionPane.showMessageDialog(Container, "Student added successfully!");

            setVisible(false);
            dashboard.setVisible(true);
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

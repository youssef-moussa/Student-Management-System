package gui;

import Model.Student;
import Model.StudentDatabase;

import javax.swing.*;

public class UpdateStudentPanel extends JFrame{
    private JPanel container;
    private JPanel Container;
    private JTextField gpaField;
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JButton saveButton;
    private JRadioButton FemaleBtn;
    private JRadioButton MaleBtn;
    private JComboBox DepartmentList;
    private JButton goBackButton;


    public UpdateStudentPanel(Student s, StudentDatabase db,SearchUpdatePanel SearchUpdatePanel) {
        String oldID = s.getStudentID();
        setContentPane(Container);
        setTitle("Update Student");
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
                if (db.containsID(id) && !id.equals(oldID)) {
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

            if(!db.validateGPA(gpa)){
                JOptionPane.showMessageDialog(Container, "Invalid GPA!");
                return;
            }
            Student s1 = new Student(id, name, age, gender, dept, gpa);
            db.UpdateStudent(s1);
            JOptionPane.showMessageDialog(Container, "Student updated successfully!");
            setVisible(false);
            SearchUpdatePanel.setVisible(true);
            dispose();
            System.gc();
            SearchUpdatePanel.refreshContent();
        });
        goBackButton.addActionListener(e -> {
            setVisible(false);
            SearchUpdatePanel.setVisible(true);

            dispose();
        });

        gpaField.setText(String.format(s.getGPA()));
        idField.setText(String.valueOf(s.getStudentID()));
        nameField.setText(s.getFullName());
        ageField.setText(String.valueOf(s.getAge()));
        if (s.getGender().equals("Male")) {
            MaleBtn.setSelected(true);
        } else {
            FemaleBtn.setSelected(true);
        }
        DepartmentList.setSelectedItem(s.getDepartment());
    }
}
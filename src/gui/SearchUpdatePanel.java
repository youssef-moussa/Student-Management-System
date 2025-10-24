package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchUpdatePanel extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JRadioButton searchByIDRadio;
    private JRadioButton searchByNameRadio;
    private JButton searchButton;
    private JButton backButton;
    private JButton saveButton;

    private JTextField idField, nameField, ageField, genderField, deptField, gpaField;

    private StudentDatabase db;
    private Dashboard dashboard;

    public SearchUpdatePanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("Search & Update Students");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔍 Top Panel: Search Controls
        JPanel topPanel = new JPanel();
        searchField = new JTextField(15);
        searchByIDRadio = new JRadioButton("Search by ID", true);
        searchByNameRadio = new JRadioButton("Search by Name");
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(searchByIDRadio);
        searchGroup.add(searchByNameRadio);
        searchButton = new JButton("Search");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchByIDRadio);
        topPanel.add(searchByNameRadio);
        topPanel.add(searchButton);

        // 📋 Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Dept", "GPA"}, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // ✏️ Edit Form
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        idField = new JTextField(); idField.setEditable(false);
        nameField = new JTextField();
        ageField = new JTextField();
        genderField = new JTextField();
        deptField = new JTextField();
        gpaField = new JTextField();
        formPanel.add(new JLabel("ID")); formPanel.add(new JLabel("Name"));
        formPanel.add(new JLabel("Age")); formPanel.add(new JLabel("Gender"));
        formPanel.add(new JLabel("Dept")); formPanel.add(new JLabel("GPA"));
        formPanel.add(idField); formPanel.add(nameField);
        formPanel.add(ageField); formPanel.add(genderField);
        formPanel.add(deptField); formPanel.add(gpaField);

        // 🔘 Bottom Panel
        JPanel bottomPanel = new JPanel();
        saveButton = new JButton("Save Changes");
        backButton = new JButton("Back to Dashboard");
        bottomPanel.add(saveButton);
        bottomPanel.add(backButton);

        // 🧩 Add to Frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);

        // 🔄 Load all students initially
        loadTableData(db.returnAllRecords());

        // 🔍 Search Action
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            ArrayList<Student> results = new ArrayList<>();
            if (searchByIDRadio.isSelected()) {
                try {
                    int id = Integer.parseInt(query);
                    Student s = db.searchByID(id);
                    if (s != null) results.add(s);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid numeric ID.");
                }
            } else {
                results = db.searchByName(query);
            }
            loadTableData(results);
        });

        // 🖱️ Table Row Selection → Fill Form
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                ageField.setText(tableModel.getValueAt(row, 2).toString());
                genderField.setText(tableModel.getValueAt(row, 3).toString());
                deptField.setText(tableModel.getValueAt(row, 4).toString());
                gpaField.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        // 💾 Save Edited Student
        saveButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String age = ageField.getText();
                String gender = genderField.getText();
                String dept = deptField.getText();
                String gpa = gpaField.getText();

                Student updated = new Student(id, name, age, gender, dept, gpa);
                db.UpdateStudent(updated);
                loadTableData(db.returnAllRecords());
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check all fields.");
            }
        });

        // 🔙 Back to Dashboard
        backButton.addActionListener(e -> {
            setVisible(false);
            dashboard.setVisible(true);
        });

        setVisible(true);
    }

    private void loadTableData(ArrayList<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentID(),
                    s.getFullName(),
                    s.getAge(),
                    s.getGender(),
                    s.getDepartment(),
                    s.getGPA()
            });
        }
    }
}

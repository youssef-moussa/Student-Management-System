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
    private JButton cancelSearchButton;
    private JButton backButton;
    private JButton saveButton;

    private JTextField idField, nameField, ageField, genderField, deptField, gpaField;

    private StudentDatabase db;
    private Dashboard dashboard;

    private int selectedRow = -1;

    public SearchUpdatePanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("Search & Update Students");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel();
        searchField = new JTextField(15);
        searchByIDRadio = new JRadioButton("Search by ID", true);
        searchByNameRadio = new JRadioButton("Search by Name");
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(searchByIDRadio);
        searchGroup.add(searchByNameRadio);
        searchButton = new JButton("Search");
        cancelSearchButton = new JButton("Cancel Search");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchByIDRadio);
        topPanel.add(searchByNameRadio);
        topPanel.add(searchButton);
        topPanel.add(cancelSearchButton);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Dept", "GPA"}, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Form
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

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        saveButton = new JButton("Save Changes");
        backButton = new JButton("Back to Dashboard");
        bottomPanel.add(saveButton);
        bottomPanel.add(backButton);

        // Add to Frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);

        // Load all students
        loadTableData(db.returnAllRecords());

        // Search
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
                if (query.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter a search query.");
                    return;
                }
                results = db.searchByName(query);
            }
            loadTableData(results);
        });

        // Cancel Search
        cancelSearchButton.addActionListener(e -> {
            searchField.setText("");
            loadTableData(db.returnAllRecords());
            selectedRow = -1;
            studentTable.clearSelection();
        });

        // Table selection
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            selectedRow = studentTable.getSelectedRow();
        });

        // Save
        saveButton.addActionListener(e -> {
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                ageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                genderField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                deptField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                gpaField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }

            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = genderField.getText().trim();
            String dept = deptField.getText().trim();
            String gpa = gpaField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || age.isEmpty() || gender.isEmpty() || dept.isEmpty() || gpa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (!db.validateID(id)) {
                JOptionPane.showMessageDialog(this, "Invalid ID. Must be numeric and 4 digits.");
                return;
            }
            if (!db.validateName(name)) {
                JOptionPane.showMessageDialog(this, "Invalid name. Use alphabetic characters only.");
                return;
            }
            if (!db.validateAge(age)) {
                JOptionPane.showMessageDialog(this, "Invalid age. Must be between 10 and 100.");
                return;
            }
            if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                JOptionPane.showMessageDialog(this, "Gender must be 'Male' or 'Female'.");
                return;
            }
            if (!db.validateDepartment(dept)) {
                JOptionPane.showMessageDialog(this, "Invalid department. Must be CCE, MRE, BME, EME, or CAE.");
                return;
            }

            float gpaValue;
            try {
                gpaValue = Float.parseFloat(gpa);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "GPA must be a valid number.");
                return;
            }

            if (!db.validateGPA(String.valueOf(gpaValue))) {
                JOptionPane.showMessageDialog(this, "Invalid GPA. Must be between 0.0 and 4.0.");
                return;
            }

            try {
                gender = formatInput(gender);
                String formattedGPA = String.format("%.1f", gpaValue);
                Student updated = new Student(id, name, age, gender, dept.toUpperCase(), formattedGPA);
                db.UpdateStudent(updated);

                loadTableData(db.returnAllRecords());

                if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                    studentTable.setRowSelectionInterval(selectedRow, selectedRow);
                }

                JOptionPane.showMessageDialog(this, "Student updated successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unexpected error. Please check all fields.");
            }
        });

        // Back
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

    public static String formatInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
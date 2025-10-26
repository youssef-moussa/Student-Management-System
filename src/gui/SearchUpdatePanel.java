package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SearchUpdatePanel extends JFrame {
    public JTable studentTable;
    public DefaultTableModel tableModel;
    private JTextField searchField;
    private JRadioButton searchByIDRadio;
    private JRadioButton searchByNameRadio;
    private JButton searchButton;
    private JButton cancelSearchButton;
    private JButton backButton;

    private JTextField idField, nameField, ageField, genderField, deptField, gpaField;

    private StudentDatabase db;
    private Dashboard dashboard;

    public SearchUpdatePanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("Search Students");
        setSize(900, 600);
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

        // Table with Edit Button
        tableModel = new DefaultTableModel(new Object[]{"Edit", "ID", "Name", "Age", "Gender", "Dept", "GPA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only the Edit button is clickable
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        studentTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), this, db));
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Form (Uneditable)
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        idField = new JTextField(); idField.setEditable(false);
        nameField = new JTextField(); nameField.setEditable(false);
        ageField = new JTextField(); ageField.setEditable(false);
        genderField = new JTextField(); genderField.setEditable(false);
        deptField = new JTextField(); deptField.setEditable(false);
        gpaField = new JTextField(); gpaField.setEditable(false);

        formPanel.add(new JLabel("ID")); formPanel.add(new JLabel("Name"));
        formPanel.add(new JLabel("Age")); formPanel.add(new JLabel("Gender"));
        formPanel.add(new JLabel("Dept")); formPanel.add(new JLabel("GPA"));
        formPanel.add(idField); formPanel.add(nameField);
        formPanel.add(ageField); formPanel.add(genderField);
        formPanel.add(deptField); formPanel.add(gpaField);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        backButton = new JButton("Back to Dashboard");
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
            studentTable.clearSelection();
            clearFormFields();
        });

        // Back
        backButton.addActionListener(e -> {
            setVisible(false);
            dashboard.setVisible(true);
        });

        setVisible(true);
    }

    public void loadTableData(ArrayList<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    "Edit",
                    s.getStudentID(),
                    s.getFullName(),
                    s.getAge(),
                    s.getGender(),
                    s.getDepartment(),
                    s.getGPA()
            });
        }
    }

    private void clearFormFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderField.setText("");
        deptField.setText("");
        gpaField.setText("");
    }

    public static String formatInput(String input) {
        if (input == null || input.isEmpty()) return input;
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    // Inside the panel class you want to refresh
    public void refreshContent() {
        loadTableData(db.returnAllRecords());
        repaint();
        revalidate();
    }
}
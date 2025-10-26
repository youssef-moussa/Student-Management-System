package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ViewPanel extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton sortByIDButton;
    private JButton sortByNameButton;
    private JButton sortByGPAButton;
    private JButton backButton;
    private StudentDatabase db;
    private Dashboard dashboard;
    private JTextField gpaFromField;
    private JTextField gpaToField;
    private JButton filterButton;
    private JButton clearFilterButton;

    private boolean ascendingID = true;
    private boolean ascendingName = true;
    private boolean ascendingGPA = true;




    public ViewPanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("View Students");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Top panel for sorting buttons
        JPanel topPanel = new JPanel();
        sortByIDButton = new JButton("Sort by ID ↑");
        sortByNameButton = new JButton("Sort by Name ↑");
        sortByGPAButton = new JButton("Sort by GPA ↑");
        topPanel.add(sortByIDButton);
        topPanel.add(sortByNameButton);
        topPanel.add(sortByGPAButton);

         //GPA Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createTitledBorder("GPA Filter"));
        filterPanel.add(new JLabel("From:"));
        gpaFromField = new JTextField(4);
        filterPanel.add(gpaFromField);
        filterPanel.add(new JLabel("To:"));
        gpaToField = new JTextField(4);
        filterPanel.add(gpaToField);
        filterButton = new JButton("Filter");
        filterPanel.add(filterButton);
        clearFilterButton = new JButton("Clear");
        filterPanel.add(clearFilterButton);

        topPanel.add(filterPanel); // Add filter panel to top panel

        // Table model and JTable
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Age", "Gender", "Dept", "GPA"});
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Back button
        backButton = new JButton("Go Back");

        // Bottom panel with FlowLayout to size button to text
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.add(backButton);

        // Layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial student data
        loadTableData(db.returnAllRecords());

        sortByIDButton.addActionListener(e -> {
            db.SortByID(ascendingID);
            ascendingID = !ascendingID;
            loadTableData(db.returnAllRecords());
            sortByIDButton.setText(ascendingID ? "Sort by ID ↑" : "Sort by ID ↓");
        });

        sortByNameButton.addActionListener(e -> {
            db.SortByName(ascendingName);
            ascendingName = !ascendingName;
            loadTableData(db.returnAllRecords());
            sortByNameButton.setText(ascendingName ? "Sort by Name ↑" : "Sort by Name ↓");
        });

        sortByGPAButton.addActionListener(e -> {
            db.SortByGPA(ascendingGPA);
            ascendingGPA = !ascendingGPA;
            loadTableData(db.returnAllRecords());
            sortByGPAButton.setText(ascendingGPA ? "Sort by GPA ↑" : "Sort by GPA ↓");
        });

        //Filter button action
        filterButton.addActionListener(e -> {
            applyGPAFilter();
        });

        //Clear filter button action
        clearFilterButton.addActionListener(e -> {
            clearGPAFilter();
        });

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

    //GPA Filter method
    private void applyGPAFilter() {
        String fromText = gpaFromField.getText().trim();
        String toText = gpaToField.getText().trim();

        double fromGPA = 0.0;
        double toGPA = 4.0;

        // Parse "From" value
        if (!fromText.isEmpty()) {
            try {
                fromGPA = Double.parseDouble(fromText);
                if (fromGPA < 0.0 || fromGPA > 4.0) {
                    JOptionPane.showMessageDialog(this, "From GPA must be between 0.0 and 4.0");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid From GPA value. Please enter a valid number.");
                return;
            }
        }

        // Parse "To" value
        if (!toText.isEmpty()) {
            try {
                toGPA = Double.parseDouble(toText);
                if (toGPA < 0.0 || toGPA > 4.0) {
                    JOptionPane.showMessageDialog(this, "To GPA must be between 0.0 and 4.0");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid To GPA value. Please enter a valid number.");
                return;
            }
        }

        // Validate range
        if (fromGPA > toGPA) {
            JOptionPane.showMessageDialog(this, "From GPA cannot be greater than To GPA");
            return;
        }

        // Filter students based on GPA range
        ArrayList<Student> filteredStudents = new ArrayList<>();
        for (Student student : db.returnAllRecords()) {
                double studentGPA = Double.parseDouble(student.getGPA());
                if (studentGPA >= fromGPA && studentGPA <= toGPA) {
                    filteredStudents.add(student);
                }
        }
        loadTableData(filteredStudents);
    }

    //Clear filter method
    private void clearGPAFilter() {
        gpaFromField.setText("");
        gpaToField.setText("");
        loadTableData(db.returnAllRecords());
    }
}
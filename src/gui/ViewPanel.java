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
    private JButton backButton;
    private StudentDatabase db;
    private Dashboard dashboard;

    public ViewPanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("View Students");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Top panel for sorting buttons
        JPanel topPanel = new JPanel();
        sortByIDButton = new JButton("Sort by ID");
        sortByNameButton = new JButton("Sort by Name");
        topPanel.add(sortByIDButton);
        topPanel.add(sortByNameButton);

        // Table model and JTable
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Name", "Age", "Gender", "Dept", "GPA"});
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Back button
        backButton = new JButton("Back to Dashboard");

        // Layout
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Load initial student data
        loadTableData(db.returnAllRecords());


        sortByIDButton.addActionListener(e -> {
            db.SortByID();
            loadTableData(db.returnAllRecords());
        });

        sortByNameButton.addActionListener(e -> {
            db.SortByName();
            loadTableData(db.returnAllRecords());
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
}

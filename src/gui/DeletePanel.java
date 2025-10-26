package gui;

import Model.StudentDatabase;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeletePanel extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton sortByIDButton;
    private JButton sortByNameButton;
    private JButton backButton;
    private JButton cancelSearchButton;
    private JTextField searchField;
    private JRadioButton searchByIDRadio;
    private JRadioButton searchByNameRadio;
    private JButton searchButton;

    private StudentDatabase db;
    private Dashboard dashboard;

    public DeletePanel(StudentDatabase db, Dashboard dashboard) {
        this.db = db;
        this.dashboard = dashboard;

        setTitle("Delete Students");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel for Search
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

        // Middle Panel for Sorting
        JPanel middlePanel = new JPanel();
        sortByIDButton = new JButton("Sort by ID");
        sortByNameButton = new JButton("Sort by Name");
        middlePanel.add(sortByIDButton);
        middlePanel.add(sortByNameButton);

        // Table with Delete buttons
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Dept", "GPA", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the Action column (delete buttons) should be "editable" for clicking
                return column == 6;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(30);

        // Set up the delete button column
        TableColumn actionColumn = studentTable.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox(), db, this));

        actionColumn.setMinWidth(45);
        actionColumn.setMaxWidth(45);
        actionColumn.setPreferredWidth(45);


        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Bottom Panel for Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        backButton = new JButton("Back to Dashboard");
        bottomPanel.add(backButton);

        // Add all panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial student data
        loadTableData(db.returnAllRecords());

        // Search functionality
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
        });

        // Sorting functionality
        sortByIDButton.addActionListener(e -> {
            db.SortByID();
            loadTableData(db.returnAllRecords());
        });

        sortByNameButton.addActionListener(e -> {
            db.SortByName();
            loadTableData(db.returnAllRecords());
        });

        // Back button
        backButton.addActionListener(e -> {
            setVisible(false);
            dashboard.setVisible(true);
            dispose();
            System.gc();
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
                    s.getGPA(),
                    "Delete"
            });
        }
    }

    // Method to refresh the table after deletion
    public void refreshTable() {
        loadTableData(db.returnAllRecords());
    }

    // Button Renderer for the Action column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setForeground(Color.RED);
            setFont(new Font("Arial", Font.BOLD, 14));
            setText("X");

            return this;
        }
    }

    // Button Editor for the Action column
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private StudentDatabase database;
        private DeletePanel deletePanel;
        private JTable currentTable; // Store table reference

        public ButtonEditor(JCheckBox checkBox, StudentDatabase db, DeletePanel panel) {
            super(checkBox);
            this.database = db;
            this.deletePanel = panel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.currentTable = table; // Store the table reference

            button.setForeground(Color.RED);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setText("X");

            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed && currentTable != null) {
                // Get the student ID from the first column of the same row
                int modelRow = currentTable.convertRowIndexToModel(currentTable.getEditingRow());
                String studentId = currentTable.getModel().getValueAt(modelRow, 0).toString();

                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(deletePanel,
                        "Are you sure you want to delete student with ID: " + studentId + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    database.deleteStudent(studentId);
                    JOptionPane.showMessageDialog(deletePanel, "Student deleted successfully!");
                    deletePanel.refreshTable(); // Refresh the table after deletion
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
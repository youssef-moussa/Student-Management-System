package gui;

import Model.Student;
import Model.StudentDatabase;

import javax.swing.*;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean clicked;
    private JTable table;
    private SearchUpdatePanel parent;
    private StudentDatabase db;
    private Dashboard dashboard;
    public ButtonEditor(JCheckBox checkBox, SearchUpdatePanel parent, StudentDatabase db) {
        super(checkBox);
        this.parent = parent;
        this.db = db;
        this.button = new JButton("Edit");
        this.button.setOpaque(true);

        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked) {
            int modelRow = table.convertRowIndexToModel(table.getEditingRow());
            String id = parent.tableModel.getValueAt(modelRow, 1).toString(); // Column 1 is ID
            Student s = db.searchByID(Integer.parseInt(id));
            if (s != null) {
                new UpdateStudentPanel(s, db,this.parent);
                this.parent.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(button, "Student not found.");
            }
        }
        clicked = false;
        return "Edit";
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }
}
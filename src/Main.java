import Model.StudentDatabase;
import gui.LoginPanel;

public static void main(String[] args) {
    StudentDatabase db = new StudentDatabase();
    new LoginPanel(db).setVisible(true);
}
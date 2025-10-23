import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDatabase{
    protected ArrayList<Student> records;
    protected String filename;

    
    public void AddStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Student data: ");
        System.out.print("Student ID: ");
        String ID = scanner.nextLine();
        System.out.print("Student Name: ");
        String Name = scanner.nextLine();
        System.out.print("Student Age: ");
        String Age = scanner.nextLine();
        System.out.print("Student Gender: ");
        String Gender = scanner.nextLine();
        System.out.print("Student Department: ");
        String Department = scanner.nextLine();
        System.out.print("Student GPA: ");
        String GPA = scanner.nextLine();
        
        Student student = new Student(ID, Name, Age, Gender, Department, GPA);
        insertRecord(student);
        System.out.println("Student added successfully.");
        
    }
    
    
    
    public void readFromFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = createRecordFrom(line);
                if (student != null) {
                    records.add(student);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public Student createRecordFrom(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(), 
                                    parts[3].trim(), parts[4].trim(), parts[5].trim()); }
        } catch (Exception e) {
            System.out.println("An error occured in reading: " + line);
        }
        return null;
    }

    public ArrayList<Student> returnAllRecords(){
        return records;
    }

    public boolean contains(String key){
        for (Student student : records) {
            if (student.getStudentID().equals(key)) {
                return true;
            }
        }
        return false;
    }

     public Student getRecord(String key){
         for (Student student : records) {
             if (student.getStudentID().equals(key)) {
                 return student;
             }
         }
         return null;
     }

    public void insertRecord(Student record){
        if (record != null && !contains(record.getStudentID())) {
            records.add(record);
        } 
    }

     public void deleteRecord(String key){
         Student Remove = null;
         for (Student student : records) {
             if (student.getStudentID().equals(key)) {
                 Remove = student;
                 break;
             }
         }
         if (Remove != null) {
             records.remove(Remove);
         }
     }

    public void saveToFile(){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            for (Student student : records) {
                writer.println(student.lineRepresentation());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void viewStudents(){
        for (Student student : returnAllRecords()) {
            System.out.println(student.lineRepresentation());
    }
}
    public void SortbyName(){
        int n = records.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            Student s1 = records.get(j);
            Student s2 = records.get(j + 1);
            
            if (s1.getFullName().compareToIgnoreCase(s2.getFullName()) > 0) {

                Student temp = records.get(j);
                records.set(j, records.get(j + 1));
                records.set(j + 1, temp);
            }
        }
    }
    }
      public void SortbyID(){
        int n = records.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            Student s1 = records.get(j);
            Student s2 = records.get(j + 1);
            
            if (s1.getStudentID().compareToIgnoreCase(s2.getStudentID()) > 0) {

                Student temp = records.get(j);
                records.set(j, records.get(j + 1));
                records.set(j + 1, temp);
            }
        }
    }
    }

    public void UpdateStudent(){
    Scanner scanner = new Scanner(System.in);
    viewStudents();
    System.out.println("Enter the ID of the Student you would like to Update: ");
    String ID = scanner.nextLine();
    if(!contains(ID)){
        System.out.println("This Student doesn't exist");
        return;
    }
    int x;
    Student updatedStudent = getRecord(ID);
     while(true){
            System.out.print("\t\t~~~MENU~~~\n\t1.ID\n\t2.NAME\n\t3.Age\n\t4.Gender\n\t5.Department\n\t6.GPA\n\t0.EXIT.\n"+updatedStudent.lineRepresentation()+"\n~>Enter the number of information you would like to update: ");
            x = scanner.nextInt();
            scanner.nextLine();
            
            switch(x){
                case 1:
                System.out.println("Enter the new ID:");
                String newID = scanner.nextLine();
                updatedStudent.setStudentID(newID);
                    break;
                case 2:
                System.out.println("Enter the new Name: ");
                String newName = scanner.nextLine();
                updatedStudent.setFullName(newName);
                   
                    break;
                case 3:
                System.out.println("Enter the new Age: ");
                String newAge = scanner.nextLine();
                updatedStudent.setAge(newAge);
                    break;
                case 4:
                System.out.println("Enter the new Gender: ");
                String newGender = scanner.nextLine();
                updatedStudent.setGender(newGender);
                    break;
                case 5:
                System.out.println("Enter the new Departement: ");
                String newDep = scanner.nextLine();
                updatedStudent.setDepartment(newDep);
                    break;
                case 6:
                System.out.println("Enter the new GPA: ");
                String newGPA = scanner.nextLine();
                updatedStudent.setGPA(newGPA);
                    break;
                case 0:
                    System.out.println("Saving...");
                    deleteRecord(ID);
                    insertRecord(updatedStudent);
                    SortbyID();
                    saveToFile();
                    System.out.println("Saved Succefully");
                    return;
                default:
                    System.out.println("***Invalid Choice (0->6)***\n");
                    break;
            }
        }
    }

    public void deleteStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the Student you want to delete: ");
        String deleteID = scanner.nextLine();
        if(!contains(deleteID)){
            System.out.println("This Student doesn't exist");
            return;
        }
        System.out.println(getRecord(deleteID).lineRepresentation()+"\n Are you sure you want to delete this Student?(y/n)");
        String Action = scanner.nextLine();
        if (Action.equalsIgnoreCase("y")){
            deleteRecord(deleteID);
            System.out.println("Deleted Succefully!");
            saveToFile();
            return;
        }
        System.out.println("Deletion Aborted!");
        return;
    }
    
    public void SearchByID(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the Student to search for: ");
        String ID = scanner.nextLine();
        if(!contains(ID)){
            System.out.println("This Student doesn't Exist");
            return;
        }
        System.out.println(getRecord(ID).lineRepresentation());
        
    }

    public static void main(String[] args) {
        StudentDatabase db = new StudentDatabase();
        db.filename = "students.txt";
        db.records = new ArrayList<>();
        db.readFromFile();
        db.AddStudent();
        db.saveToFile();
        db.UpdateStudent();
        db.deleteStudent();
        
        }
        
    }


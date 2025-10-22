import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDatabase {
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
        scanner.close();
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

    // public Student getRecord(String key){
    //     for (Student student : records) {
    //         if (student.getSearchKey().equals(key)) {
    //             return student;
    //         }
    //     }
    //     return null;
    // }

    public void insertRecord(Student record){
        if (record != null && !contains(record.getStudentID())) {
            records.add(record);
        } 
    }

    // public void deleteRecord(String key){
    //     Student Remove = null;
    //     for (Student student : records) {
    //         if (student.getSearchKey().equals(key)) {
    //             Remove = student;
    //             break;
    //         }
    //     }
    //     if (Remove != null) {
    //         records.remove(Remove);
    //     }
    // }

    public void saveToFile(){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            for (Student student : records) {
                writer.println(student.lineRepresentation());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
    
    public void main(String[] args) {
        // Example usage
        StudentDatabase db = new StudentDatabase();
        db.filename = "students.txt";
        db.records = new ArrayList<>();
        db.readFromFile();
        db.AddStudent();
        db.saveToFile();
        // Print all records
        for (Student student : db.returnAllRecords()) {
            System.out.println(student.lineRepresentation());
        }
        
    }
}

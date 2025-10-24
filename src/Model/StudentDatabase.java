package Model;


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


    public void AddStudent(String ID, String Name, String Age,String Gender, String Department, String GPA){
        Student student = new Student(ID, Name, Age, Gender, Department, GPA);
        insertRecord(student);
        SortByID();
        saveToFile();
    }
    
    public StudentDatabase(){
        records = new ArrayList<>();
        filename = "src/Model/students.txt";
        readFromFile();
        SortByID();
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
            System.out.println("An error occurred in reading: " + line);
        }
        return null;
    }

    public ArrayList<Student> returnAllRecords(){
        return records;
    }

    public boolean contains(String key){
        for (Student student : records) {
            if (student.getStudentID().equals(key) || student.getFullName().equals(key)) {
                return true;
            }
        }
        return false;
    }

     public Student getRecord(String key){
         for (Student student : records) {
             if (student.getStudentID().equals(key)  || student.getFullName().equals(key)) {
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

    public void SortByName(){
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Student s1 = records.get(j);
                Student s2 = records.get(j + 1);

                String name1 = s1.getFullName().trim().toLowerCase();
                String name2 = s2.getFullName().trim().toLowerCase();

                if (name1.compareTo(name2) > 0) {
                    Student temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public void SortByID(){
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Student s1 = records.get(j);
                Student s2 = records.get(j + 1);

                int id1 = Integer.parseInt(s1.getStudentID());
                int id2 = Integer.parseInt(s2.getStudentID());

                if (id1 > id2) {
                    Student temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public void UpdateStudent(Student student) {
        if(!contains(student.getStudentID())){
            return;
        }
        deleteRecord(student.getStudentID());
        insertRecord(student);
        SortByID();
        saveToFile();
    }

    public void deleteStudent(String id){
        if(!contains(id)){
            return;
        }
            deleteRecord(id);
            SortByID();
            saveToFile();
    }
    
    public void SearchStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID or Name of the Student to search for: ");
        String data = scanner.nextLine();
        if(!contains(data)){
            System.out.println("This Student doesn't Exist");
            return;
        }
        System.out.println(getRecord(data).lineRepresentation());
    }


    public boolean validateID(String id) {
        try {
            int num = Integer.parseInt(id);
            return num >= 1000 && num <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validateName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    public boolean validateAge(String age) {
            if (age.matches("\\d{1,2}")) {
                int num = Integer.parseInt(age);
                return num >= 10 && num <= 100;
            }
            return false;
    }

    public boolean validateDepartment(String department) {
        return department.matches("[A-Za-z ]+") && department.length() == 3;
    }

    public boolean validateGPA(String gpa) {
        if (gpa == null || gpa.trim().isEmpty()) {
            return false;
        }
        try {
            double num = Double.parseDouble(gpa.trim());
            return num >= 0.0 && num <= 4.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Student searchByID(int id) {
        for (Student student : records) {
            if (student.getStudentID().equals(String.valueOf(id))) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> searchByName(String query) {
        ArrayList<Student> results = new ArrayList<>();
        for (Student student : records) {
            if (student.getFullName().toLowerCase().contains(query.toLowerCase())) {
                results.add(student);
            }
        }
        return results;
    }
}


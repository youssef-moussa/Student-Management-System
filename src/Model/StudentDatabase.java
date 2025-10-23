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

    
    public void AddStudent(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Student data: ");

        String ID = validateID(scanner);
        String Name = validateName(scanner);
        String Age = validateAge(scanner);
        String Gender = validateGender(scanner);
        String Department = validateDepartment(scanner);
        String GPA = validateGPA(scanner);
        
        Student student = new Student(ID, Name, Age, Gender, Department, GPA);
        insertRecord(student);
        SortbyID();
        saveToFile();
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

    public void SortbyName(){
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

    public void SortbyID(){
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

    public void UpdateStudent(){
        Scanner scanner = new Scanner(System.in);
        viewStudents();
        System.out.print("Enter the ID of the Student you would like to Update: ");
        String ID = scanner.nextLine();
        if(!contains(ID)){
            System.out.println("This Student doesn't exist");
            return;
        }
        int choice;
        Student updatedStudent = getRecord(ID);
        while(true){
            System.out.print("\t\t~~~MENU~~~\n\t1.ID\n\t2.NAME\n\t3.Age\n\t4.Gender\n\t5.Department\n\t6.GPA\n\t0.Save & Exit.\n"+updatedStudent.lineRepresentation()+"\n~>Choose from 0->6: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    String newID = validateID(scanner);
                    updatedStudent.setStudentID(newID);
                    break;
                case 2:
                    String newName = validateName(scanner);
                    updatedStudent.setFullName(newName);
                    break;
                case 3:
                    String newAge = validateAge(scanner);
                    updatedStudent.setAge(newAge);
                    break;
                case 4:
                    String newGender = validateGender(scanner);
                    updatedStudent.setGender(newGender);
                    break;
                case 5:
                    String newDep = validateDepartment(scanner);
                    updatedStudent.setDepartment(newDep);
                    break;
                case 6:
                    String newGPA = validateGPA(scanner);
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
        System.out.print("Enter the ID of the Student you want to delete: ");
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
            SortbyID();
            saveToFile();
            return;
        }
        System.out.println("Deletion Aborted!");
        return;
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


    private String validateID(Scanner scanner) {
        String id;
        while (true) {
            System.out.print("Student ID: ");
            id = scanner.nextLine().trim();
            if (id.matches("[A-Za-z0-9]+")) {
                if (!contains(id)) {
                    return id;
                } else {
                    System.out.println("This ID already exists.");
                }
            } else {
                System.out.println("Invalid ID. Only letters and numbers are allowed.");
            }
        }
    }

    private String validateName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Student Name: ");
            name = scanner.nextLine().trim();
            if (name.matches("[A-Za-z ]+")) {
                return name;
            } else {
                System.out.println("Invalid name. Use only letters and spaces.");
            }
        }
    }

    private String validateAge(Scanner scanner) {
        String age;
        while (true) {
            System.out.print("Student Age: ");
            age = scanner.nextLine().trim();
            if (age.matches("\\d{1,2}")) {
                int num = Integer.parseInt(age);
                if (num >= 10 && num <= 100) {
                    return age;
                }
            }
            System.out.println("Invalid age. Enter a number between 10 and 100.");
        }
    }

    private String validateGender(Scanner scanner) {
        String gender;
        while (true) {
            System.out.print("Student Gender (M/F): ");
            gender = scanner.nextLine().trim();
            if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F") ||
                    gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
                return gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
            } else {
                System.out.println("Invalid gender. Enter M/F or Male/Female.");
            }
        }
    }

    private String validateDepartment(Scanner scanner) {
        String department;
        while (true) {
            System.out.print("Student Department: ");
            department = scanner.nextLine().trim();
            if (department.matches("[A-Za-z ]+")) {
                return department;
            } else {
                System.out.println("Invalid department. Use letters and spaces only.");
            }
        }
    }

    private String validateGPA(Scanner scanner) {
        String gpa;
        while (true) {
            System.out.print("Student GPA: ");
            gpa = scanner.nextLine().trim();
            double num = Double.parseDouble(gpa);
            if (num >= 0.0 && num <= 4.0) {
                return String.format("%.2f", num);
            }
            System.out.println("Invalid GPA. Enter a number between 0.0 and 4.0.");
        }
    }


    public static void main(String[] args) {
        StudentDatabase db = new StudentDatabase();
        db.filename = "src/Model/students.txt";
        db.records = new ArrayList<>();
        db.readFromFile();
        //db.AddStudent();
        //db.UpdateStudent();
        //db.deleteStudent();
        db.viewStudents();
        //db.SearchStudent();
        }
    }


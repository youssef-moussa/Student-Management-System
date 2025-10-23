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

//    public void UpdateStudent(){
//        Scanner scanner = new Scanner(System.in);
//        viewStudents();
//        System.out.print("Enter the ID of the Student you would like to Update: ");
//        String ID = scanner.nextLine();
//        if(!contains(ID)){
//            System.out.println("This Student doesn't exist");
//            return;
//        }
//        int choice;
//        Student updatedStudent = getRecord(ID);
//        while(true){
//            System.out.print("\t\t~~~MENU~~~\n\t1.ID\n\t2.NAME\n\t3.Age\n\t4.Gender\n\t5.Department\n\t6.GPA\n\t0.Save & Exit.\n"+updatedStudent.lineRepresentation()+"\n~>Choose from 0->6: ");
//            choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch(choice){
//                case 1:
//                    String newID = validateID(scanner);
//                    updatedStudent.setStudentID(newID);
//                    break;
//                case 2:
//                    String newName = validateName(scanner);
//                    updatedStudent.setFullName(newName);
//                    break;
//                case 3:
//                    String newAge = validateAge(scanner);
//                    updatedStudent.setAge(newAge);
//                    break;
//                case 4:
//                    String newGender = validateGender(scanner);
//                    updatedStudent.setGender(newGender);
//                    break;
//                case 5:
//                    String newDep = validateDepartment(scanner);
//                    updatedStudent.setDepartment(newDep);
//                    break;
//                case 6:
//                    String newGPA = validateGPA(scanner);
//                    updatedStudent.setGPA(newGPA);
//                    break;
//                case 0:
//                    System.out.println("Saving...");
//                    deleteRecord(ID);
//                    insertRecord(updatedStudent);
//                    SortByID();
//                    saveToFile();
//                    System.out.println("Saved Successfully");
//                    return;
//                default:
//                    System.out.println("***Invalid Choice (0->6)***\n");
//                    break;
//            }
//        }
//    }

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


    private boolean validateID(String id) {
        if (id.matches("[A-Za-z0-9]+")) {
            return !contains(id);
        }
        return false;
    }

    private boolean validateName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    private boolean validateAge(String age) {
            if (age.matches("\\d{1,2}")) {
                int num = Integer.parseInt(age);
                return num >= 10 && num <= 100;
            }
            return false;
    }

    private boolean validateDepartment(String department) {
        return department.matches("[A-Za-z ]+") && department.length() == 3;
    }

    private boolean validateGPA(String gpa) {
        double num = Double.parseDouble(gpa);
        return num >= 0.0 && num <= 4.0;
    }
}


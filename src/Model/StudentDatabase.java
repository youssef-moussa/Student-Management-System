package Model;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;


public class StudentDatabase {
    protected ArrayList<Student> records;
    protected String filename;


    public void AddStudent(String ID, String Name, String Age,String Gender, String Department, String GPA){
        String finalID = ID;
        if (ID == null || ID.isEmpty()) {
            finalID = RandomID();
        }

        Student student = new Student(finalID, Name, Age, Gender, Department, GPA);
        insertRecord(student);
        SortByID(true);
        saveToFile();
    }

    private String RandomID() {
        Random random = new Random();
        String newID;
        int attempts = 0;

        do {
            int number = 9000 + random.nextInt(1000);
            newID = String.valueOf(number);
            attempts++;

            if (attempts > 100) {
                throw new RuntimeException("Could not generate unique Student ID");
            }
        } while (containsID(newID));
        return newID;
    }
    
    public StudentDatabase(){
        records = new ArrayList<>();
        filename = "src/Model/students.txt";
        readFromFile();
        SortByID(true);
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

    public boolean containsID(String id){
        for (Student student : records) {
            if (student.getStudentID().equals(id)) {
                return true;
            }
        }
        return false;
    }


    public void insertRecord(Student record){
        if (record != null && !containsID(record.getStudentID())) {
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

    public void SortByName(boolean ascending){
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String name1 = records.get(j).getFullName().trim().toLowerCase();
                String name2 = records.get(j + 1).getFullName().trim().toLowerCase();

                boolean swap = ascending ? name1.compareTo(name2) > 0 : name1.compareTo(name2) < 0;

                if (swap) {
                    Student temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public void SortByID(boolean ascending){
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int id1 = Integer.parseInt(records.get(j).getStudentID());
                int id2 = Integer.parseInt(records.get(j + 1).getStudentID());

                boolean swap = ascending ? id1 > id2 : id1 < id2;

                if (swap) {
                    Student temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public void SortByGPA(boolean ascending){
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double n1 = Double.parseDouble(records.get(j).getGPA());
                double n2 = Double.parseDouble(records.get(j + 1).getGPA());

                boolean swap = ascending ? n1 > n2 : n1 < n2;

                if (swap) {
                    Student temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    public void UpdateStudent(Student student) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getStudentID().equals(student.getStudentID())) {
                records.set(i, student);
                SortByID(true);
                saveToFile();
                return;
            }
        }
    }

    public void deleteStudent(String id){
        if(!containsID(id)){
            return;
        }
        deleteRecord(id);
        SortByID(true);
        saveToFile();
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


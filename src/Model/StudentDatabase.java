package Model;

import java.io.*;
import java.util.ArrayList;

public class StudentDatabase {

    protected ArrayList<Student> records;
    protected String filename = "src/Model/students.txt";

    public StudentDatabase() {
        records = new ArrayList<>();
        readFromFile();
    }

    public void readFromFile() {
        records.clear();
        File file = new File(filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = createRecordFrom(line);
                if (s != null) records.add(s);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public Student createRecordFrom(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        parts[4].trim(), parts[5].trim());
            }
        } catch (Exception e) {
            System.out.println("Error reading line: " + line);
        }
        return null;
    }

    public void appendToFile(Student student) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) { // true = append
            writer.println(student.lineRepresentation());
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }

    public boolean contains(String key) {
        for (Student s : records) {
            if (s.getStudentID().equals(key)) return true;
        }
        return false;
    }

    public void insertRecord(Student s) {
        if (s != null && !contains(s.getStudentID())) {
            records.add(s);
        }
    }

    public void deleteRecord(String key) {
        Student toRemove = null;
        for (Student s : records) {
            if (s.getStudentID().equals(key)) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            records.remove(toRemove);
            saveToFile();
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Student s : records) {
                writer.println(s.lineRepresentation());
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}

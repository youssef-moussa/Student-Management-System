package Model;
public class Student extends Person{
    private String StudentID;
    private String Department;
    private String GPA;
    

    public Student(String studentID, String fullName, String age, String gender, String department, String GPA) {
        this.setFullName(fullName);
        this.setAge(age);
        this.setGender(gender);
        this.StudentID = studentID;
        this.Department = department;
        this.GPA = GPA;
    }
    
    public String lineRepresentation(){
        return this.StudentID+","+this.getFullName()+","+this.getAge()+","+this.getGender()+","+this.Department+","+this.GPA;     
    }
    
    
    
    
    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String gPA) {
        GPA = gPA;
    }  
}

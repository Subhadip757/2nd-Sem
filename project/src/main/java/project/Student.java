package project;

import org.bson.Document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

public class Student {
    private String studentId; // For string-based IDs
    private int id; // For numeric IDs
    private String name;
    private Course course;
    private int age;
    private String email;
    private String phoneNumber;
    private String address;
    private double gpa;
    private LocalDateTime registrationDate;
    private Map<String, Integer> marks;

    // Constructor for CRUD operations
    public Student(int id, String name, int age, String email, String phoneNumber, String address, Course course) {
        this(id, name, age, email, phoneNumber, address);
        this.course = course;
    }

    // Constructor for Marks and Course operations
    public Student(String studentId, String name, String email, String phone) {
        this.studentId = studentId;
        this.id = Integer.parseInt(studentId); // Convert string ID to numeric ID
        this.name = name;
        this.email = email;
        this.phoneNumber = phone;
    }

    // Constructor for CRUD operations
    public Student(Integer id, String name, Integer age, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationDate = LocalDateTime.now();
        this.gpa = 0.0;
        this.marks = new HashMap<>();
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getGPA() {
        return gpa;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Map<String, Integer> getMarks() {
        return marks;
    }

    // Setters
    public void setGPA(double gpa) {
        this.gpa = gpa;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setMarks(Map<String, Integer> marks) {
        this.marks = marks;
    }

    public void addMark(String subject, int mark) {
        if (this.marks == null) {
            this.marks = new HashMap<>();
        }
        this.marks.put(subject, mark);
    }

    public Integer getMark(String subject) {
        return this.marks != null ? this.marks.get(subject) : null;
    }

    // Validation methods
    public static boolean isValidAge(int age) {
        return age >= 16 && age <= 100;
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 10.0;
    }

    // Method to convert to MongoDB Document
    public Document toDocument() {
        return new Document()
                .append("studentId", studentId)
                .append("_id", id)
                .append("name", name)
                .append("course", course)
                .append("age", age)
                .append("email", email)
                .append("phoneNumber", phoneNumber)
                .append("address", address)
                .append("gpa", gpa)
                .append("registrationDate",
                        registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("marks", marks);
    }

    public static Student fromDocument(Document doc) {
        Student student = new Student(
                doc.getInteger("_id"),
                doc.getString("name"),
                doc.getInteger("age"),
                doc.getString("email"),
                doc.getString("phoneNumber"),
                doc.getString("address"),
                null); // Assuming course is not available in the document
        student.setGPA(doc.getDouble("gpa"));
        student.setRegistrationDate(LocalDateTime.parse(
                doc.getString("registrationDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        student.setMarks(doc.get("marks", Map.class));
        return student;
    }

    public Student(int id, String name, int age, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
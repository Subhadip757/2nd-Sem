package project;

import org.bson.Document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Student {
    private int id;
    private String name;
    private String course;
    private int age;
    private String email;
    private String phoneNumber;
    private String address;
    private double gpa;
    private LocalDateTime registrationDate;

    public Student(int id, String name, String course, int age, String email, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
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

    public void setGPA(double gpa) {
        this.gpa = gpa;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
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

    // Convert Student to MongoDB Document
    public Document toDocument() {
        return new Document()
                .append("_id", id)
                .append("name", name)
                .append("course", course)
                .append("age", age)
                .append("email", email.toLowerCase())
                .append("phoneNumber", phoneNumber)
                .append("address", address)
                .append("gpa", gpa)
                .append("registrationDate",
                        registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public static Student fromDocument(Document doc) {
        Student student = new Student(
                doc.getInteger("_id"),
                doc.getString("name"),
                doc.getString("course"),
                doc.getInteger("age"),
                doc.getString("email"),
                doc.getString("phoneNumber"),
                doc.getString("address"));
        student.setGPA(doc.getDouble("gpa"));
        student.setRegistrationDate(LocalDateTime.parse(
                doc.getString("registrationDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return student;
    }
}
package project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class StudentDataManager {
    private final MongoCollection<Document> studentsCollection;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StudentDataManager() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.studentsCollection = database.getCollection("students");
    }

    public void addStudent(Student student) {
        try {
            Document doc = student.toDocument();
            studentsCollection.insertOne(doc);
        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
            throw new RuntimeException("Failed to add student", e);
        }
    }

    public void updateStudent(Student student) {
        try {
            Bson filter = Filters.eq("_id", student.getId());
            Bson updates = Updates.combine(
                    Updates.set("name", student.getName()),
                    Updates.set("course", student.getCourse()),
                    Updates.set("age", student.getAge()),
                    Updates.set("email", student.getEmail()),
                    Updates.set("phoneNumber", student.getPhoneNumber()),
                    Updates.set("address", student.getAddress()),
                    Updates.set("gpa", student.getGPA()));

            studentsCollection.updateOne(filter, updates);
        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
            throw new RuntimeException("Failed to update student", e);
        }
    }

    public void deleteStudent(int id) {
        try {
            studentsCollection.deleteOne(Filters.eq("_id", id));
        } catch (Exception e) {
            System.err.println("Error deleting student: " + e.getMessage());
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            for (Document doc : studentsCollection.find()) {
                students.add(documentToStudent(doc));
            }
        } catch (Exception e) {
            System.err.println("Error getting all students: " + e.getMessage());
            throw new RuntimeException("Failed to get students", e);
        }
        return students;
    }

    public Student getStudentById(int id) {
        try {
            Document doc = studentsCollection.find(Filters.eq("_id", id)).first();
            return doc != null ? documentToStudent(doc) : null;
        } catch (Exception e) {
            System.err.println("Error getting student by ID: " + e.getMessage());
            throw new RuntimeException("Failed to get student by ID", e);
        }
    }

    public boolean isDuplicateId(int id) {
        try {
            return studentsCollection.countDocuments(Filters.eq("_id", id)) > 0;
        } catch (Exception e) {
            System.err.println("Error checking duplicate ID: " + e.getMessage());
            throw new RuntimeException("Failed to check duplicate ID", e);
        }
    }

    public boolean isDuplicateEmail(String email) {
        try {
            return studentsCollection.countDocuments(Filters.eq("email", email.toLowerCase())) > 0;
        } catch (Exception e) {
            System.err.println("Error checking duplicate email: " + e.getMessage());
            throw new RuntimeException("Failed to check duplicate email", e);
        }
    }

    public boolean isDuplicatePhone(String phone) {
        try {
            return studentsCollection.countDocuments(Filters.eq("phoneNumber", phone)) > 0;
        } catch (Exception e) {
            System.err.println("Error checking duplicate phone: " + e.getMessage());
            throw new RuntimeException("Failed to check duplicate phone", e);
        }
    }

    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        try {
            Bson filter = Filters.or(
                    Filters.regex("name", query, "i"),
                    Filters.regex("course", query, "i"),
                    Filters.regex("email", query, "i"));

            for (Document doc : studentsCollection.find(filter)) {
                results.add(documentToStudent(doc));
            }
        } catch (Exception e) {
            System.err.println("Error searching students: " + e.getMessage());
            throw new RuntimeException("Failed to search students", e);
        }
        return results;
    }

    private Student documentToStudent(Document doc) {
        try {
            Student student = new Student(
                    doc.getInteger("_id"),
                    doc.getString("name"),
                    doc.getString("course"),
                    doc.getInteger("age"),
                    doc.getString("email"),
                    doc.getString("phoneNumber"),
                    doc.getString("address"));

            student.setGPA(doc.getDouble("gpa"));

            String regDateStr = doc.getString("registrationDate");
            if (regDateStr != null) {
                student.setRegistrationDate(LocalDateTime.parse(regDateStr, dateFormatter));
            }

            return student;
        } catch (Exception e) {
            System.err.println("Error converting document to student: " + e.getMessage());
            throw new RuntimeException("Failed to convert document to student", e);
        }
    }
}
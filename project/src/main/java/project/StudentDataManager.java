package project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.*;

public class StudentDataManager {
    private MongoCollection<Document> studentsCollection;
    private MongoCollection<Document> coursesCollection;
    private MongoCollection<Document> studentCoursesCollection;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StudentDataManager() {
        MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
        this.studentsCollection = db.getCollection("students");
        this.coursesCollection = db.getCollection("courses");
        this.studentCoursesCollection = db.getCollection("student_courses");
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
            studentsCollection.find().forEach(doc -> {
                try {
                    // Try to get string ID first, then fall back to numeric ID
                    String studentId = doc.getString("studentId");
                    if (studentId == null) {
                        Integer numericId = doc.getInteger("_id");
                        if (numericId != null) {
                            studentId = String.valueOf(numericId);
                        }
                    }

                    String name = doc.getString("name");
                    String email = doc.getString("email");
                    String phone = doc.getString("phoneNumber");

                    if (studentId != null && name != null) {
                        Student student = new Student(
                                doc.getInteger("_id"),
                                doc.getString("name"),
                                doc.getInteger("age"),
                                doc.getString("email"),
                                doc.getString("phoneNumber"),
                                doc.getString("address"));

                        // Set additional fields if they exist
                        if (doc.getString("course") != null) {
                            student.setCourse(doc.getString("course"));
                        }
                        if (doc.getDouble("gpa") != null) {
                            student.setGPA(doc.getDouble("gpa"));
                        }

                        students.add(student);
                        System.out.println("Added student to list: " + studentId + " - " + name);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing student document: " + doc.toJson());
                    e.printStackTrace();
                }
            });
            System.out.println("Total students loaded: " + students.size());
            return students;
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching students: " + e.getMessage());
        }
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

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            coursesCollection.find().forEach(doc -> {
                Course course = new Course(
                        doc.getString("courseId"),
                        doc.getString("courseName"),
                        doc.getString("description"));
                courses.add(course);
                System.out.println("Found course: " + course.getCourseId() + " - " + course.getCourseName());
            });
            return courses;
        } catch (Exception e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            throw new RuntimeException("Error fetching courses: " + e.getMessage());
        }
    }

    public void saveStudentMarks(String studentId, String courseId, Map<String, Integer> marks) {
        try {
            MongoCollection<Document> marksCollection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("marks");

            // Create the marks document
            Document marksDoc = new Document()
                    .append("studentId", Integer.parseInt(studentId))
                    .append("courseId", courseId)
                    .append("marks", new Document(marks));

            // Use upsert to either update existing marks or insert new ones
            marksCollection.updateOne(
                    Filters.eq("studentId", Integer.parseInt(studentId)),
                    new Document("$set", marksDoc),
                    new UpdateOptions().upsert(true));
        } catch (Exception e) {
            throw new RuntimeException("Error saving marks: " + e.getMessage());
        }
    }

    public void addCourse(Course course) {
        try {
            // First check if MongoDB connection is established
            if (coursesCollection == null) {
                MongoDatabase db = MongoDBConnection.getInstance().getDatabase();
                coursesCollection = db.getCollection("courses");
            }

            // Check if course already exists
            Document existingCourse = coursesCollection.find(
                    new Document("courseId", course.getCourseId())).first();

            if (existingCourse != null) {
                throw new RuntimeException("Course with ID " + course.getCourseId() + " already exists");
            }

            // Create the course document
            Document courseDoc = new Document()
                    .append("courseId", course.getCourseId())
                    .append("courseName", course.getCourseName())
                    .append("description", course.getDescription())
                    .append("createdAt", new Date());

            // Insert the document and wait for acknowledgment
            coursesCollection.insertOne(courseDoc);

            // Verify the insertion
            Document verifyDoc = coursesCollection.find(new Document("courseId", course.getCourseId())).first();
            if (verifyDoc == null) {
                throw new RuntimeException("Failed to verify course insertion");
            }

            System.out.println("Course added successfully: " + course.getCourseId() + " - " + course.getCourseName());
        } catch (Exception e) {
            String errorMessage = "Error adding course: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace(); // Print the full stack trace for debugging
            throw new RuntimeException(errorMessage);
        }
    }

    public void assignCourseToStudent(String studentId, String courseId) {
        try {
            // Try to find student by both string ID and integer ID
            Document student = null;
            try {
                // First try with string ID
                student = studentsCollection.find(new Document("studentId", studentId)).first();

                // If not found, try with integer ID
                if (student == null) {
                    int numericId = Integer.parseInt(studentId);
                    student = studentsCollection.find(new Document("_id", numericId)).first();
                }
            } catch (NumberFormatException e) {
                // Handle case where ID can't be converted to integer
                student = studentsCollection.find(new Document("studentId", studentId)).first();
            }

            if (student == null) {
                throw new RuntimeException("Student with ID " + studentId + " not found");
            }

            // Verify course exists
            Document course = coursesCollection.find(new Document("courseId", courseId)).first();
            if (course == null) {
                throw new RuntimeException("Course not found");
            }

            // Update or insert the course assignment
            Document query = new Document("studentId", studentId);
            Document update = new Document("$set", new Document()
                    .append("courseId", courseId)
                    .append("updatedAt", new Date()));

            studentCoursesCollection.updateOne(
                    query,
                    update,
                    new com.mongodb.client.model.UpdateOptions().upsert(true));

            System.out.println("Course " + courseId + " assigned/updated for student " + studentId);
        } catch (Exception e) {
            System.err.println("Error assigning course: " + e.getMessage());
            throw new RuntimeException("Error assigning course: " + e.getMessage());
        }
    }

    public void deleteCourse(String courseId) {
        try {
            // Delete course from courses collection
            coursesCollection.deleteOne(new Document("courseId", courseId));

            // Delete all assignments for this course
            studentCoursesCollection.deleteMany(new Document("courseId", courseId));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting course: " + e.getMessage());
        }
    }

    public List<Course> getStudentCourses(String studentId) {
        List<Course> courses = new ArrayList<>();
        try {
            studentCoursesCollection.find(new Document("studentId", studentId))
                    .forEach(doc -> {
                        String courseId = doc.getString("courseId");
                        Document courseDoc = coursesCollection.find(
                                new Document("courseId", courseId)).first();

                        if (courseDoc != null) {
                            Course course = new Course(
                                    courseDoc.getString("courseId"),
                                    courseDoc.getString("courseName"),
                                    courseDoc.getString("description"));
                            courses.add(course);
                        }
                    });
            return courses;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching student courses: " + e.getMessage());
        }
    }

    public List<Student> getStudentsInCourse(String courseId) {
        List<Student> students = new ArrayList<>();
        try {
            // Find all student IDs assigned to this course
            studentCoursesCollection.find(new Document("courseId", courseId))
                    .forEach(doc -> {
                        String studentId = doc.getString("studentId");
                        Document studentDoc = studentsCollection.find(
                                new Document("studentId", studentId)).first();

                        if (studentDoc != null) {
                            Student student = new Student(
                                    studentDoc.getString("studentId"),
                                    studentDoc.getString("name"),
                                    studentDoc.getString("email"),
                                    studentDoc.getString("phone"));
                            students.add(student);
                        }
                    });
            return students;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching students in course: " + e.getMessage());
        }
    }

    public Student getStudentById(String studentId) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("students");
            Document doc = collection.find(new Document("studentId", studentId)).first();

            if (doc != null) {
                return new Student(
                        doc.getString("studentId"),
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("phone"));
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching student: " + e.getMessage());
        }
    }

    private Student documentToStudent(Document doc) {
        try {
            Student student = new Student(
                    doc.getInteger("_id"),
                    doc.getString("name"),
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

    public boolean updateStudentCourse(int studentId, String newCourse) {
        try {
            // Update the course in the students collection
            MongoCollection<Document> studentsCollection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("students");
            studentsCollection.updateOne(
                    Filters.eq("_id", studentId),
                    Updates.set("course", newCourse));

            // Update the course assignments in the courses collection
            MongoCollection<Document> coursesCollection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("courses");

            // Remove student from all courses
            coursesCollection.updateMany(
                    Filters.exists("students"),
                    Updates.pull("students", studentId));

            // Add student to new course
            coursesCollection.updateOne(
                    Filters.eq("courseId", newCourse),
                    Updates.addToSet("students", studentId));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getStudentMarks(int studentId) {
        try {
            MongoCollection<Document> marksCollection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("marks");
            Document marksDoc = marksCollection.find(Filters.eq("studentId", studentId)).first();

            if (marksDoc == null) {
                return new HashMap<>();
            }

            Map<String, Integer> marks = new HashMap<>();
            Document subjectMarks = marksDoc.get("marks", Document.class);
            if (subjectMarks != null) {
                for (String subject : Subject.CS_SUBJECTS) {
                    Integer mark = subjectMarks.getInteger(subject);
                    if (mark != null) {
                        marks.put(subject, mark);
                    }
                }
            }
            return marks;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void updateStudentGPA(int studentId, double gpa) {
        try {
            MongoCollection<Document> studentsCollection = MongoDBConnection.getInstance().getDatabase()
                    .getCollection("students");

            Document query = new Document("_id", studentId);
            Document update = new Document("$set", new Document("gpa", gpa));

            studentsCollection.updateOne(query, update);
        } catch (Exception e) {
            throw new RuntimeException("Error updating student GPA: " + e.getMessage());
        }
    }
}
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
            // First check if student ID already exists
            if (isDuplicateId(student.getId())) {
                throw new RuntimeException("Student ID " + student.getId() + " already exists");
            }

            // Create the student document with all required fields
            Document doc = new Document()
                    .append("_id", student.getId())
                    .append("name", student.getName())
                    .append("age", student.getAge())
                    .append("email", student.getEmail())
                    .append("phoneNumber", student.getPhoneNumber())
                    .append("address", student.getAddress())
                    .append("gpa", 0.0) 
                    .append("registrationDate", new Date());

            // Insert the document
            studentsCollection.insertOne(doc);

            // Verify the insertion
            Document verifyDoc = studentsCollection.find(new Document("_id", student.getId())).first();
            if (verifyDoc == null) {
                throw new RuntimeException("Failed to verify student insertion");
            }

            System.out.println("Student added successfully: " + student.getId() + " - " + student.getName());
        } catch (Exception e) {
            String errorMessage = "Error adding student: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            throw new RuntimeException(errorMessage);
        }
    }

    public void updateStudent(Student student) {
        try {
            // First verify the student exists
            Document existingStudent = studentsCollection.find(
                    new Document("_id", student.getId())).first();

            if (existingStudent == null) {
                throw new RuntimeException("Student not found with ID: " + student.getId());
            }

            // Create update document
            Document updateDoc = new Document()
                    .append("name", student.getName())
                    .append("age", student.getAge())
                    .append("email", student.getEmail())
                    .append("phoneNumber", student.getPhoneNumber())
                    .append("address", student.getAddress());

            // If course is being updated, handle course assignments
            if (student.getCourse() != null) {
                String newCourseId = student.getCourse().getCourseId();
                String oldCourseId = existingStudent.getString("course");

                // If course is changing, update course assignments
                if (oldCourseId != null && !oldCourseId.equals(newCourseId)) {
                    // Remove from old course
                    studentCoursesCollection.deleteOne(
                            new Document()
                                    .append("studentId", student.getId())
                                    .append("courseId", oldCourseId));
                }

                // Add to new course
                Document assignmentDoc = new Document()
                        .append("studentId", student.getId())
                        .append("courseId", newCourseId)
                        .append("assignedAt", new Date());

                studentCoursesCollection.updateOne(
                        new Document()
                                .append("studentId", student.getId())
                                .append("courseId", newCourseId),
                        new Document("$set", assignmentDoc),
                        new UpdateOptions().upsert(true));

                updateDoc.append("course", newCourseId);
            }

            // Update the student document
            studentsCollection.updateOne(
                    new Document("_id", student.getId()),
                    new Document("$set", updateDoc));

            // Verify the update
            Document verifyDoc = studentsCollection.find(
                    new Document("_id", student.getId())).first();
            if (verifyDoc == null) {
                throw new RuntimeException("Failed to verify student update");
            }

            System.out.println("Student updated successfully: " + student.getId());
        } catch (Exception e) {
            String errorMessage = "Error updating student: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            throw new RuntimeException(errorMessage);
        }
    }

    public void deleteStudent(int id) {
        try {
            // First verify the student exists
            Document studentDoc = studentsCollection.find(
                    new Document("_id", id)).first();

            if (studentDoc == null) {
                throw new RuntimeException("Student not found with ID: " + id);
            }

            // Get the student's current course
            String courseId = studentDoc.getString("course");

            // Delete student's course assignments
            if (courseId != null) {
                studentCoursesCollection.deleteMany(
                        new Document("studentId", id));
            }

            // Delete student's marks
            MongoCollection<Document> marksCollection = MongoDBConnection.getInstance()
                    .getDatabase().getCollection("marks");
            marksCollection.deleteMany(
                    new Document("studentId", id));

            // Finally, delete the student
            studentsCollection.deleteOne(
                    new Document("_id", id));

            // Verify the deletion
            Document verifyDoc = studentsCollection.find(
                    new Document("_id", id)).first();
            if (verifyDoc != null) {
                throw new RuntimeException("Failed to verify student deletion");
            }

            System.out.println("Student deleted successfully: " + id);
        } catch (Exception e) {
            String errorMessage = "Error deleting student: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            throw new RuntimeException(errorMessage);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            // Fetch all students with their current data
            studentsCollection.find().forEach(doc -> {
                try {
                    // Get basic student information
                    Integer id = doc.getInteger("_id");
                    String name = doc.getString("name");
                    Integer age = doc.getInteger("age");
                    String email = doc.getString("email");
                    String phone = doc.getString("phoneNumber");
                    String address = doc.getString("address");
                    Double gpa = doc.getDouble("gpa");
                    String courseId = doc.getString("course");

                    if (id != null && name != null) {
                        Student student = new Student(id, name, age, email, phone, address);

                        // Set GPA if available
                        if (gpa != null) {
                            student.setGPA(gpa);
                        }

                        // Set course if available
                        if (courseId != null) {
                            Course course = getCourseById(courseId);
                            if (course != null) {
                                student.setCourse(course);
                            }
                        }

                        // Get marks from marks collection
                        MongoCollection<Document> marksCollection = MongoDBConnection.getInstance()
                                .getDatabase().getCollection("marks");
                        Document marksDoc = marksCollection.find(
                                new Document("studentId", id)).first();

                        if (marksDoc != null) {
                            Document subjectMarks = marksDoc.get("marks", Document.class);
                            if (subjectMarks != null) {
                                Map<String, Integer> marks = new HashMap<>();
                                for (String subject : Subject.CS_SUBJECTS) {
                                    Integer mark = subjectMarks.getInteger(subject);
                                    if (mark != null) {
                                        marks.put(subject, mark);
                                    }
                                }
                                student.setMarks(marks);
                            }
                        }

                        students.add(student);
                        System.out.println("Loaded student: " + id + " - " + name +
                                (courseId != null ? " (Course: " + courseId + ")" : ""));
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing student document: " + doc.toJson());
                    e.printStackTrace();
                }
            });

            System.out.println("Total students loaded: " + students.size());
            return students;
        } catch (Exception e) {
            System.err.println("Database error while fetching students: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching students: " + e.getMessage());
        }
    }

    public Student getStudentById(int id) {
        try {
            Document doc = studentsCollection.find(new Document("_id", id)).first();
            if (doc != null) {
                Student student = new Student(
                        doc.getInteger("_id"),
                        doc.getString("name"),
                        doc.getInteger("age"),
                        doc.getString("email"),
                        doc.getString("phoneNumber"),
                        doc.getString("address"));

                // Set additional fields if they exist
                if (doc.getDouble("gpa") != null) {
                    student.setGPA(doc.getDouble("gpa"));
                }
                if (doc.getString("course") != null) {
                    Course course = getCourseById(doc.getString("course"));
                    if (course != null) {
                        student.setCourse(course);
                    }
                }

                return student;
            }
            return null;
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
            // First verify both student and course exist
            Document studentDoc = studentsCollection.find(
                    new Document("_id", Integer.parseInt(studentId))).first();

            Document courseDoc = coursesCollection.find(
                    new Document("courseId", courseId)).first();

            if (studentDoc == null) {
                throw new RuntimeException("Student not found with ID: " + studentId);
            }
            if (courseDoc == null) {
                throw new RuntimeException("Course not found with ID: " + courseId);
            }

            // Check if student is already enrolled in a course
            String currentCourseId = studentDoc.getString("course");
            if (currentCourseId != null && !currentCourseId.equals(courseId)) {
                // Remove student from current course
                studentCoursesCollection.deleteOne(
                        new Document()
                                .append("studentId", Integer.parseInt(studentId))
                                .append("courseId", currentCourseId));
            }

            // Create or update the student-course assignment
            Document assignmentDoc = new Document()
                    .append("studentId", Integer.parseInt(studentId))
                    .append("courseId", courseId)
                    .append("assignedAt", new Date());

            // Use upsert to either update existing assignment or create new one
            studentCoursesCollection.updateOne(
                    new Document()
                            .append("studentId", Integer.parseInt(studentId))
                            .append("courseId", courseId),
                    new Document("$set", assignmentDoc),
                    new UpdateOptions().upsert(true));

            // Update the student's course field
            studentsCollection.updateOne(
                    new Document("_id", Integer.parseInt(studentId)),
                    new Document("$set", new Document("course", courseId)));

            System.out.println("Successfully assigned course " + courseId + " to student " + studentId);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid student ID format: " + studentId);
        } catch (Exception e) {
            System.err.println("Error assigning course to student: " + e.getMessage());
            throw new RuntimeException("Failed to assign course to student: " + e.getMessage());
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
                        try {
                            int studentId = doc.getInteger("studentId");
                            Document studentDoc = studentsCollection.find(
                                    new Document("_id", studentId)).first();

                            if (studentDoc != null) {
                                Student student = new Student(
                                        studentDoc.getInteger("_id"),
                                        studentDoc.getString("name"),
                                        studentDoc.getInteger("age"),
                                        studentDoc.getString("email"),
                                        studentDoc.getString("phoneNumber"),
                                        studentDoc.getString("address"));
                                students.add(student);
                            }
                        } catch (Exception e) {
                            System.err.println("Error processing student document: " + e.getMessage());
                        }
                    });
            return students;
        } catch (Exception e) {
            System.err.println("Error fetching students in course: " + e.getMessage());
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

    public Course getCourseById(String courseId) {
        Document doc = coursesCollection.find(new Document("courseId", courseId)).first();
        if (doc != null) {
            return new Course(
                    doc.getString("courseId"),
                    doc.getString("courseName"),
                    doc.getString("description"));
        }
        return null;
    }
}
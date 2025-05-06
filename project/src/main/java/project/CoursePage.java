package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CoursePage extends JFrame {
    private StudentManagementSystem mainSystem;
    private StudentDataManager dataManager;
    private JTextField courseIdField;
    private JTextField courseNameField;
    private DefaultListModel<String> courseListModel;
    private JList<String> courseList;
    private JButton addCourseButton, assignButton, backButton;
    private JList<String> studentJList;
    private DefaultListModel<String> studentListModel;
    private Map<String, Set<Student>> courseAssignments; // Changed to use Student objects

    public CoursePage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        this.courseAssignments = new HashMap<>();

        setTitle("Course Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadStudentsAndCourses();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(240, 244, 250));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        topPanel.setBackground(new Color(240, 244, 250));

        JLabel titleLabel = new JLabel("Course Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(47, 128, 237));
        topPanel.add(titleLabel, BorderLayout.WEST);

        backButton = createStyledButton("Back to Dashboard", new Color(47, 128, 237));
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });
        topPanel.add(backButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center panel: Courses and Students
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        centerPanel.setBackground(new Color(240, 244, 250));

        // Left: Course List and Add
        JPanel coursePanel = new JPanel(new BorderLayout(10, 18));
        coursePanel.setBackground(Color.WHITE);
        coursePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(47, 128, 237), 2, true),
                "Courses",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(47, 128, 237)));

        courseListModel = new DefaultListModel<>();
        courseList = new JList<>(courseListModel);
        courseList.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setFixedCellHeight(32);
        JScrollPane courseScroll = new JScrollPane(courseList);

        coursePanel.add(courseScroll, BorderLayout.CENTER);

        JPanel addCoursePanel = new JPanel(new GridLayout(3, 2, 8, 8));
        addCoursePanel.setBackground(Color.WHITE);
        addCoursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addCoursePanel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField();
        addCoursePanel.add(courseIdField);

        addCoursePanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField();
        addCoursePanel.add(courseNameField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 8, 0));
        buttonPanel.setBackground(Color.WHITE);

        addCourseButton = createStyledButton("Add Course", new Color(48, 209, 88));
        addCourseButton.addActionListener(e -> addCourse());
        buttonPanel.add(addCourseButton);

        JButton deleteCourseButton = createStyledButton("Delete Course", new Color(255, 59, 48));
        deleteCourseButton.addActionListener(e -> deleteSelectedCourse());
        buttonPanel.add(deleteCourseButton);

        addCoursePanel.add(buttonPanel);

        coursePanel.add(addCoursePanel, BorderLayout.SOUTH);

        centerPanel.add(coursePanel);

        // Right: Student Assignment
        JPanel studentPanel = new JPanel(new BorderLayout(10, 18));
        studentPanel.setBackground(Color.WHITE);
        studentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(47, 128, 237), 2, true),
                "Assign Students to Course",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 18),
                new Color(47, 128, 237)));

        studentListModel = new DefaultListModel<>();
        studentJList = new JList<>(studentListModel);
        studentJList.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        studentJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        studentJList.setFixedCellHeight(32);
        JScrollPane studentScroll = new JScrollPane(studentJList);

        studentPanel.add(studentScroll, BorderLayout.CENTER);

        assignButton = createStyledButton("Assign Selected Students", new Color(47, 128, 237));
        assignButton.addActionListener(e -> assignStudentsToCourse());

        studentPanel.add(assignButton, BorderLayout.SOUTH);

        centerPanel.add(studentPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        courseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateStudentSelection();
            }
        });
    }

    private void loadStudentsAndCourses() {
        try {
            // Load students
            studentListModel.clear();
            List<Student> students = dataManager.getAllStudents();
            if (students != null) {
                for (Student student : students) {
                    String courseInfo = student.getCourse() != null
                            ? " (Enrolled in: " + student.getCourse().getCourseName() + ")"
                            : "";
                    String studentDisplay = String.format("%s - %s%s",
                            student.getId(),
                            student.getName(),
                            courseInfo);
                    studentListModel.addElement(studentDisplay);
                }
                System.out.println("Loaded " + students.size() + " students");
            }

            // Load courses
            courseListModel.clear();
            List<Course> courses = dataManager.getAllCourses();
            if (courses != null) {
                for (Course course : courses) {
                    // Get number of students enrolled in this course
                    List<Student> enrolledStudents = dataManager.getStudentsInCourse(course.getCourseId());
                    String studentCount = String.format(" (%d students enrolled)", enrolledStudents.size());

                    String courseDisplay = String.format("%s - %s%s",
                            course.getCourseId(),
                            course.getCourseName(),
                            studentCount);
                    courseListModel.addElement(courseDisplay);
                }
                System.out.println("Loaded " + courses.size() + " courses");
            }

            // Update student selection if a course is selected
            if (courseList.getSelectedValue() != null) {
                updateStudentSelection();
            }
        } catch (Exception e) {
            String errorMsg = "Error loading data: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCourse() {
        String courseId = courseIdField.getText().trim();
        String courseName = courseNameField.getText().trim();

        if (courseId.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Course ID and Course Name");
            return;
        }

        try {
            // Check if course already exists
            Course existingCourse = dataManager.getCourseById(courseId);
            if (existingCourse != null) {
                JOptionPane.showMessageDialog(this,
                        "Course with ID " + courseId + " already exists",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create new course
            Course newCourse = new Course(courseId, courseName, "");
            dataManager.addCourse(newCourse);
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            clearFields();
            loadStudentsAndCourses(); // Refresh the lists
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding course: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignStudentsToCourse() {
        String selectedCourse = courseList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first.");
            return;
        }

        String courseId = selectedCourse.split(" - ")[0];
        List<String> selectedStudentIds = new ArrayList<>();

        for (String studentDisplay : studentJList.getSelectedValuesList()) {
            String studentId = studentDisplay.split(" - ")[0];
            selectedStudentIds.add(studentId);
        }

        if (selectedStudentIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one student to assign.");
            return;
        }

        try {
            // First verify the course exists
            Course course = dataManager.getCourseById(courseId);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Selected course not found in database.");
                return;
            }

            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMessages = new StringBuilder();

            for (String studentId : selectedStudentIds) {
                try {
                    // Get the student to verify it exists
                    Student student = dataManager.getStudentById(Integer.parseInt(studentId));
                    if (student == null) {
                        failCount++;
                        errorMessages.append("Student ").append(studentId).append(" not found.\n");
                        continue;
                    }

                    // Check if student is already assigned to this course
                    List<Student> studentsInCourse = dataManager.getStudentsInCourse(courseId);
                    boolean alreadyAssigned = studentsInCourse.stream()
                            .anyMatch(s -> s.getId() == Integer.parseInt(studentId));

                    if (alreadyAssigned) {
                        failCount++;
                        errorMessages.append("Student ").append(studentId)
                                .append(" is already assigned to this course.\n");
                        continue;
                    }

                    // Assign the course to the student
                    dataManager.assignCourseToStudent(studentId, courseId);
                    successCount++;
                } catch (NumberFormatException e) {
                    failCount++;
                    errorMessages.append("Invalid student ID format: ").append(studentId).append("\n");
                } catch (Exception e) {
                    failCount++;
                    errorMessages.append("Error assigning student ").append(studentId).append(": ")
                            .append(e.getMessage()).append("\n");
                }
            }

            // Show appropriate message based on results
            if (successCount > 0) {
                String message = successCount + " student(s) assigned successfully.";
                if (failCount > 0) {
                    message += "\n\nFailed to assign " + failCount + " student(s):\n" + errorMessages.toString();
                }
                JOptionPane.showMessageDialog(this, message,
                        failCount > 0 ? "Partial Success" : "Success",
                        failCount > 0 ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);

                // Refresh the lists to show updated assignments
                loadStudentsAndCourses();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to assign any students:\n" + errorMessages.toString(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            String errorMsg = "Error assigning students to course: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudentSelection() {
        String selectedCourse = courseList.getSelectedValue();
        if (selectedCourse == null) {
            studentJList.clearSelection();
            studentJList.setEnabled(false);
            assignButton.setEnabled(false);
            return;
        }

        studentJList.setEnabled(true);
        assignButton.setEnabled(true);

        try {
            String courseId = selectedCourse.split(" - ")[0];
            List<Student> assignedStudents = dataManager.getStudentsInCourse(courseId);
            System.out.println("Found " + assignedStudents.size() + " students in course " + courseId);

            // Select students already assigned to this course
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < studentListModel.size(); i++) {
                String currentStudentDisplay = studentListModel.get(i);
                String currentStudentId = currentStudentDisplay.split(" - ")[0];

                for (Student assignedStudent : assignedStudents) {
                    if (assignedStudent.getId() == Integer.parseInt(currentStudentId)) {
                        indices.add(i);
                        break;
                    }
                }
            }

            int[] selectedIndices = indices.stream().mapToInt(i -> i).toArray();
            studentJList.setSelectedIndices(selectedIndices);
        } catch (Exception e) {
            String errorMsg = "Error updating student selection: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        courseList.clearSelection();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1, true),
                BorderFactory.createEmptyBorder(10, 24, 10, 24)));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void deleteSelectedCourse() {
        String selectedCourse = courseList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        String courseId = selectedCourse.split(" - ")[0];

        // Get number of enrolled students
        List<Student> enrolledStudents = dataManager.getStudentsInCourse(courseId);
        int studentCount = enrolledStudents.size();

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this course?\n" +
                        "Course: " + selectedCourse + "\n" +
                        "Number of enrolled students: " + studentCount + "\n\n" +
                        "This will remove all student enrollments for this course.",
                "Confirm Course Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Delete the course
                dataManager.deleteCourse(courseId);

                // Show success message
                JOptionPane.showMessageDialog(this,
                        "Course deleted successfully.\n" +
                                "Removed " + studentCount + " student enrollments.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the lists
                loadStudentsAndCourses();
            } catch (Exception e) {
                String errorMsg = "Error deleting course: " + e.getMessage();
                System.err.println(errorMsg);
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        errorMsg,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

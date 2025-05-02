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

        addCourseButton = createStyledButton("Add Course", new Color(48, 209, 88));
        addCourseButton.addActionListener(e -> addCourse());
        addCoursePanel.add(addCourseButton);

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
            for (Student student : students) {
                String studentDisplay = student.getStudentId() + " - " + student.getName();
                studentListModel.addElement(studentDisplay);
            }

            // Load courses
            courseListModel.clear();
            List<Course> courses = dataManager.getAllCourses();
            for (Course course : courses) {
                courseListModel.addElement(course.getCourseId() + " - " + course.getCourseName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
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
            // Create new course
            Course newCourse = new Course(courseId, courseName, "");
            dataManager.addCourse(newCourse);
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            clearFields();
            loadStudentsAndCourses(); // Refresh the course list
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

        try {
            for (String studentId : selectedStudentIds) {
                dataManager.assignCourseToStudent(studentId, courseId);
            }
            JOptionPane.showMessageDialog(this, "Students assigned to course successfully!");
            updateStudentSelection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error assigning students to course: " + e.getMessage(),
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

            // Select students already assigned to this course
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < studentListModel.size(); i++) {
                String currentStudentId = studentListModel.get(i).split(" - ")[0];
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
            JOptionPane.showMessageDialog(this,
                    "Error updating student selection: " + e.getMessage(),
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
}

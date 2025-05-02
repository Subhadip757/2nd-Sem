package project;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarksPage extends JFrame {
    private StudentManagementSystem mainSystem;
    private JComboBox<String> courseComboBox;
    private JComboBox<String> studentComboBox;
    private JPanel marksPanel;
    private Map<String, JTextField> marksFields;
    private StudentDataManager dataManager;

    public MarksPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        this.marksFields = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Assign Marks");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel for Course and Student Selection
        JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        courseComboBox = new JComboBox<>(new String[] { "Select Course" });
        studentComboBox = new JComboBox<>(new String[] { "Select Student" });

        selectionPanel.add(new JLabel("Select Course:"));
        selectionPanel.add(courseComboBox);
        selectionPanel.add(new JLabel("Select Student:"));
        selectionPanel.add(studentComboBox);

        mainPanel.add(selectionPanel, BorderLayout.NORTH);

        // Center Panel for Marks Entry
        marksPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        marksPanel.setBorder(BorderFactory.createTitledBorder("Enter Marks"));

        for (String subject : Subject.CS_SUBJECTS) {
            marksPanel.add(new JLabel(subject + ":"));
            JTextField markField = new JTextField();
            marksFields.put(subject, markField);
            marksPanel.add(markField);
        }

        mainPanel.add(new JScrollPane(marksPanel), BorderLayout.CENTER);

        // Bottom Panel for Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Marks");
        JButton backButton = new JButton("Back to Dashboard");

        saveButton.addActionListener(e -> saveMarks());
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadCoursesAndStudents();
    }

    private void loadCoursesAndStudents() {
        try {
            courseComboBox.removeAllItems();
            studentComboBox.removeAllItems();

            courseComboBox.addItem("Select Course");
            studentComboBox.addItem("Select Student");

            // Debug print
            System.out.println("Loading students and courses...");

            // Fetch courses from database
            List<Course> courses = dataManager.getAllCourses();
            System.out.println("Found " + courses.size() + " courses");

            for (Course course : courses) {
                String courseDisplay = course.getCourseId() + " - " + course.getCourseName();
                courseComboBox.addItem(courseDisplay);
                System.out.println("Added course: " + courseDisplay);
            }

            // Fetch students from database
            List<Student> students = dataManager.getAllStudents();
            System.out.println("Found " + students.size() + " students");

            for (Student student : students) {
                if (student != null && student.getStudentId() != null && student.getName() != null) {
                    String studentDisplay = student.getStudentId() + " - " + student.getName();
                    studentComboBox.addItem(studentDisplay);
                    System.out.println("Added student: " + studentDisplay);
                }
            }

        } catch (Exception e) {
            String errorMsg = "Error loading courses and students: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveMarks() {
        String selectedStudent = (String) studentComboBox.getSelectedItem();
        String selectedCourse = (String) courseComboBox.getSelectedItem();

        if (selectedStudent == null || selectedStudent.equals("Select Student") ||
                selectedCourse == null || selectedCourse.equals("Select Course")) {
            JOptionPane.showMessageDialog(this, "Please select both course and student");
            return;
        }

        // Extract student ID from the selected item
        String studentId = selectedStudent.split(" - ")[0];
        String courseId = selectedCourse.split(" - ")[0];

        boolean validInput = true;
        Map<String, Integer> marks = new HashMap<>();

        for (Map.Entry<String, JTextField> entry : marksFields.entrySet()) {
            try {
                int mark = Integer.parseInt(entry.getValue().getText().trim());
                if (mark < 0 || mark > 100) {
                    validInput = false;
                    break;
                }
                marks.put(entry.getKey(), mark);
            } catch (NumberFormatException e) {
                validInput = false;
                break;
            }
        }

        if (!validInput) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks (0-100) for all subjects");
            return;
        }

        try {
            // Save marks to database
            dataManager.saveStudentMarks(studentId, courseId, marks);
            JOptionPane.showMessageDialog(this, "Marks saved successfully!");
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving marks: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        for (JTextField field : marksFields.values()) {
            field.setText("");
        }
        courseComboBox.setSelectedIndex(0);
        studentComboBox.setSelectedIndex(0);
    }
}
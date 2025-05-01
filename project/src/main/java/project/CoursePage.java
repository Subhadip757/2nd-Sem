package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CoursePage extends JFrame {
    private StudentManagementSystem mainSystem;
    private DefaultListModel<String> courseListModel;
    private JList<String> courseList;
    private JTextField courseNameField;
    private JButton addCourseButton, assignButton, backButton;
    private JList<String> studentJList;
    private DefaultListModel<String> studentListModel;
    private Map<String, Set<String>> courseAssignments; // course -> set of student names

    public CoursePage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.courseAssignments = new HashMap<>();
        setTitle("Courses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
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

        JPanel addCoursePanel = new JPanel(new BorderLayout(8, 0));
        addCoursePanel.setBackground(Color.WHITE);
        courseNameField = new JTextField();
        courseNameField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        courseNameField.setPreferredSize(new Dimension(120, 38));
        addCourseButton = createStyledButton("Add Course", new Color(48, 209, 88));
        addCourseButton.addActionListener(e -> addCourse());

        addCoursePanel.add(courseNameField, BorderLayout.CENTER);
        addCoursePanel.add(addCourseButton, BorderLayout.EAST);

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

        // Load students
        loadStudents();
    }

    private void addCourse() {
        String courseName = courseNameField.getText().trim();
        if (courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name cannot be empty.");
            return;
        }
        if (courseListModel.contains(courseName)) {
            JOptionPane.showMessageDialog(this, "Course already exists.");
            return;
        }
        courseListModel.addElement(courseName);
        courseAssignments.put(courseName, new HashSet<>());
        courseNameField.setText("");
    }

    private void loadStudents() {
        studentListModel.clear();
        try {
            List<Student> students = new StudentDataManager().getAllStudents();
            for (Student s : students) {
                studentListModel.addElement(s.getName() + " (ID: " + s.getId() + ")");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
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

        // Select students already assigned to this course
        Set<String> assigned = courseAssignments.getOrDefault(selectedCourse, new HashSet<>());
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < studentListModel.size(); i++) {
            if (assigned.contains(studentListModel.get(i))) {
                indices.add(i);
            }
        }
        int[] selectedIndices = indices.stream().mapToInt(i -> i).toArray();
        studentJList.setSelectedIndices(selectedIndices);
    }

    private void assignStudentsToCourse() {
        String selectedCourse = courseList.getSelectedValue();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first.");
            return;
        }
        Set<String> assigned = new HashSet<>();
        for (String student : studentJList.getSelectedValuesList()) {
            assigned.add(student);
        }
        courseAssignments.put(selectedCourse, assigned);
        JOptionPane.showMessageDialog(this, "Assigned students to " + selectedCourse + "!");
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

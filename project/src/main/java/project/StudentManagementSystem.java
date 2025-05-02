package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentManagementSystem extends JFrame {
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private CRUDPage crudPage;
    private final StudentDataManager dataManager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, courseField, ageField, emailField, phoneField, addressField, gpaField,
            searchField;
    private JLabel statusLabel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StudentManagementSystem() {
        dataManager = new StudentDataManager();
        showLoginPage();
    }

    public void showLoginPage() {
        if (loginPage != null) {
            loginPage.dispose();
        }
        loginPage = new LoginPage(this);
        loginPage.setVisible(true);
    }

    public void showDashboard() {
        if (dashboardPage != null) {
            dashboardPage.dispose();
        }
        dashboardPage = new DashboardPage(this);
        dashboardPage.setVisible(true);
    }

    public void showCRUDPage() {
        if (crudPage != null) {
            crudPage.dispose();
        }
        crudPage = new CRUDPage(this);
        crudPage.setVisible(true);
    }

    public void showCoursePage() {
        CoursePage coursePage = new CoursePage(this);
        coursePage.setVisible(true);
    }

    public void showLeaderboardPage() {
        try {
            LeaderboardPage leaderboardPage = new LeaderboardPage(this);
            leaderboardPage.setVisible(true);
            System.out.println("Opening Leaderboard Page...");
        } catch (Exception e) {
            System.err.println("Error opening Leaderboard Page: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error opening Leaderboard: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showCalendarPage() {
        CalendarPage calendarPage = new CalendarPage(this);
        calendarPage.setVisible(true);
    }

    public void showMarksPage() {
        MarksPage marksPage = new MarksPage(this);
        marksPage.setVisible(true);
    }

    public void showResultPage() {
        ResultPage resultPage = new ResultPage(this);
        resultPage.setVisible(true);
    }

    private void initializeUI() {
        setTitle("Student Management System with MongoDB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create top panel with search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createSearchPanel(), BorderLayout.NORTH);
        topPanel.add(createInputPanel(), BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create table
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        searchButton.addActionListener(e -> searchStudents());
        refreshButton.addActionListener(e -> loadStudentsToTable());

        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        // First row
        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        // Second row
        panel.add(new JLabel("Course:"));
        courseField = new JTextField();
        panel.add(courseField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        // Third row
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        // Fourth row
        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("GPA:"));
        gpaField = new JTextField();
        panel.add(gpaField);

        return panel;
    }

    private void createTable() {
        String[] columns = { "ID", "Name", "Course", "Age", "Email", "Phone", "Address", "GPA", "Registration Date" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Course
        table.getColumnModel().getColumn(3).setPreferredWidth(50); // Age
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Email
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Phone
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Address
        table.getColumnModel().getColumn(7).setPreferredWidth(50); // GPA
        table.getColumnModel().getColumn(8).setPreferredWidth(150); // Registration Date
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(table.getValueAt(selectedRow, 0).toString());
            nameField.setText(table.getValueAt(selectedRow, 1).toString());
            courseField.setText(table.getValueAt(selectedRow, 2).toString());
            ageField.setText(table.getValueAt(selectedRow, 3).toString());
            emailField.setText(table.getValueAt(selectedRow, 4).toString());
            phoneField.setText(table.getValueAt(selectedRow, 5).toString());
            addressField.setText(table.getValueAt(selectedRow, 6).toString());
            gpaField.setText(table.getValueAt(selectedRow, 7).toString());
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);

        return panel;
    }

    private boolean validateInput() {
        try {
            if (idField.getText().trim().isEmpty() ||
                    nameField.getText().trim().isEmpty() ||
                    courseField.getText().trim().isEmpty() ||
                    ageField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty() ||
                    gpaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return false;
            }

            int id = Integer.parseInt(idField.getText());
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            double gpa = Double.parseDouble(gpaField.getText().trim());

            if (!Student.isValidAge(age)) {
                throw new IllegalArgumentException("Invalid age (must be between 16 and 100)");
            }
            if (!Student.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (!Student.isValidPhoneNumber(phone)) {
                throw new IllegalArgumentException("Invalid phone number (must be 10 digits)");
            }
            if (!Student.isValidGPA(gpa)) {
                throw new IllegalArgumentException("Invalid GPA (must be between 0.0 and 10.0)");
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID, Age, and GPA");
            return false;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return false;
        }
    }

    private void addStudent() {
        if (!validateInput()) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            if (dataManager.isDuplicateId(id)) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!");
                return;
            }

            if (dataManager.isDuplicateEmail(emailField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Email already exists!");
                return;
            }

            if (dataManager.isDuplicatePhone(phoneField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Phone number already exists!");
                return;
            }

            Student student = new Student(
                    id,
                    nameField.getText().trim(),
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());
            student.setGPA(Double.parseDouble(gpaField.getText().trim()));
            student.setCourse(courseField.getText().trim());

            dataManager.addStudent(student);
            loadStudentsToTable();
            clearFields();
            statusLabel.setText("Student added successfully - " + LocalDateTime.now().format(dateFormatter));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage());
            statusLabel.setText("Error adding student - " + LocalDateTime.now().format(dateFormatter));
        }
    }

    private void updateStudent() {
        if (!validateInput()) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            Student existingStudent = dataManager.getStudentById(id);
            if (existingStudent == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }

            // Check if email is being changed to an existing one
            if (!existingStudent.getEmail().equalsIgnoreCase(emailField.getText().trim())) {
                if (dataManager.isDuplicateEmail(emailField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Email already exists!");
                    return;
                }
            }

            // Check if phone is being changed to an existing one
            if (!existingStudent.getPhoneNumber().equals(phoneField.getText().trim())) {
                if (dataManager.isDuplicatePhone(phoneField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Phone number already exists!");
                    return;
                }
            }

            Student updatedStudent = new Student(
                    id,
                    nameField.getText().trim(),
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());
            updatedStudent.setGPA(Double.parseDouble(gpaField.getText().trim()));
            updatedStudent.setCourse(courseField.getText().trim());
            updatedStudent.setRegistrationDate(existingStudent.getRegistrationDate());

            dataManager.updateStudent(updatedStudent);
            loadStudentsToTable();
            clearFields();
            statusLabel.setText("Student updated successfully - " + LocalDateTime.now().format(dateFormatter));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage());
            statusLabel.setText("Error updating student - " + LocalDateTime.now().format(dateFormatter));
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this student?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dataManager.deleteStudent(id);
                    loadStudentsToTable();
                    clearFields();
                    statusLabel.setText("Student deleted successfully - " + LocalDateTime.now().format(dateFormatter));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage());
                statusLabel.setText("Error deleting student - " + LocalDateTime.now().format(dateFormatter));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }
    }

    private void searchStudents() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadStudentsToTable();
            return;
        }

        try {
            List<Student> results = dataManager.searchStudents(searchText);
            updateTableWithStudents(results);

            if (results.isEmpty()) {
                statusLabel.setText("No matching students found - " + LocalDateTime.now().format(dateFormatter));
            } else {
                statusLabel.setText("Found " + results.size() + " matching students - "
                        + LocalDateTime.now().format(dateFormatter));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching students: " + e.getMessage());
            statusLabel.setText("Error searching students - " + LocalDateTime.now().format(dateFormatter));
        }
    }

    private void loadStudentsToTable() {
        try {
            List<Student> students = dataManager.getAllStudents();
            updateTableWithStudents(students);
            statusLabel
                    .setText("Loaded " + students.size() + " students - " + LocalDateTime.now().format(dateFormatter));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
            statusLabel.setText("Error loading students - " + LocalDateTime.now().format(dateFormatter));
        }
    }

    private void updateTableWithStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[] {
                    student.getId(),
                    student.getName(),
                    student.getCourse(),
                    student.getAge(),
                    student.getEmail(),
                    student.getPhoneNumber(),
                    student.getAddress(),
                    student.getGPA(),
                    student.getRegistrationDate().format(dateFormatter)
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        courseField.setText("");
        ageField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        gpaField.setText("");
        table.clearSelection();
    }

    @Override
    public void dispose() {
        MongoDBConnection.closeConnection();
        super.dispose();
    }

    public static void main(String[] args) {
        try {
            // Initialize database
            DatabaseInitializer.initialize();

            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Start application
            SwingUtilities.invokeLater(() -> {
                try {
                    new StudentManagementSystem();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Error starting application: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
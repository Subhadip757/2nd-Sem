package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CRUDPage extends JFrame {
    private StudentManagementSystem mainSystem;
    private StudentDataManager dataManager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, courseField, ageField, emailField, phoneField, addressField, gpaField;
    private JButton addButton, updateButton, deleteButton, clearButton, backButton;

    public CRUDPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        initializeUI();
        loadStudentsToTable();
    }

    private void initializeUI() {
        setTitle("Student Management System - CRUD Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create input panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Create table
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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
        String[] columns = { "ID", "Name", "Course", "Age", "Email", "Phone", "Address", "GPA" };
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
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back to Dashboard");

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(backButton);

        return panel;
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

            Student student = new Student(
                    id,
                    nameField.getText().trim(),
                    courseField.getText().trim(),
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());
            student.setGPA(Double.parseDouble(gpaField.getText().trim()));

            dataManager.addStudent(student);
            loadStudentsToTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        if (!validateInput()) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            Student student = new Student(
                    id,
                    nameField.getText().trim(),
                    courseField.getText().trim(),
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());
            student.setGPA(Double.parseDouble(gpaField.getText().trim()));

            dataManager.updateStudent(student);
            loadStudentsToTable();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + e.getMessage());
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
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting student: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }
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
            double gpa = Double.parseDouble(gpaField.getText().trim());

            if (age < 0 || age > 120) {
                JOptionPane.showMessageDialog(this, "Invalid age!");
                return false;
            }

            if (gpa < 0 || gpa > 4.0) {
                JOptionPane.showMessageDialog(this, "Invalid GPA!");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID, Age, and GPA");
            return false;
        }
    }

    private void loadStudentsToTable() {
        try {
            List<Student> students = dataManager.getAllStudents();
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
                        student.getGPA()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
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
}
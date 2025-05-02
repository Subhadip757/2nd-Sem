package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javax.swing.border.TitledBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import javax.swing.border.TitledBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class CRUDPage extends JFrame {
    private StudentManagementSystem mainSystem;
    private StudentDataManager dataManager;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, ageField, emailField, phoneField, addressField;
    private JButton addButton, updateButton, deleteButton, clearButton, backButton;

    public CRUDPage(StudentManagementSystem mainSystem) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        initializeUI();
        loadStudentsToTable();
    }

    private void initializeUI() {
        setTitle("Student Management System - CRUD Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.decode("#f4f6fb"));
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 16));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.decode("#f4f6fb"));

        // Left: Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);
        // Left: Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        // Center: Table Panel
        // Center: Table Panel
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 500));
        scrollPane.setPreferredSize(new Dimension(750, 500));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // South: Button Panel
        // South: Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 58, 64), 2, true),
                "Student Information",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 20),
                new Color(52, 58, 64)));
        panel.setBackground(new Color(245, 247, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 17);

        java.util.function.Supplier<JTextField> fieldSupplier = () -> {
            JTextField tf = new JTextField();
            tf.setPreferredSize(new Dimension(220, 32));
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            tf.setBackground(Color.WHITE);
            tf.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    tf.setBorder(BorderFactory.createLineBorder(new Color(23, 162, 184), 2, true));
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
                }
            });
            return tf;
        };

        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        idField = fieldSupplier.get();
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = fieldSupplier.get();
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        panel.add(ageLabel, gbc);
        gbc.gridx = 1;
        ageField = fieldSupplier.get();
        panel.add(ageField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        panel.add(ageLabel, gbc);
        gbc.gridx = 1;
        ageField = fieldSupplier.get();
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        emailField = fieldSupplier.get();
        panel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        emailField = fieldSupplier.get();
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        phoneField = fieldSupplier.get();
        panel.add(phoneField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        phoneField = fieldSupplier.get();
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        panel.add(addressLabel, gbc);
        gbc.gridx = 1;
        addressField = fieldSupplier.get();
        panel.add(addressField, gbc);

        return panel;
    }

    private void createTable() {
        String[] columns = { "ID", "Name", "Age", "Email", "Phone", "Address" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(222, 226, 230));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(222, 226, 230));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBackground(Color.decode("#f4f6fb"));
        panel.setBackground(Color.decode("#f4f6fb"));

        addButton = createIconButton("Add", FontAwesomeSolid.PLUS, new Color(40, 167, 69));
        updateButton = createIconButton("Update", FontAwesomeSolid.PEN, new Color(23, 162, 184));
        deleteButton = createIconButton("Delete", FontAwesomeSolid.TRASH, new Color(220, 53, 69));
        clearButton = createIconButton("Clear", FontAwesomeSolid.ERASER, new Color(255, 193, 7));
        backButton = createIconButton("Back to Dashboard", FontAwesomeSolid.ARROW_LEFT, new Color(108, 117, 125));
        addButton = createIconButton("Add", FontAwesomeSolid.PLUS, new Color(40, 167, 69));
        updateButton = createIconButton("Update", FontAwesomeSolid.PEN, new Color(23, 162, 184));
        deleteButton = createIconButton("Delete", FontAwesomeSolid.TRASH, new Color(220, 53, 69));
        clearButton = createIconButton("Clear", FontAwesomeSolid.ERASER, new Color(255, 193, 7));
        backButton = createIconButton("Back to Dashboard", FontAwesomeSolid.ARROW_LEFT, new Color(108, 117, 125));

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

    private JButton createIconButton(String text, FontAwesomeSolid icon, Color color) {
        JButton button = new JButton(text, FontIcon.of(icon, 18, Color.WHITE));
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(text);

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

    private JButton createIconButton(String text, FontAwesomeSolid icon, Color color) {
        JButton button = new JButton(text, FontIcon.of(icon, 18, Color.WHITE));
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(text);

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

    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(table.getValueAt(selectedRow, 0).toString());
            nameField.setText(table.getValueAt(selectedRow, 1).toString());
            ageField.setText(table.getValueAt(selectedRow, 2).toString());
            emailField.setText(table.getValueAt(selectedRow, 3).toString());
            phoneField.setText(table.getValueAt(selectedRow, 4).toString());
            addressField.setText(table.getValueAt(selectedRow, 5).toString());
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
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());

            dataManager.addStudent(student);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
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
                    Integer.parseInt(ageField.getText()),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    addressField.getText().trim());

            dataManager.updateStudent(student);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
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
                    refreshTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
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
                    ageField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return false;
            }

            int id = Integer.parseInt(idField.getText());
            int age = Integer.parseInt(ageField.getText());

            if (age < 0 || age > 100) {
                JOptionPane.showMessageDialog(this, "Invalid age!");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and Age");
            return false;
        }
    }

    private void loadStudentsToTable() {
        refreshTable();
    }

    private void refreshTable() {
        try {
            List<Student> students = dataManager.getAllStudents();
            tableModel.setRowCount(0);
            for (Student student : students) {
                tableModel.addRow(new Object[] {
                        student.getId(),
                        student.getName(),
                        student.getAge(),
                        student.getEmail(),
                        student.getPhoneNumber(),
                        student.getAddress()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        table.clearSelection();
    }

    // Utility to set global font
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    // Utility to set global font
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
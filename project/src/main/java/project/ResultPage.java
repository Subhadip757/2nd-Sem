package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.border.*;
import java.awt.event.*;

public class ResultPage extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(47, 128, 237);
    private static final Color BACKGROUND_COLOR = new Color(247, 250, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(26, 32, 44);
    private static final Color TEXT_SECONDARY = new Color(113, 128, 150);
    private static final Color SUCCESS_COLOR = new Color(48, 209, 88);
    private static final Color ERROR_COLOR = new Color(220, 38, 38);

    private StudentManagementSystem mainSystem;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private StudentDataManager dataManager;

    public ResultPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Results");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadResults();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        JLabel titleLabel = new JLabel("Student Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(BACKGROUND_COLOR);

        // Create table model with columns
        String[] columns = new String[Subject.CS_SUBJECTS.length + 4];
        columns[0] = "Student ID";
        columns[1] = "Name";
        columns[2] = "Course";
        System.arraycopy(Subject.CS_SUBJECTS, 0, columns, 3, Subject.CS_SUBJECTS.length);
        columns[columns.length - 1] = "Grade";

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        styleTable();

        // Set column widths
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        for (int i = 3; i < columns.length - 1; i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        resultTable.getColumnModel().getColumn(columns.length - 1).setPreferredWidth(80);

        // Create scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(CARD_COLOR);

        // Custom scrollbar styling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(BACKGROUND_COLOR);
        verticalScrollBar.setForeground(PRIMARY_COLOR);
        verticalScrollBar.setPreferredSize(new Dimension(10, 0));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void styleTable() {
        // Table styling
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.setRowHeight(35);
        resultTable.setShowGrid(false);
        resultTable.setIntercellSpacing(new Dimension(0, 0));
        resultTable.setBackground(CARD_COLOR);
        resultTable.setForeground(TEXT_PRIMARY);
        resultTable.setSelectionBackground(new Color(237, 242, 247));
        resultTable.setSelectionForeground(TEXT_PRIMARY);

        // Header styling
        JTableHeader header = resultTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(BACKGROUND_COLOR);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        header.setReorderingAllowed(false);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton refreshButton = new JButton("🔄 Refresh Results");
        JButton backButton = new JButton("← Back to Dashboard");

        // Use more vibrant colors and make buttons larger
        styleButton(refreshButton, new Color(37, 99, 235), true); // Vibrant blue
        styleButton(backButton, new Color(75, 85, 99), false); // Lighter gray

        refreshButton.addActionListener(e -> {
            refreshButton.setEnabled(false);
            refreshButton.setText("🔄 Refreshing...");
            loadResults();
            Timer timer = new Timer(1000, evt -> {
                refreshButton.setEnabled(true);
                refreshButton.setText("🔄 Refresh Results");
            });
            timer.setRepeats(false);
            timer.start();
        });

        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private void styleButton(JButton button, Color color, boolean isPrimary) {
        // Make text larger and bolder
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(color);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Increase padding for larger buttons
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2), // Thicker border
                BorderFactory.createEmptyBorder(15, 30, 15, 30) // More padding
        ));

        // Add shadow effect for primary button
        if (isPrimary) {
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(color.darker(), 2),
                            BorderFactory.createEmptyBorder(15, 30, 15, 30)),
                    BorderFactory.createEmptyBorder(0, 0, 2, 0) // Bottom shadow
            ));
        }

        // Enhanced hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(new Color(29, 78, 216)); // Darker blue
                } else {
                    button.setBackground(new Color(55, 65, 81)); // Darker gray
                }
                button.setForeground(Color.BLACK);
                if (isPrimary) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(color.darker().darker(), 2),
                            BorderFactory.createEmptyBorder(15, 30, 15, 30)));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
                button.setForeground(Color.BLACK);
                if (isPrimary) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(color.darker(), 2),
                            BorderFactory.createEmptyBorder(15, 30, 15, 30)));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(new Color(30, 64, 175)); // Even darker blue
                } else {
                    button.setBackground(new Color(31, 41, 55)); // Even darker gray
                }
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isPrimary) {
                    button.setBackground(new Color(29, 78, 216)); // Back to hover color
                } else {
                    button.setBackground(new Color(55, 65, 81)); // Back to hover color
                }
                button.setForeground(Color.BLACK);
            }
        });
    }

    private void loadResults() {
        tableModel.setRowCount(0);

        try {
            List<Student> students = dataManager.getAllStudents();

            for (Student student : students) {
                Map<String, Integer> marks = dataManager.getStudentMarks(student.getId());
                if (marks == null || marks.isEmpty()) {
                    continue;
                }

                // Create Result object to calculate GPA
                Result result = new Result(
                        String.valueOf(student.getId()),
                        student.getCourse() != null ? student.getCourse().getCourseId() : "N/A");
                marks.forEach(result::addSubjectMark);

                // Create row data
                Object[] rowData = new Object[tableModel.getColumnCount()];
                rowData[0] = student.getId();
                rowData[1] = student.getName();
                rowData[2] = student.getCourse() != null ? student.getCourse().getCourseName() : "Not Enrolled";

                // Fill marks for each subject
                for (int i = 0; i < Subject.CS_SUBJECTS.length; i++) {
                    Integer mark = marks.get(Subject.CS_SUBJECTS[i]);
                    rowData[i + 3] = mark != null ? mark : "-";
                }

                // Set grade and save GPA
                String grade = result.getGrade();
                rowData[rowData.length - 1] = grade;

                // Save GPA to database
                dataManager.updateStudentGPA(student.getId(), result.getGPA());

                tableModel.addRow(rowData);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading results: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ResultPage extends JFrame {
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
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = new String[Subject.CS_SUBJECTS.length + 4];
        columns[0] = "Student ID";
        columns[1] = "Name";
        columns[2] = "Course";
        System.arraycopy(Subject.CS_SUBJECTS, 0, columns, 3, Subject.CS_SUBJECTS.length);
        columns[columns.length - 1] = "Grade";

        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        for (int i = 3; i < columns.length - 1; i++) {
            resultTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
        resultTable.getColumnModel().getColumn(columns.length - 1).setPreferredWidth(80);

        mainPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Dashboard");

        refreshButton.addActionListener(e -> loadResults());
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadResults();
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
                rowData[2] = student.getCourse();

                // Fill marks for each subject
                for (int i = 0; i < Subject.CS_SUBJECTS.length; i++) {
                    Integer mark = marks.get(Subject.CS_SUBJECTS[i]);
                    rowData[i + 3] = mark != null ? mark : "-";
                }

                // Set grade and save GPA
                rowData[rowData.length - 1] = result.getGrade();

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

    private String calculateGrade(double average) {
        if (average >= 90)
            return "A+";
        if (average >= 80)
            return "A";
        if (average >= 70)
            return "B";
        if (average >= 60)
            return "C";
        if (average >= 50)
            return "D";
        return "F";
    }
}
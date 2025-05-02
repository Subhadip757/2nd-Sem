package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LeaderboardPage extends JFrame {
    private StudentManagementSystem mainSystem;
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> courseComboBox;
    private MongoCollection<Document> resultsCollection;
    private MongoCollection<Document> studentsCollection;
    private static final Color PRIMARY_COLOR = new Color(47, 128, 237); // Blue
    private static final Color BACKGROUND_COLOR = new Color(247, 250, 252); // Light Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(26, 32, 44); // Dark Gray
    private static final Color TEXT_SECONDARY = new Color(113, 128, 150); // Medium Gray
    private static final Color GOLD_COLOR = new Color(255, 215, 0);
    private static final Color SILVER_COLOR = new Color(192, 192, 192);
    private static final Color BRONZE_COLOR = new Color(205, 127, 50);

    public LeaderboardPage(StudentManagementSystem mainSystem) {
        try {
            System.out.println("Initializing Leaderboard Page...");
            this.mainSystem = mainSystem;
            this.resultsCollection = MongoDBConnection.getInstance().getDatabase().getCollection("results");
            this.studentsCollection = MongoDBConnection.getInstance().getDatabase().getCollection("students");

            // Verify MongoDB collections are accessible
            System.out.println("Checking MongoDB collections...");
            System.out.println("Results collection exists: " + (resultsCollection != null));
            System.out.println("Students collection exists: " + (studentsCollection != null));

            initializeUI();
            loadCourses(); // Load courses first
            loadLeaderboard(); // Then load the leaderboard
            System.out.println("Leaderboard Page initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error in LeaderboardPage constructor: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Leaderboard Page: " + e.getMessage());
        }
    }

    private void initializeUI() {
        setTitle("Student Leaderboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Enhanced Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Enhanced Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Enhanced Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        courseComboBox.addActionListener(e -> loadLeaderboard());
        loadLeaderboard(); // Initial load
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 15));
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)));

        // Title with icon
        JLabel titleLabel = new JLabel("🏆 Student Performance Leaderboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Course selection panel with modern styling
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(CARD_COLOR);

        JLabel courseLabel = new JLabel("📚 Select Course:");
        courseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        courseLabel.setForeground(TEXT_SECONDARY);

        courseComboBox = new JComboBox<>(new String[] { "All Courses" });
        courseComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseComboBox.setPreferredSize(new Dimension(200, 35));
        courseComboBox.setBackground(Color.WHITE);

        filterPanel.add(courseLabel);
        filterPanel.add(courseComboBox);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Create table with enhanced styling
        String[] columns = { "Rank", "Student ID", "Name", "Course", "Total Marks", "Percentage", "Grade" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leaderboardTable.setRowHeight(40);
        leaderboardTable.setShowGrid(true);
        leaderboardTable.setGridColor(new Color(226, 232, 240));
        leaderboardTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        leaderboardTable.getTableHeader().setBackground(new Color(247, 250, 252));
        leaderboardTable.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Enhanced column styling
        int[] columnWidths = { 80, 120, 200, 250, 120, 120, 100 };
        for (int i = 0; i < columnWidths.length; i++) {
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            leaderboardTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Custom renderer for rank column with medals
        leaderboardTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);

                String rankText = value.toString();
                if (row == 0) {
                    setText("🥇 " + rankText);
                    setBackground(GOLD_COLOR);
                } else if (row == 1) {
                    setText("🥈 " + rankText);
                    setBackground(SILVER_COLOR);
                } else if (row == 2) {
                    setText("🥉 " + rankText);
                    setBackground(BRONZE_COLOR);
                } else {
                    setText(rankText);
                    setBackground(table.getBackground());
                }

                return c;
            }
        });

        // Custom renderer for grade column
        leaderboardTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);

                String grade = value.toString();
                switch (grade) {
                    case "A+":
                    case "A":
                        setForeground(new Color(34, 197, 94)); // Green
                        break;
                    case "B":
                        setForeground(new Color(2, 132, 199)); // Blue
                        break;
                    case "C":
                        setForeground(new Color(234, 179, 8)); // Yellow
                        break;
                    default:
                        setForeground(new Color(239, 68, 68)); // Red
                }
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton refreshButton = new JButton("🔄 Refresh");
        JButton backButton = new JButton("← Back to Dashboard");

        // Use more vibrant colors
        styleButton(refreshButton, new Color(0, 123, 255)); // Bright blue
        styleButton(backButton, new Color(52, 58, 64)); // Dark gray

        refreshButton.addActionListener(e -> loadLeaderboard());
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 48));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2, true),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setMargin(new Insets(10, 25, 10, 25));
        button.setFocusable(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void loadCourses() {
        try {
            System.out.println("Loading courses...");
            Set<String> courses = new HashSet<>();
            courses.add("All Courses");

            // Fetch unique courses from results collection
            MongoCursor<String> cursor = resultsCollection.distinct("courseId", String.class).iterator();
            while (cursor.hasNext()) {
                String course = cursor.next();
                if (course != null && !course.trim().isEmpty()) {
                    courses.add(course);
                    System.out.println("Found course: " + course);
                }
            }

            if (courses.size() <= 1) {
                System.out.println("No courses found in database");
            }

            // Update the combo box
            SwingUtilities.invokeLater(() -> {
                courseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(new String[0])));
            });

        } catch (Exception e) {
            System.err.println("Error loading courses: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void loadLeaderboard() {
        try {
            System.out.println("Loading leaderboard data...");
            tableModel.setRowCount(0);
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            System.out.println("Selected course: " + selectedCourse);

            // Build the aggregation pipeline
            List<Document> pipeline = new ArrayList<>();

            // Match stage for specific course if selected
            if (selectedCourse != null && !selectedCourse.equals("All Courses")) {
                String courseId = selectedCourse.split(" - ")[0]; // Extract course ID if it contains course name
                pipeline.add(new Document("$match",
                        new Document("courseId", courseId)));
            }

            // Group by student and calculate total marks
            pipeline.add(new Document("$group", new Document()
                    .append("_id", "$studentId")
                    .append("totalMarks", new Document("$sum", "$mark")) // Changed from "marks" to "mark"
                    .append("courseCount", new Document("$sum", 1))
                    .append("courses", new Document("$push", "$courseId"))));

            // Add lookup stage to get student details
            pipeline.add(new Document("$lookup", new Document()
                    .append("from", "students")
                    .append("localField", "_id")
                    .append("foreignField", "studentId") // Changed from "_id" to "studentId"
                    .append("as", "studentDetails")));

            // Unwind student details
            pipeline.add(new Document("$unwind", "$studentDetails"));

            // Add percentage calculation
            pipeline.add(new Document("$addFields", new Document()
                    .append("percentage", new Document("$multiply", Arrays.asList(
                            new Document("$divide", Arrays.asList(
                                    "$totalMarks",
                                    new Document("$multiply", Arrays.asList("$courseCount", 100)))),
                            100)))));

            // Sort by percentage in descending order
            pipeline.add(new Document("$sort", new Document("percentage", -1)));

            System.out.println("Executing MongoDB aggregation with pipeline: " + pipeline);
            List<Document> results = resultsCollection.aggregate(pipeline).into(new ArrayList<>());
            System.out.println("Found " + results.size() + " results");

            // Debug: Print raw results
            for (Document doc : results) {
                System.out.println("Raw result document: " + doc.toJson());
            }

            // Process and display results
            int rank = 1;
            for (Document result : results) {
                try {
                    String studentId = result.getString("_id");
                    Document studentDetails = (Document) result.get("studentDetails");
                    String studentName = studentDetails.getString("name");
                    Number totalMarks = result.get("totalMarks", Number.class);
                    Number percentage = result.get("percentage", Number.class);
                    List<String> courses = (List<String>) result.get("courses");

                    // Handle null values
                    double totalMarksValue = totalMarks != null ? totalMarks.doubleValue() : 0.0;
                    double percentageValue = percentage != null ? percentage.doubleValue() : 0.0;
                    String courseDisplay = courses != null ? String.join(", ", courses) : "";

                    String grade = calculateGrade(percentageValue);

                    Object[] row = {
                            rank++,
                            studentId,
                            studentName,
                            courseDisplay,
                            String.format("%.2f", totalMarksValue),
                            String.format("%.2f%%", percentageValue),
                            grade
                    };
                    tableModel.addRow(row);
                    System.out.println("Added row for student: " + studentName +
                            " with percentage: " + percentageValue);
                } catch (Exception e) {
                    System.err.println("Error processing result: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (tableModel.getRowCount() == 0) {
                System.out.println("No results found in the database");
                JOptionPane.showMessageDialog(this,
                        "No results found. Please ensure marks have been entered in the Marks page first.",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading leaderboard: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String calculateGrade(double percentage) {
        if (percentage >= 90)
            return "A+";
        if (percentage >= 80)
            return "A";
        if (percentage >= 70)
            return "B";
        if (percentage >= 60)
            return "C";
        if (percentage >= 50)
            return "D";
        return "F";
    }
}
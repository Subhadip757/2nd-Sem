package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPage extends JFrame {
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(47, 128, 237); // Blue
    private static final Color SECONDARY_COLOR = new Color(45, 55, 72); // Dark Blue Gray
    private static final Color ACCENT_COLOR = new Color(48, 209, 88); // Green
    private static final Color BACKGROUND_COLOR = new Color(247, 250, 252); // Light Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(26, 32, 44); // Dark Gray
    private static final Color TEXT_SECONDARY = new Color(113, 128, 150); // Medium Gray

    private StudentManagementSystem mainSystem;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    private StudentDataManager dataManager;
    private JLabel totalStudentsLabel;
    private JLabel averageGPALabel;

    public DashboardPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        initializeUI();
        updateStatistics();
    }

    private void initializeUI() {
        setTitle("Student Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main panel with modern layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create navbar
        JPanel navbar = createNavbar();
        mainPanel.add(navbar, BorderLayout.NORTH);

        // Create content panel with sidebar and main content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create sidebar
        JPanel sidebar = createSidebar();
        contentPanel.add(sidebar, BorderLayout.WEST);

        // Create main content area
        JPanel mainContent = createMainContent();
        contentPanel.add(mainContent, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(CARD_COLOR);
        navbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // Left side - Welcome message
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(CARD_COLOR);
        welcomeLabel = new JLabel("Welcome, Admin");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        leftPanel.add(welcomeLabel);
        navbar.add(leftPanel, BorderLayout.WEST);

        // Right side - Only Logout button now
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(CARD_COLOR);

        // Logout button
        logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(220, 38, 38));
        logoutButton.addActionListener(e -> {
            mainSystem.showLoginPage();
            dispose();
        });
        rightPanel.add(logoutButton);

        navbar.add(rightPanel, BorderLayout.EAST);
        return navbar;
    }

    private JButton createIconButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(TEXT_SECONDARY);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(CARD_COLOR);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(20, 0, 20, 0)));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        addSidebarItem(sidebar, "📊 Dashboard", e -> {
        }, true);
        addSidebarItem(sidebar, "👥 Student Records", e -> {
            mainSystem.showCRUDPage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "📚 Courses", e -> {
            JOptionPane.showMessageDialog(this, "Courses module coming soon!");
        }, false);
        addSidebarItem(sidebar, "📝 Attendance", e -> {
            JOptionPane.showMessageDialog(this, "Attendance module coming soon!");
        }, false);
        addSidebarItem(sidebar, "📈 Reports", e -> {
            JOptionPane.showMessageDialog(this, "Reports module coming soon!");
        }, false);

        return sidebar;
    }

    private void addSidebarItem(JPanel sidebar, String text, ActionListener action, boolean isActive) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(TEXT_SECONDARY);

        if (isActive) {
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
        }

        button.addActionListener(action);
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(BACKGROUND_COLOR);

        // Welcome Section
        JPanel welcomeSection = new JPanel(new BorderLayout());
        welcomeSection.setBackground(BACKGROUND_COLOR);
        JLabel dashboardTitle = new JLabel("Dashboard Overview");
        dashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        dashboardTitle.setForeground(TEXT_PRIMARY);
        welcomeSection.add(dashboardTitle, BorderLayout.NORTH);

        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Total Students card
        JPanel totalStudentsCard = createStatCard("👥 Total Students", "0", "Total number of enrolled students",
                ACCENT_COLOR);
        statsPanel.add(totalStudentsCard);

        // Average GPA card
        JPanel averageGPACard = createStatCard("📊 Average GPA", "0.00", "Average GPA across all students",
                PRIMARY_COLOR);
        statsPanel.add(averageGPACard);

        // Store references to labels
        for (Component comp : totalStudentsCard.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 28) {
                totalStudentsLabel = (JLabel) comp;
                break;
            }
        }

        for (Component comp : averageGPACard.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 28) {
                averageGPALabel = (JLabel) comp;
                break;
            }
        }

        welcomeSection.add(statsPanel, BorderLayout.CENTER);
        mainContent.add(welcomeSection, BorderLayout.NORTH);

        return mainContent;
    }

    private JPanel createStatCard(String title, String value, String description, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        // Title with icon
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descLabel);

        return card;
    }

    private void updateStatistics() {
        try {
            List<Student> students = dataManager.getAllStudents();
            int totalStudents = students.size();
            double totalGPA = 0.0;

            for (Student student : students) {
                totalGPA += student.getGPA();
            }

            double averageGPA = totalStudents > 0 ? totalGPA / totalStudents : 0.0;

            if (totalStudentsLabel != null) {
                totalStudentsLabel.setText(String.valueOf(totalStudents));
            }
            if (averageGPALabel != null) {
                averageGPALabel.setText(String.format("%.2f", averageGPA));
            }
        } catch (Exception e) {
            System.err.println("Error updating statistics: " + e.getMessage());
        }
    }
}
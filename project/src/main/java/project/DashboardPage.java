package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardPage extends JFrame {
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
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));

        // Create navbar
        JPanel navbar = createNavbar();
        mainPanel.add(navbar, BorderLayout.NORTH);

        // Create content panel with sidebar and main content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 242, 245));

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
        navbar.setBackground(new Color(255, 255, 255));
        navbar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left side - Welcome message
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomeLabel = new JLabel("Welcome, Admin");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(welcomeLabel);
        navbar.add(leftPanel, BorderLayout.WEST);

        // Right side - User profile and notifications
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // Notifications icon
        JButton notificationsButton = new JButton("🔔");
        notificationsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        notificationsButton.setBorderPainted(false);
        notificationsButton.setContentAreaFilled(false);
        rightPanel.add(notificationsButton);

        // User profile
        JButton profileButton = new JButton("👤");
        profileButton.setFont(new Font("Arial", Font.PLAIN, 16));
        profileButton.setBorderPainted(false);
        profileButton.setContentAreaFilled(false);
        rightPanel.add(profileButton);

        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> {
            mainSystem.showLoginPage();
            dispose();
        });
        rightPanel.add(logoutButton);

        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(new Color(255, 255, 255));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Dashboard item
        addSidebarItem(sidebar, "📊 Dashboard", e -> {
        }, true);

        // Student Records item
        addSidebarItem(sidebar, "👥 Student Records", e -> {
            mainSystem.showCRUDPage();
            dispose();
        }, false);

        // Academic Management item
        addSidebarItem(sidebar, "📚 Academic Management", e -> {
            JOptionPane.showMessageDialog(this, "Academic Management module coming soon!");
        }, false);

        // Attendance System item
        addSidebarItem(sidebar, "📝 Attendance System", e -> {
            JOptionPane.showMessageDialog(this, "Attendance System module coming soon!");
        }, false);

        // Reports & Analytics item
        addSidebarItem(sidebar, "📈 Reports & Analytics", e -> {
            JOptionPane.showMessageDialog(this, "Reports & Analytics module coming soon!");
        }, false);

        // Settings item
        addSidebarItem(sidebar, "⚙️ Settings", e -> {
            JOptionPane.showMessageDialog(this, "Settings module coming soon!");
        }, false);

        return sidebar;
    }

    private void addSidebarItem(JPanel sidebar, String text, ActionListener action, boolean isActive) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        if (isActive) {
            button.setBackground(new Color(240, 242, 245));
            button.setOpaque(true);
        }

        button.addActionListener(action);
        sidebar.add(button);
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(240, 242, 245));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));

        // Total Students card
        JPanel totalStudentsCard = createStatCard("👥 Total Students", "0", "Total number of enrolled students");
        statsPanel.add(totalStudentsCard);
        // Find the value label in the card
        for (Component comp : totalStudentsCard.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 24) {
                totalStudentsLabel = (JLabel) comp;
                break;
            }
        }

        // Average GPA card
        JPanel averageGPACard = createStatCard("📊 Average GPA", "0.00", "Average GPA across all students");
        statsPanel.add(averageGPACard);
        // Find the value label in the card
        for (Component comp : averageGPACard.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getFont().getSize() == 24) {
                averageGPALabel = (JLabel) comp;
                break;
            }
        }

        mainContent.add(statsPanel, BorderLayout.NORTH);

        return mainContent;
    }

    private JPanel createStatCard(String title, String value, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(0, 0, 0));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(150, 150, 150));

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
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
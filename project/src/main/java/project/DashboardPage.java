package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardPage extends JFrame {
    private StudentManagementSystem mainSystem;
    private JButton logoutButton;
    private JLabel welcomeLabel;

    public DashboardPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message with current date/time
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome to Student Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add a subtitle
        JLabel subtitleLabel = new JLabel("Administrative Control Panel", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel with grid of buttons
        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Student Management Button
        addDashboardButton(centerPanel, "Student Records", "Manage student profiles and academic records",
                "/icons/students.png", e -> {
                    mainSystem.showCRUDPage();
                    dispose();
                });

        // Academic Management Button
        addDashboardButton(centerPanel, "Academic Management", "Manage courses, grades, and academic progress",
                "/icons/academic.png", e -> {
                    JOptionPane.showMessageDialog(this, "Academic Management module coming soon!");
                });

        // Attendance Tracking Button
        addDashboardButton(centerPanel, "Attendance System", "Track and manage student attendance",
                "/icons/attendance.png", e -> {
                    JOptionPane.showMessageDialog(this, "Attendance System module coming soon!");
                });

        // Reports & Analytics Button
        addDashboardButton(centerPanel, "Reports & Analytics", "Generate reports and view analytics",
                "/icons/reports.png", e -> {
                    JOptionPane.showMessageDialog(this, "Reports & Analytics module coming soon!");
                });

        // Communication Center Button
        addDashboardButton(centerPanel, "Communication Center", "Send notifications and manage communications",
                "/icons/communication.png", e -> {
                    JOptionPane.showMessageDialog(this, "Communication Center module coming soon!");
                });

        // Settings Button
        addDashboardButton(centerPanel, "System Settings", "Configure system settings and preferences",
                "/icons/settings.png", e -> {
                    JOptionPane.showMessageDialog(this, "System Settings module coming soon!");
                });

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with status and logout
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Status panel on the left
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("System Status: Online");
        statusLabel.setForeground(new Color(0, 128, 0));
        statusPanel.add(statusLabel);
        bottomPanel.add(statusPanel, BorderLayout.WEST);

        // Logout button on the right
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        // Make sure the background color is shown
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        // Add padding to the button
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        // Add hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });
        logoutButton.addActionListener(e -> {
            mainSystem.showLoginPage();
            dispose();
        });
        logoutPanel.add(logoutButton);
        bottomPanel.add(logoutPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addDashboardButton(JPanel panel, String title, String tooltip, String iconPath,
            ActionListener action) {
        JButton button = new JButton(title);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(200, 100));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Create a panel for vertical layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Try to load and set icon if exists
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            // If icon loading fails, just show text
            System.err.println("Could not load icon: " + iconPath);
        }

        button.addActionListener(action);
        panel.add(button);
    }
}
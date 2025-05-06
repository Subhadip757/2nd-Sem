package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private StudentManagementSystem mainSystem;

    public LoginPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Split main panel into left (blue) and right (white) panels
        JPanel splitPanel = new JPanel(new GridLayout(1, 2));

        // LEFT PANEL (Blue, Welcome)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 123, 255));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome User!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel, BorderLayout.NORTH);

        // User icon
        JLabel userIcon = new JLabel("\uD83D\uDC64", SwingConstants.CENTER); // Unicode for user icon
        userIcon.setFont(new Font("Arial", Font.PLAIN, 140));
        userIcon.setForeground(Color.WHITE);
        leftPanel.add(userIcon, BorderLayout.CENTER);

        // RIGHT PANEL (White, Login Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // System title
        JLabel systemTitle = new JLabel("School Management System");
        systemTitle.setFont(new Font("Arial", Font.BOLD, 22));
        systemTitle.setForeground(new Color(52, 152, 219));
        systemTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(systemTitle);
        rightPanel.add(Box.createVerticalStrut(10));

        // Login Page label
        JLabel loginPageLabel = new JLabel("Login Page");
        loginPageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        loginPageLabel.setForeground(new Color(44, 62, 80));
        loginPageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(loginPageLabel);
        rightPanel.add(Box.createVerticalStrut(25));

        // Username
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        JLabel userIconLabel = new JLabel("\uD83D\uDC64");
        userIconLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        userPanel.add(userIconLabel);
        userPanel.add(Box.createHorizontalStrut(8));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        userPanel.add(usernameField);
        rightPanel.add(new JLabel("User name"));
        rightPanel.add(userPanel);
        rightPanel.add(Box.createVerticalStrut(15));

        // Password
        JPanel passPanel = new JPanel();
        passPanel.setOpaque(false);
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.X_AXIS));
        JLabel passIconLabel = new JLabel("\uD83D\uDD12");
        passIconLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        passPanel.add(passIconLabel);
        passPanel.add(Box.createHorizontalStrut(8));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        passPanel.add(passwordField);
        rightPanel.add(new JLabel("Password"));
        rightPanel.add(passPanel);
        rightPanel.add(Box.createVerticalStrut(15));

        // User Type
        JPanel typePanel = new JPanel();
        typePanel.setOpaque(false);
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
        JLabel gearIconLabel = new JLabel("\u2699");
        gearIconLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        typePanel.add(gearIconLabel);
        typePanel.add(Box.createHorizontalStrut(8));
        String[] userTypes = { "Admin", "Teacher" };
        JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);
        userTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        userTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        typePanel.add(userTypeCombo);
        rightPanel.add(new JLabel("User Type"));
        rightPanel.add(typePanel);
        rightPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        loginButton = createStyledButton("SUBMIT", new Color(52, 152, 219));
        exitButton = createStyledButton("CLOSE", new Color(231, 76, 60));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        rightPanel.add(buttonPanel);

        // Add listeners
        loginButton.addActionListener(e -> handleLoginCombo(userTypeCombo));
        exitButton.addActionListener(e -> System.exit(0));
        getRootPane().setDefaultButton(loginButton);

        // Add panels to splitPanel
        splitPanel.add(leftPanel);
        splitPanel.add(rightPanel);
        setContentPane(splitPanel);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    // New login handler for combo box
    private void handleLoginCombo(JComboBox<String> userTypeCombo) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        boolean isValid = false;
        if ("Admin".equals(userType)) {
            isValid = AdminCredentials.validateCredentials(username, password);
            if (isValid) {
                mainSystem.showDashboard();
                dispose();
            }
        }
        if (!isValid) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            // Shake the window for invalid login
            new Thread(() -> {
                Point originalLocation = getLocation();
                try {
                    for (int i = 0; i < 5; i++) {
                        setLocation(originalLocation.x + 5, originalLocation.y);
                        Thread.sleep(50);
                        setLocation(originalLocation.x - 5, originalLocation.y);
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setLocation(originalLocation);
            }).start();
        }
    }
}
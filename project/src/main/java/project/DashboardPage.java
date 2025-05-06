package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Arrays;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;
import java.awt.event.MouseAdapter;

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
    private CalendarEventDAO eventDAO;
    private MongoCollection<Document> studentsCollection;
    private List<CalendarEvent> upcomingEvents;
    private JPanel eventsPanel;

    public DashboardPage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        this.dataManager = new StudentDataManager();
        this.eventDAO = new CalendarEventDAO();
        this.studentsCollection = MongoDBConnection.getInstance().getDatabase().getCollection("students");
        this.upcomingEvents = new ArrayList<>();
        initializeUI();
        createTestEvents();
        fetchDashboardData();
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

        // Add navigation buttons
        addSidebarItem(sidebar, "📊 Dashboard", e -> {
            // Current page, no action needed
        }, true);
        addSidebarItem(sidebar, "👥 Student Records", e -> {
            mainSystem.showCRUDPage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "📚 Courses", e -> {
            mainSystem.showCoursePage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "📅 Calendar", e -> {
            mainSystem.showCalendarPage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "📝 Marks", e -> {
            mainSystem.showMarksPage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "📊 Results", e -> {
            mainSystem.showResultPage();
            dispose();
        }, false);
        addSidebarItem(sidebar, "🗓️ Attendance", e -> {
            mainSystem.showAttendancePage();
            dispose();
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

        // Welcome Section with stats
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

        // Add stats cards
        JPanel totalStudentsCard = createStatCard("👥 Total Students", "0", "Total number of enrolled students",
                ACCENT_COLOR);
        JPanel averageGPACard = createStatCard("📊 Average GPA", "0.00", "Average GPA across all students",
                PRIMARY_COLOR);
        statsPanel.add(totalStudentsCard);
        statsPanel.add(averageGPACard);

        welcomeSection.add(statsPanel, BorderLayout.CENTER);
        mainContent.add(welcomeSection, BorderLayout.NORTH);

        // Calendar Events Section
        JPanel calendarSection = createCalendarEventsSection();
        mainContent.add(calendarSection, BorderLayout.CENTER);

        // Fetch data after UI is created
        SwingUtilities.invokeLater(() -> {
            fetchDashboardData();
        });

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

        // Store references to labels based on the title
        if (title.contains("Total Students")) {
            totalStudentsLabel = valueLabel;
        } else if (title.contains("Average GPA")) {
            averageGPALabel = valueLabel;
        }

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

    private void fetchDashboardData() {
        fetchStudentStatistics();
        fetchUpcomingEvents();
    }

    private void fetchStudentStatistics() {
        try {
            // Get total students count
            long totalStudents = studentsCollection.countDocuments();

            // Calculate average CGPA using MongoDB aggregation
            Document result = studentsCollection.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("gpa", new Document("$exists", true))),
                    new Document("$group",
                            new Document("_id", null)
                                    .append("averageCGPA", new Document("$avg", "$gpa")))))
                    .first();

            // Create final variables for use in lambda
            final long finalTotalStudents = totalStudents;
            final double avgCGPA = (result != null && result.get("averageCGPA") != null)
                    ? result.getDouble("averageCGPA")
                    : 0.0;

            // Update the labels
            SwingUtilities.invokeLater(() -> {
                if (totalStudentsLabel != null) {
                    totalStudentsLabel.setText(String.valueOf(finalTotalStudents));
                }
                if (averageGPALabel != null) {
                    averageGPALabel.setText(String.format("%.2f", avgCGPA));
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching student statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fetchUpcomingEvents() {
        try {
            System.out.println("Starting to fetch upcoming events...");
            // Clear existing events
            upcomingEvents.clear();

            // Fetch fresh data from database
            List<CalendarEvent> allEvents = eventDAO.getAllEvents();
            System.out.println("Total events fetched from database: " + allEvents.size());

            // Debug: Print all events
            System.out.println("All events from database:");
            allEvents.forEach(event -> {
                System.out.println("Event: " + event.getTitle() +
                        ", Date: " + event.getStartDateTime() +
                        ", Type: " + event.getEventType());
            });

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime weekLater = now.plusDays(7);

            upcomingEvents = allEvents.stream()
                    .filter(event -> {
                        LocalDateTime eventDate = event.getStartDateTime();
                        boolean isUpcoming = !eventDate.isBefore(now) && !eventDate.isAfter(weekLater);
                        System.out.println("Event: " + event.getTitle() +
                                " is upcoming: " + isUpcoming +
                                " (Date: " + eventDate + ")");
                        return isUpcoming;
                    })
                    .sorted((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()))
                    .limit(5)
                    .collect(Collectors.toList());

            System.out.println("Filtered upcoming events count: " + upcomingEvents.size());
            System.out.println("Upcoming events:");
            upcomingEvents.forEach(event -> {
                System.out.println("- " + event.getTitle() +
                        " on " + event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            });

            // Ensure we update the UI on the EDT
            SwingUtilities.invokeLater(() -> {
                if (eventsPanel != null) {
                    eventsPanel.removeAll();
                    if (upcomingEvents.isEmpty()) {
                        JLabel noEventsLabel = new JLabel("No upcoming events");
                        noEventsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                        noEventsLabel.setForeground(TEXT_SECONDARY);
                        eventsPanel.add(noEventsLabel);
                    } else {
                        for (CalendarEvent event : upcomingEvents) {
                            addEventCard(eventsPanel, event);
                        }
                    }
                    eventsPanel.revalidate();
                    eventsPanel.repaint();
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching calendar events: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addEventCard(JPanel container, CalendarEvent event) {
        JPanel eventCard = new JPanel(new BorderLayout(15, 10));
        eventCard.setBackground(CARD_COLOR);
        eventCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        // Add hover effect
        eventCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                eventCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                eventCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                        BorderFactory.createEmptyBorder(15, 20, 15, 20)));
            }
        });

        // Event details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_COLOR);

        // Event title with icon based on type
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(CARD_COLOR);
        String eventIcon = getEventTypeIcon(event.getEventType());
        JLabel iconLabel = new JLabel(eventIcon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        JLabel titleLabel = new JLabel(event.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Event date and time with better formatting
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        dateTimePanel.setBackground(CARD_COLOR);

        JLabel dateLabel = new JLabel("📅 " + event.getStartDateTime().format(dateFormatter));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_SECONDARY);

        JLabel timeLabel = new JLabel("🕒 " + event.getStartDateTime().format(timeFormatter));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(TEXT_SECONDARY);

        dateTimePanel.add(dateLabel);
        dateTimePanel.add(timeLabel);

        // Event type badge with improved styling
        JLabel typeLabel = new JLabel(event.getEventType());
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(CARD_COLOR);
        typeLabel.setBackground(getEventTypeColor(event.getEventType()));
        typeLabel.setOpaque(true);
        typeLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getEventTypeColor(event.getEventType()).darker(), 1, true),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));

        detailsPanel.add(titlePanel);
        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(dateTimePanel);

        eventCard.add(detailsPanel, BorderLayout.CENTER);
        eventCard.add(typeLabel, BorderLayout.EAST);

        container.add(eventCard);
        container.add(Box.createVerticalStrut(10));
    }

    private String getEventTypeIcon(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EXAM":
                return "📝";
            case "SUBMISSION":
                return "📚";
            case "MEETING":
                return "👥";
            default:
                return "📅";
        }
    }

    private Color getEventTypeColor(String eventType) {
        switch (eventType.toUpperCase()) {
            case "EXAM":
                return new Color(220, 38, 38); // Red
            case "SUBMISSION":
                return new Color(37, 99, 235); // Blue
            case "MEETING":
                return new Color(5, 150, 105); // Green
            default:
                return new Color(107, 114, 128); // Gray
        }
    }

    private JPanel createCalendarEventsSection() {
        JPanel calendarSection = new JPanel(new BorderLayout(0, 15));
        calendarSection.setBackground(BACKGROUND_COLOR);
        calendarSection.setName("calendarSection");

        // Calendar header with refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Left side: Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel calendarIcon = new JLabel("📅");
        calendarIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        JLabel calendarTitle = new JLabel("Upcoming Events");
        calendarTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        calendarTitle.setForeground(TEXT_PRIMARY);
        titlePanel.add(calendarIcon);
        titlePanel.add(calendarTitle);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Right side: Refresh button with hover effect
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setForeground(PRIMARY_COLOR);
        refreshButton.setBackground(CARD_COLOR);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            refreshButton.setEnabled(false);
            refreshButton.setText("🔄 Refreshing...");
            fetchDashboardData();
            Timer timer = new Timer(1000, evt -> {
                refreshButton.setEnabled(true);
                refreshButton.setText("🔄 Refresh");
            });
            timer.setRepeats(false);
            timer.start();
        });
        headerPanel.add(refreshButton, BorderLayout.EAST);

        calendarSection.add(headerPanel, BorderLayout.NORTH);

        // Events panel with custom background
        eventsPanel = new JPanel();
        eventsPanel.setName("eventsPanel");
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(BACKGROUND_COLOR);
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setName("eventsScrollPane");
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Custom scrollbar styling
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(BACKGROUND_COLOR);
        verticalScrollBar.setForeground(PRIMARY_COLOR);
        verticalScrollBar.setPreferredSize(new Dimension(10, 0));

        calendarSection.add(scrollPane, BorderLayout.CENTER);

        return calendarSection;
    }

    private void createTestEvents() {
        try {
            // Check if there are any existing events
            List<CalendarEvent> existingEvents = eventDAO.getAllEvents();
            if (!existingEvents.isEmpty()) {
                System.out.println("Events already exist in database, skipping test event creation");
                return;
            }

            System.out.println("No events found in database, creating test events...");
            // Create some test events
            LocalDateTime now = LocalDateTime.now();

            // Event 1: Today
            CalendarEvent event1 = new CalendarEvent(
                    "Math Exam",
                    "Final examination for Mathematics",
                    now.plusHours(2),
                    now.plusHours(4),
                    "EXAM",
                    null);

            // Event 2: Tomorrow
            CalendarEvent event2 = new CalendarEvent(
                    "Project Submission",
                    "Deadline for Software Engineering project",
                    now.plusDays(1).withHour(14).withMinute(0),
                    now.plusDays(1).withHour(16).withMinute(0),
                    "SUBMISSION",
                    null);

            // Event 3: Day after tomorrow
            CalendarEvent event3 = new CalendarEvent(
                    "Faculty Meeting",
                    "Monthly faculty meeting",
                    now.plusDays(2).withHour(10).withMinute(0),
                    now.plusDays(2).withHour(11).withMinute(30),
                    "MEETING",
                    null);

            // Save events to database
            eventDAO.save(event1);
            eventDAO.save(event2);
            eventDAO.save(event3);

            System.out.println("Test events created successfully");
        } catch (Exception e) {
            System.err.println("Error creating test events: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add method to refresh dashboard data
    public void refreshDashboard() {
        fetchDashboardData();
    }
}
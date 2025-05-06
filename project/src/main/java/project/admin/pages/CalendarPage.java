package project.admin.pages;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.border.*;
import java.awt.event.*;

public class CalendarPage extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(47, 128, 237);
    private static final Color BACKGROUND_COLOR = new Color(247, 250, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = Color.BLACK;

    private StudentManagementSystem mainSystem;
    private LocalDate currentDate;
    private JLabel monthYearLabel;
    private JPanel calendarPanel;
    private JTable eventsTable;
    private DefaultTableModel eventsTableModel;
    private List<CalendarEvent> events;
    private JPopupMenu dayPopupMenu;
    private LocalDate selectedDate;
    private CalendarEventDAO eventDAO;

    public CalendarPage(StudentManagementSystem mainSystem) {
        try {
            this.mainSystem = mainSystem;
            this.currentDate = LocalDate.now();
            this.events = new ArrayList<>();
            this.eventDAO = new CalendarEventDAO();
            initializeUI();
            initializeEventsTable();
            loadEvents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error initializing calendar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            // After showing error, ensure the window can still be closed
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);
        }
    }

    private void initializeUI() {
        setTitle("Academic Calendar");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel with Navigation
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Split Panel for Calendar and Events
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setBackground(BACKGROUND_COLOR);

        // Left side - Calendar
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(CARD_COLOR);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        calendarPanel.setBackground(CARD_COLOR);
        updateCalendar();
        leftPanel.add(calendarPanel, BorderLayout.CENTER);

        // Right side - Events List and Add Event
        JPanel rightPanel = createEventsPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom Panel with Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        JButton backButton = new JButton("Back to Dashboard");
        styleButton(backButton, PRIMARY_COLOR);
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });
        bottomPanel.add(backButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Month/Year Label and Navigation
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navigationPanel.setBackground(CARD_COLOR);

        JButton prevMonth = new JButton("←");
        styleButton(prevMonth, PRIMARY_COLOR);
        prevMonth.addActionListener(e -> changeMonth(-1));

        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        monthYearLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton nextMonth = new JButton("→");
        styleButton(nextMonth, PRIMARY_COLOR);
        nextMonth.addActionListener(e -> changeMonth(1));

        navigationPanel.add(prevMonth);
        navigationPanel.add(monthYearLabel);
        navigationPanel.add(nextMonth);

        panel.add(navigationPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Events Table
        String[] columns = { "Date", "Event", "Type" };
        eventsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(eventsTableModel);
        eventsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventsTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(eventsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add Event Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_COLOR);
        JButton addEventButton = new JButton("Add New Event");
        styleButton(addEventButton, PRIMARY_COLOR);
        addEventButton.addActionListener(e -> showAddEventDialog(currentDate));
        buttonPanel.add(addEventButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // Add day labels
        String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(PRIMARY_COLOR);
            calendarPanel.add(label);
        }

        // Get first day of month and total days
        YearMonth yearMonth = YearMonth.from(currentDate);
        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = yearMonth.lengthOfMonth();

        // Update month/year label
        monthYearLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        // Add empty labels for days before start of month
        for (int i = 0; i < dayOfWeek; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(CARD_COLOR);
            calendarPanel.add(emptyPanel);
        }

        // Add day buttons with hover effect and click handling
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBackground(CARD_COLOR);
            dayPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

            JButton dayButton = createDayButton(day);
            dayPanel.add(dayButton, BorderLayout.CENTER);

            // Add event indicator if events exist on this day
            LocalDate date = currentDate.withDayOfMonth(day);
            if (hasEvents(date)) {
                JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
                indicatorPanel.setBackground(CARD_COLOR);
                JLabel indicator = new JLabel("•");
                indicator.setForeground(PRIMARY_COLOR);
                indicatorPanel.add(indicator);
                dayPanel.add(indicatorPanel, BorderLayout.SOUTH);
            }

            calendarPanel.add(dayPanel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JButton createDayButton(int day) {
        JButton dayButton = new JButton(String.valueOf(day));
        dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dayButton.setBorderPainted(false);
        dayButton.setContentAreaFilled(false);
        dayButton.setFocusPainted(false);
        dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Style for current day
        if (day == currentDate.getDayOfMonth() &&
                currentDate.getMonth() == LocalDate.now().getMonth() &&
                currentDate.getYear() == LocalDate.now().getYear()) {
            dayButton.setForeground(Color.WHITE);
            dayButton.setBackground(PRIMARY_COLOR);
            dayButton.setOpaque(true);
        } else {
            dayButton.setForeground(TEXT_PRIMARY);
        }

        // Hover effect
        dayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!dayButton.isOpaque()) {
                    dayButton.setContentAreaFilled(true);
                    dayButton.setBackground(new Color(237, 242, 247));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!dayButton.isOpaque()) {
                    dayButton.setContentAreaFilled(false);
                }
            }
        });

        // Click handler
        dayButton.addActionListener(e -> {
            LocalDate selectedDate = currentDate.withDayOfMonth(day);
            showAddEventDialog(selectedDate);
        });

        return dayButton;
    }

    private void changeMonth(int delta) {
        currentDate = currentDate.plusMonths(delta);
        updateCalendar();
        loadEvents();
    }

    private boolean hasEvents(LocalDate date) {
        return events.stream().anyMatch(event -> event.getStartDateTime().toLocalDate().equals(date));
    }

    private void showEventsForDay(int day) {
        LocalDate selectedDate = currentDate.withDayOfMonth(day);
        eventsTableModel.setRowCount(0);

        events.stream()
                .filter(event -> event.getStartDateTime().toLocalDate().equals(selectedDate))
                .forEach(event -> {
                    eventsTableModel.addRow(new Object[] {
                            event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            event.getTitle(),
                            event.getEventType()
                    });
                });
    }

    private void showAddEventDialog(LocalDate date) {
        JDialog dialog = new JDialog(this, "Add New Event", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Selected Date Display
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel dateLabel = new JLabel("Selected Date:");
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        JLabel selectedDateLabel = new JLabel(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        selectedDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectedDateLabel.setForeground(PRIMARY_COLOR);
        formPanel.add(selectedDateLabel, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JTextField titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JTextArea descField = new JTextArea(3, 20);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descField);
        formPanel.add(scrollPane, gbc);

        // Date selection
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField dateField = new JTextField(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateField.setEditable(false);
        formPanel.add(dateField, gbc);

        // Time selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Start Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JPanel startTimePanel = createTimeSelectionPanel();
        formPanel.add(startTimePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("End Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JPanel endTimePanel = createTimeSelectionPanel();
        formPanel.add(endTimePanel, gbc);

        // Event Type
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Event Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "EXAM", "SUBMISSION", "MEETING", "OTHER" });
        formPanel.add(typeCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save");

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                LocalTime startTime = getTimeFromPanel((JPanel) startTimePanel);
                LocalTime endTime = getTimeFromPanel((JPanel) endTimePanel);

                if (validateEventInput(titleField.getText(), date, startTime, endTime)) {
                    LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                    LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

                    CalendarEvent event = new CalendarEvent(
                            titleField.getText(),
                            descField.getText(),
                            startDateTime,
                            endDateTime,
                            (String) typeCombo.getSelectedItem(),
                            null // courseId is null for now
                    );

                    addEvent(event);
                    dialog.dispose();
                    updateCalendar();
                    loadEvents();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid times.");
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createTimeSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Hour spinner (12-hour format)
        SpinnerModel hourModel = new SpinnerNumberModel(12, 1, 12, 1);
        JSpinner hourSpinner = new JSpinner(hourModel);
        ((JSpinner.DefaultEditor) hourSpinner.getEditor()).getTextField().setColumns(2);

        // Minute spinner
        SpinnerModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner minuteSpinner = new JSpinner(minuteModel);
        ((JSpinner.DefaultEditor) minuteSpinner.getEditor()).getTextField().setColumns(2);

        // AM/PM selector
        String[] ampm = { "AM", "PM" };
        JComboBox<String> ampmCombo = new JComboBox<>(ampm);
        ampmCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(hourSpinner);
        panel.add(new JLabel(":"));
        panel.add(minuteSpinner);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(ampmCombo);

        return panel;
    }

    private LocalTime getTimeFromPanel(JPanel timePanel) {
        JSpinner hourSpinner = null;
        JSpinner minuteSpinner = null;
        JComboBox<String> ampmCombo = null;

        for (Component c : timePanel.getComponents()) {
            if (c instanceof JSpinner) {
                if (hourSpinner == null) {
                    hourSpinner = (JSpinner) c;
                } else {
                    minuteSpinner = (JSpinner) c;
                }
            } else if (c instanceof JComboBox) {
                ampmCombo = (JComboBox<String>) c;
            }
        }

        if (hourSpinner != null && minuteSpinner != null && ampmCombo != null) {
            int hour = (Integer) hourSpinner.getValue();
            int minute = (Integer) minuteSpinner.getValue();
            String ampm = (String) ampmCombo.getSelectedItem();

            // Convert to 24-hour format
            if (ampm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (ampm.equals("AM") && hour == 12) {
                hour = 0;
            }

            return LocalTime.of(hour, minute);
        }

        throw new IllegalStateException("Invalid time panel configuration");
    }

    private boolean validateEventInput(String title, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title for the event.");
            return false;
        }
        if (date == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.");
            return false;
        }
        if (startTime == null || endTime == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end times.");
            return false;
        }
        if (endTime.isBefore(startTime)) {
            JOptionPane.showMessageDialog(this, "End time cannot be before start time.");
            return false;
        }
        return true;
    }

    private void addEvent(CalendarEvent event) {
        try {
            eventDAO.save(event);
            events.add(event);
            updateCalendar();
            loadEvents();
            updateDashboard();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving event: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEvents() {
        try {
            events = eventDAO.getAllEvents();
            eventsTableModel.setRowCount(0);

            for (CalendarEvent event : events) {
                if (event.getStartDateTime().getMonth() == currentDate.getMonth() &&
                        event.getStartDateTime().getYear() == currentDate.getYear()) {
                    eventsTableModel.addRow(new Object[] {
                            event.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                            event.getTitle(),
                            event.getEventType()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showDayPopupMenu(Component component, int x, int y) {
        if (dayPopupMenu == null) {
            dayPopupMenu = new JPopupMenu();
            JMenuItem addEventItem = new JMenuItem("Add Event");
            addEventItem.addActionListener(e -> showAddEventDialog(selectedDate));
            dayPopupMenu.add(addEventItem);
        }
        dayPopupMenu.show(component, x, y);
    }

    private void showEventDetails(CalendarEvent event) {
        JDialog dialog = new JDialog(this, "Event Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.add(new JLabel("Title: " + event.getTitle()));
        detailsPanel.add(new JLabel("Description: " + event.getDescription()));
        detailsPanel
                .add(new JLabel("Date: " + event.getStartDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        detailsPanel.add(new JLabel("Time: " + event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + " - " + event.getEndDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
        detailsPanel.add(new JLabel("Type: " + event.getEventType()));

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton closeButton = new JButton("Close");

        editButton.addActionListener(e -> {
            dialog.dispose();
            showEditEventDialog(event);
        });

        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to delete this event?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteEvent(event);
                dialog.dispose();
                updateCalendar();
                loadEvents();
            }
        });

        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showEditEventDialog(CalendarEvent event) {
        JDialog dialog = new JDialog(this, "Edit Event", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JTextField titleField = new JTextField(event.getTitle(), 20);
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JTextArea descField = new JTextArea(event.getDescription(), 3, 20);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descField);
        formPanel.add(scrollPane, gbc);

        // Date display
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField dateField = new JTextField(
                event.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dateField.setEditable(false);
        formPanel.add(dateField, gbc);

        // Time selection
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Start Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JPanel startTimePanel = createTimeSelectionPanel();
        setTimeInPanel(startTimePanel, event.getStartDateTime().toLocalTime());
        formPanel.add(startTimePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("End Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        JPanel endTimePanel = createTimeSelectionPanel();
        setTimeInPanel(endTimePanel, event.getEndDateTime().toLocalTime());
        formPanel.add(endTimePanel, gbc);

        // Event Type
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Event Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "EXAM", "SUBMISSION", "MEETING", "OTHER" });
        typeCombo.setSelectedItem(event.getEventType());
        formPanel.add(typeCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save");

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            try {
                LocalTime startTime = getTimeFromPanel((JPanel) startTimePanel);
                LocalTime endTime = getTimeFromPanel((JPanel) endTimePanel);
                LocalDate date = event.getStartDateTime().toLocalDate();

                if (validateEventInput(titleField.getText(), date, startTime, endTime)) {
                    LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
                    LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

                    CalendarEvent updatedEvent = new CalendarEvent(
                            event.getId(),
                            titleField.getText(),
                            descField.getText(),
                            startDateTime,
                            endDateTime,
                            (String) typeCombo.getSelectedItem(),
                            event.getCourseId());

                    updateEvent(updatedEvent);
                    dialog.dispose();
                    updateCalendar();
                    loadEvents();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating event: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void deleteEvent(CalendarEvent event) {
        try {
            eventDAO.delete(event.getId());
            events.remove(event);
            updateCalendar();
            updateDashboard();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting event: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEvent(CalendarEvent event) {
        try {
            eventDAO.update(event);
            events.removeIf(e -> e.getId().equals(event.getId()));
            events.add(event);
            updateCalendar();
            updateDashboard();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating event: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDashboard() {
        try {
            if (mainSystem != null) {
                mainSystem.showDashboard();
            }
        } catch (Exception e) {
            System.err.println("Error updating dashboard: " + e.getMessage());
        }
    }

    private void initializeEventsTable() {
        eventsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int row = eventsTable.getSelectedRow();
                    if (row >= 0) {
                        String title = (String) eventsTableModel.getValueAt(row, 1);
                        // Find the event by title and date
                        CalendarEvent selectedEvent = events.stream()
                                .filter(event -> event.getTitle().equals(title))
                                .findFirst()
                                .orElse(null);

                        if (selectedEvent != null) {
                            showEventDetails(selectedEvent);
                        }
                    }
                }
            }
        });
    }

    private void setTimeInPanel(JPanel timePanel, LocalTime time) {
        for (Component c : timePanel.getComponents()) {
            if (c instanceof JSpinner) {
                JSpinner spinner = (JSpinner) c;
                if (spinner.getModel() instanceof SpinnerNumberModel) {
                    SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
                    if (model.getMaximum().equals(12)) { // Hour spinner
                        int hour = time.getHour() % 12;
                        if (hour == 0)
                            hour = 12;
                        spinner.setValue(hour);
                    } else { // Minute spinner
                        spinner.setValue(time.getMinute());
                    }
                }
            } else if (c instanceof JComboBox) {
                @SuppressWarnings("unchecked")
                JComboBox<String> ampmCombo = (JComboBox<String>) c;
                ampmCombo.setSelectedItem(time.getHour() >= 12 ? "PM" : "AM");
            }
        }
    }
}
package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AttendancePage extends JFrame {
    private StudentManagementSystem mainSystem;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public AttendancePage(StudentManagementSystem mainSystem) {
        this.mainSystem = mainSystem;
        setTitle("Student Attendance");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadAttendanceData();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(244, 246, 251));

        // Header panel with icon and title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(47, 128, 237));
        JLabel iconLabel = new JLabel("\uD83D\uDCC5"); // Calendar emoji
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        JLabel titleLabel = new JLabel("  Student Attendance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Name", "Course", "Attendance (%)" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        attendanceTable = new JTable(tableModel) {
            // Alternate row color
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(232, 240, 254));
                } else {
                    c.setBackground(new Color(186, 230, 253));
                }
                return c;
            }
        };
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        attendanceTable.setRowHeight(60);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        attendanceTable.getTableHeader().setBackground(new Color(52, 58, 64));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));

        // Improved attendance circle renderer
        attendanceTable.getColumnModel().getColumn(3).setCellRenderer(new AttendanceCircleRenderer());

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(new Color(244, 246, 251));

        JButton backButton = createStyledButton("Back to Dashboard", new Color(108, 117, 125), "\u2B05");
        backButton.addActionListener(e -> {
            mainSystem.showDashboard();
            dispose();
        });

        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 18, 8, 18));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void loadAttendanceData() {
        List<Student> students = mainSystem.getDataManager().getAllStudents();
        tableModel.setRowCount(0);
        for (Student student : students) {
            String courseName = (student.getCourse() != null) ? student.getCourse().getCourseName() : "N/A";
            // For demo, generate random attendance between 60 and 100
            int attendance = 60 + (int) (Math.random() * 41);
            tableModel.addRow(new Object[] {
                    student.getId(),
                    student.getName(),
                    courseName,
                    attendance
            });
        }
    }

    // Improved circle renderer with better centering and text
    private static class AttendanceCircleRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            int percent = 0;
            try {
                percent = Integer.parseInt(value.toString());
            } catch (Exception ignored) {
            }
            Color color;
            if (percent > 75) {
                color = new Color(40, 167, 69); // Green
            } else if (percent > 65) {
                color = new Color(255, 193, 7); // Yellow
            } else {
                color = new Color(220, 53, 69); // Red
            }
            return new CirclePanel(percent, color, isSelected);
        }
    }

    // Custom panel for attendance circle
    private static class CirclePanel extends JPanel {
        private final int percent;
        private final Color color;
        private final boolean selected;

        public CirclePanel(int percent, Color color, boolean selected) {
            this.percent = percent;
            this.color = color;
            this.selected = selected;
            setOpaque(false);
            setPreferredSize(new Dimension(100, 50));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int size = 42;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3)); // Thicker outline
            g2.setColor(selected ? color.darker() : color);
            g2.drawOval(x, y, size, size);
            // Draw the percentage text in black
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            String text = percent + "%";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            g2.drawString(text, x + (size - textWidth) / 2, y + (size + textHeight) / 2 - 3);
        }
    }
}

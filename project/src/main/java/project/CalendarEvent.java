package project;

import java.time.LocalDateTime;

public class CalendarEvent {
    private String id;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String eventType; // EXAM, SUBMISSION, MEETING, OTHER
    private String courseId; // Optional, can be null for general events

    // Constructor with ID
    public CalendarEvent(String id, String title, String description, LocalDateTime startDateTime,
            LocalDateTime endDateTime, String eventType, String courseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.eventType = eventType;
        this.courseId = courseId;
    }

    // Constructor without ID (for new events)
    public CalendarEvent(String title, String description, LocalDateTime startDateTime,
            LocalDateTime endDateTime, String eventType, String courseId) {
        this.id = java.util.UUID.randomUUID().toString(); // Generate a new ID
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.eventType = eventType;
        this.courseId = courseId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCourseId() {
        return courseId;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", eventType='" + eventType + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
package project;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDAO {
    private final MongoCollection<Document> collection;

    public CalendarEventDAO() {
        this.collection = MongoDBConnection.getInstance().getDatabase().getCollection("calendar_events");
    }

    public void save(CalendarEvent event) {
        try {
            Document doc = new Document()
                    .append("_id", event.getId())
                    .append("title", event.getTitle())
                    .append("description", event.getDescription())
                    .append("startDateTime", event.getStartDateTime().toString())
                    .append("endDateTime", event.getEndDateTime().toString())
                    .append("eventType", event.getEventType())
                    .append("courseId", event.getCourseId());

            collection.insertOne(doc);
            System.out.println("Successfully saved event: " + event.getTitle());
        } catch (Exception e) {
            System.err.println("Error saving event to MongoDB: " + e.getMessage());
            throw e;
        }
    }

    public void update(CalendarEvent event) {
        Document doc = new Document()
                .append("title", event.getTitle())
                .append("description", event.getDescription())
                .append("startDateTime", event.getStartDateTime().toString())
                .append("endDateTime", event.getEndDateTime().toString())
                .append("eventType", event.getEventType())
                .append("courseId", event.getCourseId());
        collection.updateOne(Filters.eq("_id", event.getId()), new Document("$set", doc));
    }

    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public List<CalendarEvent> getAllEvents() {
        List<CalendarEvent> events = new ArrayList<>();
        try {
            System.out.println("Attempting to fetch events from MongoDB...");
            for (Document doc : collection.find()) {
                try {
                    CalendarEvent event = documentToEvent(doc);
                    events.add(event);
                    System.out.println("Successfully loaded event: " + event.getTitle());
                } catch (Exception e) {
                    System.err.println("Error parsing event document: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Total events loaded from MongoDB: " + events.size());
        } catch (Exception e) {
            System.err.println("Error accessing MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    private CalendarEvent documentToEvent(Document doc) {
        try {
            String id = doc.getString("_id");
            String title = doc.getString("title");
            String description = doc.getString("description");
            LocalDateTime startDateTime = LocalDateTime.parse(doc.getString("startDateTime"));
            LocalDateTime endDateTime = LocalDateTime.parse(doc.getString("endDateTime"));
            String eventType = doc.getString("eventType");
            String courseId = doc.getString("courseId");

            return new CalendarEvent(
                    id, title, description, startDateTime, endDateTime, eventType, courseId);
        } catch (Exception e) {
            System.err.println("Error converting document to event: " + e.getMessage());
            throw e;
        }
    }
}
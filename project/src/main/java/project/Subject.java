package project;

public class Subject {
    private String name;
    private int maxMarks;
    private String courseId;

    public static final String[] CS_SUBJECTS = {
            "Programming Fundamentals",
            "Data Structures",
            "Algorithms",
            "Database Management",
            "Operating Systems",
            "Computer Networks",
            "Software Engineering",
            "Web Development"
    };

    public Subject(String name, int maxMarks, String courseId) {
        this.name = name;
        this.maxMarks = maxMarks;
        this.courseId = courseId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(int maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
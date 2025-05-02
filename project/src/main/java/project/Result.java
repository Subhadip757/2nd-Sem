package project;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private String studentId;
    private String courseId;
    private Map<String, Integer> subjectMarks;
    private double percentage;
    private String grade;
    private double gpa;

    public Result(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.subjectMarks = new HashMap<>();
    }

    public void addSubjectMark(String subject, int marks) {
        subjectMarks.put(subject, marks);
        calculatePercentageAndGrade();
    }

    private void calculatePercentageAndGrade() {
        if (subjectMarks.isEmpty()) {
            percentage = 0.0;
            grade = "N/A";
            gpa = 0.0;
            return;
        }

        int totalMarks = subjectMarks.values().stream().mapToInt(Integer::intValue).sum();
        int maxPossibleMarks = subjectMarks.size() * 100; // Assuming each subject has max marks of 100
        percentage = (totalMarks * 100.0) / maxPossibleMarks;

        // Grade and GPA calculation
        if (percentage >= 90) {
            grade = "A+";
            gpa = 10.0;
        } else if (percentage >= 80) {
            grade = "A";
            gpa = 9.0;
        } else if (percentage >= 70) {
            grade = "B";
            gpa = 8.0;
        } else if (percentage >= 60) {
            grade = "C";
            gpa = 7.0;
        } else if (percentage >= 50) {
            grade = "D";
            gpa = 6.0;
        } else {
            grade = "F";
            gpa = 0.0;
        }
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public Map<String, Integer> getSubjectMarks() {
        return subjectMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGrade() {
        return grade;
    }

    public double getGPA() {
        return gpa;
    }
}
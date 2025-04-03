class Student {
    private String name;
    private int age;
    private String course;

    // Constructor
    public Student(String name, int age, String course) {
        this.name = name;
        this.age = age;
        this.course = course;
    }

    // Custom toString() method
    @Override
    public String toString() {
        return "Student Details: { " +
                "Name: '" + name + '\'' +
                ", Age: " + age +
                ", Course: '" + course + '\'' +
                " }";
    }
}

public class Main {
    public static void main(String[] args) {
        Student student1 = new Student("Subhadip Dey", 22, "MCA");
        Student student2 = new Student("John Doe", 25, "B.Tech");

        // Printing the object directly
        System.out.println(student1); // Calls the overridden toString() method
        System.out.println(student2);
    }
}
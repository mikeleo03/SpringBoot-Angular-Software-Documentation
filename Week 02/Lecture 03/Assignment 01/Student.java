import java.util.ArrayList;
import java.util.List;

public class Student {
    // States, which defined as the attribute
    private String name;
    private int age;
    private List<Subject> subjects;

    // Constructor
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.subjects = new ArrayList<>();
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Adds a new subject to the student's list of subjects.
     *
     * @param subject the subject to be added
     */
    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    /**
     * This method is used to simulate a student learning subjects.
     * If the student has no subjects, it prints a message indicating that.
     * If the student has subjects, it prints the names of the subjects the student is learning.
     */
    public void learn() {
        // Check if the student has any subjects
        if (subjects.isEmpty()) {
            // Print a message indicating that the student is not learning any subjects
            System.out.println("Student " + name + " is not learning any subjects.");
        } else {
            // Print a message indicating the subjects the student is learning
            System.out.print("Student " + name + " is learning: \n");
            // Loop through the subjects and print their names
            for (Subject subject : subjects) {
                System.out.print("- " + subject.getName() + "\n");
            }
            // Print a new line after printing all the subject names
            System.out.println();
        }
    }
}

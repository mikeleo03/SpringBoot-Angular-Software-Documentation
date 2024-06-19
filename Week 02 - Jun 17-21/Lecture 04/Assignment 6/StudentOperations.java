import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Student {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

public class StudentOperations {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
            new Student("Alice", 24),
            new Student("David", 21),
            new Student("Bob", 22),
            new Student("Charlie", 23),
            new Student("Frank", 19),
            new Student("Eve", 20)
        );

        // Sort students by name
        List<Student> sortedStudents = students.stream()
                                               .sorted(Comparator.comparing(Student::getName))
                                               .collect(Collectors.toList());

        System.out.println("Sorted Students:");
        sortedStudents.forEach(System.out::println);

        // Find student with max age
        Optional<Student> maxAgeStudent = students.stream()
                                                  .max(Comparator.comparing(Student::getAge));

        maxAgeStudent.ifPresent(student -> System.out.println("Student with max age: " + student));

        // Check if any student name matches a specific keyword
        String keyword = "Ali";
        boolean hasMatch = students.stream()
                                   .map(Student::getName)
                                   .anyMatch(name -> name.contains(keyword));

        System.out.println("Any student name matches the keyword '" + keyword + "': " + hasMatch);
    }
}
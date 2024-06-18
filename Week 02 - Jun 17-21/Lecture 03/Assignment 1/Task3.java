public class Task3 {
    // Main method for testing
    public static void main(String[] args) {
        Subject math = new Subject("Mathematics", 1);
        Subject biology = new Subject("Biology", 2);
        Subject english = new Subject("English", 3);
        Student student = new Student("Alice", 16);
        student.addSubject(math);
        student.addSubject(biology);
        student.addSubject(english);
        student.learn();
    }
}

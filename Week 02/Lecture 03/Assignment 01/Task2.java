public class Task2 {
    // Main method for testing
    public static void main(String[] args) {
        Subject math = new Subject("Mathematics", 1);
        Teacher teacher = new Teacher("Tam", 30);
        teacher.assignSubject(math);
        teacher.teach();
    }
}
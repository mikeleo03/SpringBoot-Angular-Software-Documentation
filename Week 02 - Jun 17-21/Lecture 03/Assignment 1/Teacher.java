public class Teacher {
    // States, which defined as the attribute
    private String name;
    private int age;
    private Subject subject;

    // Constructor
    public Teacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Assigns a subject to the teacher.
     *
     * @param subject the subject to be assigned
     */
    public void assignSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * Method to make the teacher teach.
     *
     * @return void
     * @throws Exception If the teacher is not assigned to any subject.
     */
    public void teach() {
        if (subject != null) {
            System.out.println("Teacher " + name + " is teaching " + subject.getName() + " for Class " + subject.getClassId() + ".");
        } else {
            System.out.println("Teacher " + name + " is not assigned to any subject.");
        }
    }
}
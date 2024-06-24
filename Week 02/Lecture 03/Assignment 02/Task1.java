interface FirstInterface {
    void firstMethod(String string);

    default void log(String string) {
        System.out.println("This method is FirstInterface implementation: " + string);
    }
}

interface SecondInterface {
    void secondMethod();

    default void log(String string) {
        System.out.println("This method is SecondInterface implementation: " + string);
    }
}

public class Task1 implements FirstInterface, SecondInterface {
    @Override
    public void firstMethod(String string) {
        System.out.println("First method: " + string);
    }

    @Override
    public void secondMethod() {
        System.out.println("Second method");
    }

    @Override
    public void log(String string) {
        FirstInterface.super.log(string); // Or choose SecondInterface.super.log(string);
        System.out.println("This method is Task1 implementation: " + string); // Alternatively, provide custom implementation
    }

    public static void main(String[] args) {
        Task1 obj = new Task1();
        obj.firstMethod("Test1");
        obj.secondMethod();
        obj.log("TestLog");
    }
}

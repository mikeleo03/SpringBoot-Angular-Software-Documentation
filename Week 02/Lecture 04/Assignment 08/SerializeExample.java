import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializeExample {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", 101, 50000.0));
        employees.add(new Employee("Jane Smith", 102, 60000.0));
        employees.add(new Employee("Mike Johnson", 103, 55000.0));

        try (FileOutputStream fos = new FileOutputStream("employees.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
             
            oos.writeObject(employees); // Serialize the list
            System.out.println("Serialization complete");
        } catch (Exception e) {
            System.out.println("Error happened: " + e);
        }
    }
}
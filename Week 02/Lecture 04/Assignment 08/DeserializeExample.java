import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class DeserializeExample {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("employees.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             
            List<Employee> employees = (List<Employee>) ois.readObject(); // Deserialize the list
            for (Employee emp : employees) {
                System.out.println(emp);
            }
            System.out.println("Deserialization complete");
        } catch (Exception e) {
            System.out.println("Error happened: " + e);
        }
    }
}

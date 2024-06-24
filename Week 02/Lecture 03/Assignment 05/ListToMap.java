import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Assume Employee class with fields: int employeeID, String name, String department
class Employee {
    private int employeeID;
    private String name;
    private String department;

    public Employee(int employeeID, String name, String department) {
        this.employeeID = employeeID;
        this.name = name;
        this.department = department;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}

public class ListToMap {
    public static void main(String[] args) {
        // Example list of Employee objects
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Alice", "HR"),
                new Employee(2, "Bob", "IT"),
                new Employee(3, "Charlie", "Finance")
        );

        // Convert List to Map using employeeID as key
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getEmployeeID, emp -> emp));

        // Print the resulting Map
        employeeMap.forEach((id, emp) -> System.out.println("Employee ID: " + id + ", Employee: " + emp));
    }
}
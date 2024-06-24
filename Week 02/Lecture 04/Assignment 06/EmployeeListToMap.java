import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Employee Class
class Employee {
    private int id;
    private String name;
    private String department;

    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', department='" + department + "'}";
    }
}


public class EmployeeListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", "HR"),
            new Employee(2, "Bob", "IT"),
            new Employee(1, "Alicia", "Marketing"), // Duplicate ID 1
            new Employee(3, "Charlie", "Finance")
        );

        // Convert list of employees to map with ID as the key, handling duplicates
        Map<Integer, Employee> employeeMap = employees.stream()
                                                      .collect(Collectors.toMap(
                                                          Employee::getId,  // Key Mapper
                                                          employee -> employee, // Value Mapper
                                                          (existing, replacement) -> existing // Merge function: keep existing
                                                      ));

        // Print the map
        employeeMap.forEach((id, employee) -> System.out.println("ID: " + id + ", Employee: " + employee));
    }
}

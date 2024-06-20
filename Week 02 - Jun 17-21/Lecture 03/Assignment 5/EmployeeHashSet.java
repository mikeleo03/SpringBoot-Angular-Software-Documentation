import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

class Employee {
    private int id;
    private String name;

    // Constructor
    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Overriding equals to compare Employee by 'id'
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }

    // Overriding hashCode to be consistent with equals
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // toString for easier output
    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "'}";
    }
}

public class EmployeeHashSet {
    public static void main(String[] args) {
        // Create a HashSet to store Employee objects
        Set<Employee> employees = new HashSet<>();

        // Create Employee objects
        Employee e1 = new Employee(1, "Alice");
        Employee e2 = new Employee(2, "Bob");
        Employee e3 = new Employee(1, "Charlie"); // Same ID as e1

        // Add employees to the HashSet
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);  // This will not be added as e1 and e3 are considered equal

        // Print the HashSet size and contents
        System.out.println("HashSet size: " + employees.size()); // Output: 2
        System.out.println(employees); // Output: [Employee{id=1, name='Alice'}, Employee{id=2, name='Bob'}]
    }
}
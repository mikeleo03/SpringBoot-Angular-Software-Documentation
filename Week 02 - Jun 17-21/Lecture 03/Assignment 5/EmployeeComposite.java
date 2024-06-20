import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Employee {
    private int id;
    private String name;

    // Constructor
    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
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


class EmployeeKey {
    private String department;
    private int employeeId;

    // Constructor
    public EmployeeKey(String department, int employeeId) {
        this.department = department;
        this.employeeId = employeeId;
    }

    // Getters
    public String getDepartment() {
        return department;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    // Overriding equals to compare EmployeeKey by 'department' and 'employeeId'
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmployeeKey that = (EmployeeKey) obj;
        return employeeId == that.employeeId && department.equals(that.department);
    }

    // Overriding hashCode to be consistent with equals
    @Override
    public int hashCode() {
        return Objects.hash(department, employeeId);
    }

    // toString for easier output
    @Override
    public String toString() {
        return "EmployeeKey{department='" + department + "', employeeId=" + employeeId + '}';
    }
}


public class EmployeeComposite {
    public static void main(String[] args) {
        // Create a Map with EmployeeKey as key and Employee as value
        Map<EmployeeKey, Employee> employeeMap = new HashMap<>();

        // Create Employee and EmployeeKey objects
        EmployeeKey key1 = new EmployeeKey("HR", 101);
        Employee e1 = new Employee(101, "Alice");

        EmployeeKey key2 = new EmployeeKey("Finance", 102);
        Employee e2 = new Employee(102, "Bob");

        EmployeeKey key3 = new EmployeeKey("HR", 103);
        Employee e3 = new Employee(103, "Charlie");

        // Add employees to the map
        employeeMap.put(key1, e1);
        employeeMap.put(key2, e2);
        employeeMap.put(key3, e3);

        // Retrieve and print employees
        System.out.println("Employees in HR department:");
        for (Map.Entry<EmployeeKey, Employee> entry : employeeMap.entrySet()) {
            if ("HR".equals(entry.getKey().getDepartment())) {
                System.out.println(entry.getValue());
            }
        }

        // Find a specific employee
        EmployeeKey searchKey = new EmployeeKey("Finance", 102);
        Employee foundEmployee = employeeMap.get(searchKey);
        System.out.println("Employee found: " + foundEmployee); // Output: Employee{id=102, name='Bob'}
    }
}

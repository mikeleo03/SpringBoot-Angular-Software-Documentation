import java.util.*;

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("Bob", 8000) // Duplicate name
        );

        Map<String, Employee> employeeMapPlain = ListUtils.convertListToMap(
            employees,
            Employee::getName
        );

        System.out.println(employeeMapPlain);
        // Output: {Bob=Employee{name='Bob', salary=8000}, Charlie=Employee{name='Charlie', salary=6000}}

        Map<EmployeeKey, Employee> employeeMap = ListUtils.convertListToMap(
            employees,
            e -> new EmployeeKey(e.getName(), e.getSalary())
        );

        System.out.println(employeeMap);
        // Output: {EmployeeKey{name='Bob', salary=8000}=Employee{name='Bob', salary=8000}, EmployeeKey{name='Charlie', salary=6000}=Employee{name='Charlie', salary=6000}, EmployeeKey{name='Bob', salary=7000}=Employee{name='Bob', salary=7000}}
    }
}
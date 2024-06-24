import java.util.*;

public class EmployeeOperation {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        List<Employee> sortedBySalary = ListUtils.sortByField(employees, Employee::getSalary);
        System.out.println(sortedBySalary);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Charlie', salary=6000}, Employee{name='Bob', salary=7000}]

        Optional<Employee> maxSalaryEmployee = ListUtils.findMaxByField(employees, Employee::getSalary);
        maxSalaryEmployee.ifPresent(System.out::println);
        // Output: Employee{name='Bob', salary=7000}
    }
}
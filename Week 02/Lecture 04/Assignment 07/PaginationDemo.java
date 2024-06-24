import java.util.*;

public class PaginationDemo {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("David", 8000),
            new Employee("Eve", 9000)
        );

        int pageNumber = 1;
        int size = 2;

        Page<Employee> employeePage = PagingUtils.getPage(employees, pageNumber, size);
        System.out.println(employeePage);

        while (employeePage.hasNext()) {
            pageNumber++;
            employeePage = PagingUtils.getPage(employees, pageNumber, size);
            System.out.println(employeePage);
        }
    }
}

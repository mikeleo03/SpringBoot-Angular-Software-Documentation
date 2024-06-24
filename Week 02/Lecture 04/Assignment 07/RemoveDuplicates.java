import java.util.*;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Alice", "Bob");
        List<String> uniqueNames = ListUtils.removeDuplicates(names);
        System.out.println(uniqueNames); 
        // Output: [Alice, Bob, Charlie]

        List<Person> list = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Alice", 35)
        );

        List<Person> uniquePerson = ListUtils.removeDuplicatesByField(
            list,
            Person::getName
        );

        System.out.println(uniquePerson);
        // Output: [Person{name='Alice', age=30}, Person{name='Bob', age=25}]
    }
}
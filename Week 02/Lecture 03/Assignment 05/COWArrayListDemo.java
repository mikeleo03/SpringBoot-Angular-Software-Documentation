import java.util.concurrent.CopyOnWriteArrayList;

public class COWArrayListDemo {
    public static void main(String[] args) {
        // Create a CopyOnWriteArrayList
        CopyOnWriteArrayList<Integer> numbers = new CopyOnWriteArrayList<>();
        
        // Add elements to the list
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        
        // Print initial list
        System.out.println("Initial List: " + numbers);
        
        // Iterate over the list and modify it
        for (Integer number : numbers) {
            System.out.println("Current Element: " + number);
            numbers.add(number + 10); // Modify the list by adding 10 to each element
        }
        
        // Print modified list
        System.out.println("Modified List: " + numbers);
    }
}
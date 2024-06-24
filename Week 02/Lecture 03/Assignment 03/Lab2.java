import java.util.InputMismatchException;
import java.util.Scanner;

// Custom exception for Lab 2
class Lab2Exception extends RuntimeException {
    public Lab2Exception(String message) {
        super(message);
    }
}

public class Lab2 {
    public static void main(String[] args) {
        String[] menu = {"Item 1: Pizza", "Item 2: Burger", "Item 3: Pasta", "Item 4: Salad", "Item 5: Sushi"};
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter a number between 0 and 4 to select a menu item:");
            int index = scanner.nextInt();

            // Check if the index is within the valid range
            if (index < 0 || index >= menu.length) {
                throw new Lab2Exception("Invalid selection. Please select a number between 0 and 4.");
            }

            // Print the selected menu item
            System.out.println("You selected: " + menu[index]);

        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid integer.");
        } catch (Lab2Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
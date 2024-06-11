import java.util.*;

public class Task4 {
    public static void main(String args[]) {
 
        // Create an array list and the elements, same as the task
        List<Integer> listOfNumber = new ArrayList<>(Arrays.asList(1, 2, 3, -6, 5, 0));
 
        // Try to do iterate with 2 for loop to count based on the index
        for (int i = 0; i < listOfNumber.size() - 1; i++) {
            // Store the initial value
            Integer total = listOfNumber.get(i);

            // Iterate for the rest, try to sum up
            for (int j = i + 1; j < listOfNumber.size() - 1; j++) {
                total += listOfNumber.get(j);

                // Check if it is equal to 0
                if (total == 0) {
                    List<Integer> index = new ArrayList<>();
                    index.add(i);
                    index.add(j);
                    System.out.println(index);
                }
            }
        }
    }
}
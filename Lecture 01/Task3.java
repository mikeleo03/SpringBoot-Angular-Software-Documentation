import java.util.*;

public class Task3 {
    public static void main(String args[]) {
 
        // Create an array list and the elements, same as the task
        List<Integer> listOfNumber = new ArrayList<>(Arrays.asList(1, 4, 3, -6, 5, 4));
 
        // Set the initial value to get the max
        Integer max = listOfNumber.get(0);

        // Iterate to the array list get the first biggest element
        for (int i = 0; i < listOfNumber.size() - 1; i++) {
            if (listOfNumber.get(i) > max) {
                max = listOfNumber.get(i);
            }
        }

        // Exclude the biggest element from the array list
        listOfNumber.remove(listOfNumber.indexOf(max));

        // Reiterate to the array list and get the biggest element
        Integer max2 = listOfNumber.get(0);
        for (int i = 0; i < listOfNumber.size() - 1; i++) {
            if (listOfNumber.get(i) > max2) {
                max2 = listOfNumber.get(i);
            }
        }

        // Get the index, store to the array
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < listOfNumber.size(); i++) {
            if (listOfNumber.get(i).equals(max2)) {
                index.add(i);
            }
        }

        // Print the array
        System.out.println(index);
    }
}

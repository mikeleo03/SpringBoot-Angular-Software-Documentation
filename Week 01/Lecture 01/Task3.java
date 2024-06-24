import java.util.*;

public class Task3 {
    /**
     * Finds the second largest element in the list and returns the indices of its occurrences.
     * 
     * @param list The list of integers.
     * @return A list of indices where the second largest element occurs.
     */
    private static List<Integer> getSecondLargestElementIndices(List<Integer> list) {
        Integer max = findMax(list);
        List<Integer> listWithoutMax = new ArrayList<>(list);
        listWithoutMax.removeAll(Collections.singleton(max));
        Integer secondMax = findMax(listWithoutMax);
        return findIndices(list, secondMax);
    }

    /**
     * Finds the maximum element in the list.
     * 
     * @param list The list of integers.
     * @return The maximum integer in the list.
     */
    private static Integer findMax(List<Integer> list) {
        Integer max = list.get(0);
        for (Integer num : list) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * Finds all indices of the given element in the list.
     * 
     * @param list The list of integers.
     * @param value The value to find in the list.
     * @return A list of indices where the value occurs.
     */
    private static List<Integer> findIndices(List<Integer> list, Integer value) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                indices.add(i);
            }
        }
        return indices;
    }

    public static void main(String[] args) {
        // Create an array list with the elements
        List<Integer> listOfNumbers = new ArrayList<>(Arrays.asList(1, 4, 3, -6, 5, 4));

        // Get the indices of the second largest element
        List<Integer> indices = getSecondLargestElementIndices(listOfNumbers);

        // Print the indices
        System.out.println(indices);
    }
}
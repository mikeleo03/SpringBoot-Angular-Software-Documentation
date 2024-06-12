import java.util.*;

public class Task4 {
    /**
     * Finds all subarrays in the list that sum to zero.
     * 
     * @param list The list of integers.
     * @return A list of lists, where each inner list contains two integers representing the start and end indices of a zero-sum subarray.
     */
    private static List<List<Integer>> findZeroSumSubarrays(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        
        // Iterate through the list
        for (int i = 0; i < list.size(); i++) {
            int sum = 0;
            // Sum up subarray elements from index `i` to `j`
            for (int j = i; j < list.size(); j++) {
                sum += list.get(j);

                // If the sum is zero, store the start and end indices
                if (sum == 0) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Create an array list with the elements
        List<Integer> listOfNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, -6, 5, 0));

        // Find and print the indices of subarrays that sum to zero
        List<List<Integer>> zeroSumSubarrays = findZeroSumSubarrays(listOfNumbers);
        zeroSumSubarrays.forEach(System.out::println);
    }
}
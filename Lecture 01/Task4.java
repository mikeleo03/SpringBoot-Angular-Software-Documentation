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
        Map<Integer, Integer> prefixSumMap = new HashMap<>();
        
        // Initialize to handle subarrays starting from index 0
        prefixSumMap.put(0, -1);
        
        int cumulativeSum = 0;
        
        for (int i = 0; i < list.size(); i++) {
            cumulativeSum += list.get(i);
            
            // If cumulativeSum is already in the map,
            // it means we've found a subarray that sums to zero
            if (prefixSumMap.containsKey(cumulativeSum)) {
                result.add(Arrays.asList(prefixSumMap.get(cumulativeSum) + 1, i));
            }
            
            // Update the map with the current cumulative sum and index
            prefixSumMap.put(cumulativeSum, i);
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
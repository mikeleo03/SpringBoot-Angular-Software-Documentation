import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MultiThreadedSort {
    private static final int THRESHOLD = 2; // Threshold for switching to sequential sort

    // RecursiveAction for merge sort
    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int start, end;

        public MergeSortTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                Arrays.sort(array, start, end + 1); // Sequential sort for small chunks
            } else {
                int mid = (start + end) / 2;
                MergeSortTask leftTask = new MergeSortTask(array, start, mid);
                MergeSortTask rightTask = new MergeSortTask(array, mid + 1, end);
                invokeAll(leftTask, rightTask); // Fork and join subtasks
                merge(array, start, mid, end); // Merge sorted parts
            }
        }

        private void merge(int[] array, int start, int mid, int end) {
            int[] temp = new int[end - start + 1];
            int left = start, right = mid + 1, index = 0;

            while (left <= mid && right <= end) {
                if (array[left] <= array[right]) {
                    temp[index++] = array[left++];
                } else {
                    temp[index++] = array[right++];
                }
            }
            while (left <= mid) {
                temp[index++] = array[left++];
            }
            while (right <= end) {
                temp[index++] = array[right++];
            }

            System.arraycopy(temp, 0, array, start, temp.length); // Copy sorted elements back
        }
    }

    public static void main(String[] args) {
        int[] array = generateRandomArray(20); // Generate random array
        System.out.println("Original array: " + Arrays.toString(array));

        ForkJoinPool pool = new ForkJoinPool(); // Create a thread pool
        pool.invoke(new MergeSortTask(array, 0, array.length - 1)); // Sort the array
        pool.shutdown(); // Shutdown the pool

        System.out.println("Sorted array: " + Arrays.toString(array));
    }

    // Helper method to generate an array of random integers
    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100); // Generate random integers between 0 and 99
        }
        return array;
    }
}
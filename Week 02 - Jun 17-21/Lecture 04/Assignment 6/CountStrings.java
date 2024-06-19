import java.util.Arrays;
import java.util.List;

public class CountStrings {
    public static void main(String[] args) {
        List<String> colors = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
        String letter = "G";

        // Count the number of strings starting with the specified letter
        long count = colors.stream()
                           .filter(color -> color.startsWith(letter))
                           .count();

        System.out.println("Number of strings starting with '" + letter + "': " + count);
    }
}
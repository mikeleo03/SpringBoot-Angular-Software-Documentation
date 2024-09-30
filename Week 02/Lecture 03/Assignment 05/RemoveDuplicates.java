import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicates {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java RemoveDuplicates <inputFile> <outputFile> <keyFieldIndex>");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];
        int keyFieldIndex;
        try {
            keyFieldIndex = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid keyFieldIndex. It must be an integer.");
            return;
        }

        // Set to store unique keys
        Set<String> seenKeys = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
             
            String line;
            while ((line = reader.readLine()) != null) {
                // Properly split the line respecting quoted commas
                String[] fields = parseCsvLine(line);

                // Ensure key field index is valid
                if (fields.length > keyFieldIndex) {
                    String key = fields[keyFieldIndex];
                    if (!seenKeys.contains(key)) {
                        seenKeys.add(key); // Add the key to set (marks as seen)
                        writer.println(line); // Write the line to output
                    }
                }
            }
            
            System.out.println("Duplicates removed successfully. Output written to " + outputFileName);
            
        } catch (IOException e) {
            System.out.println("I/O Error occurred: " + e);
        }
    }

    // Function to parse CSV line while handling commas within quotes
    private static String[] parseCsvLine(String line) {
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();
        List<String> fields = new ArrayList<>();

        for (char c : line.toCharArray()) {
            switch (c) {
                case '"':
                    inQuotes = !inQuotes; // Toggle the inQuotes flag
                    break;
                case ',':
                    if (inQuotes) {
                        field.append(c); // Inside quotes, include comma
                    } else {
                        fields.add(field.toString());
                        field.setLength(0); // Reset the field buffer
                    }
                    break;
                default:
                    field.append(c); // Add character to field buffer
                    break;
            }
        }
        fields.add(field.toString()); // Add last field

        return fields.toArray(new String[0]);
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicates {
    public static void main(String[] args) {
        String inputFileName = "data/input.csv";
        String outputFileName = "data/output.csv";
        String delimiter = ","; // Parse CSV format
        
        // Set to store unique keys (employeeID in this case)
        Set<String> seenKeys = new HashSet<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
             
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into fields
                String[] fields = line.split(delimiter);
                
                // Ensure there are enough fields and key field is valid
                if (fields.length > 1) {
                    String key = fields[0]; // Assuming employeeID is the first field
                    if (!seenKeys.contains(key)) {
                        seenKeys.add(key); // Add the key to set (marks as seen)
                        writer.println(line); // Write the line to output
                    }
                }
            }
            
            System.out.println("Duplicates removed successfully. Output written to " + outputFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

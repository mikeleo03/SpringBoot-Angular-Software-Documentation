class NoVowelsException extends Exception {
    public NoVowelsException(String message) {
        super(message);
    }
}

public class Lab3 {
    public static void main(String[] args) {
        try {
            checkForVowels("try");
            checkForVowels("sky");
        } catch (NoVowelsException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkForVowels(String input) throws NoVowelsException {
        if (!input.matches(".*[AEIOUaeiou].*")) {
            throw new NoVowelsException("No vowels found in input string: " + input);
        } else {
            System.out.println("String contains vowels: " + input);
        }
    }
}
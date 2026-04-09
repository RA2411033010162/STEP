import java.util.Scanner;

public class UseCase10PalindromeCheckerApp {

    // Function to check palindrome
    public static boolean isPalindrome(String str) {

        // Normalize string (remove spaces and convert to lowercase)
        str = str.replaceAll("\\s+", "").toLowerCase();

        int start = 0;
        int end = str.length() - 1;

        // Compare characters
        while (start < end) {
            if (str.charAt(start) != str.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String input = sc.nextLine();

        boolean result = isPalindrome(input);

        System.out.println("Input: " + input);
        System.out.println("Is Palindrome?: " + result);

        sc.close();
    }
}
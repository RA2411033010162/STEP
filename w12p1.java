import java.util.*;

public class UsernameAvailabilityChecker {

    // username -> userId
    private HashMap<String, Integer> userDatabase = new HashMap<>();

    // username -> attempt count
    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    // Check availability
    public boolean checkAvailability(String username) {

        // Track attempts
        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !userDatabase.containsKey(username);
    }

    // Register username
    public void registerUser(String username, int userId) {
        userDatabase.put(username, userId);
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));
        suggestions.add(username + "_official");

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String maxUser = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxUser = entry.getKey();
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    public static void main(String[] args) {

        UsernameAvailabilityChecker system =
                new UsernameAvailabilityChecker();

        // Existing users
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 1);

        // Availability checks
        System.out.println("checkAvailability('john_doe') → "
                + system.checkAvailability("john_doe"));

        System.out.println("checkAvailability('jane_smith') → "
                + system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("suggestAlternatives('john_doe') → "
                + system.suggestAlternatives("john_doe"));

        // Simulate attempts
        for (int i = 0; i < 5; i++) {
            system.checkAvailability("admin");
        }

        // Most attempted
        System.out.println("getMostAttempted() → "
                + system.getMostAttempted());
    }
}
import java.util.*;

/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Generates unique room IDs and prevents double-booking.
 */
public class UseCase6ReservationSystem {

    // Centralized inventory
    private static Map<String, Integer> roomInventory = new HashMap<>();

    // Set to store allocated room IDs to ensure uniqueness
    private static Set<String> allocatedRooms = new HashSet<>();

    static {
        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
    }

    /**
     * Allocation Service: Checks availability, generates ID, and updates inventory.
     */
    public static void confirmReservation(String guestName, String roomType) {
        System.out.println("Processing reservation for: " + guestName);

        // Step 1: Check availability
        int currentCount = roomInventory.getOrDefault(roomType, 0);

        if (currentCount > 0) {
            // Step 2: Generate a unique Room ID (e.g., Suite-101)
            String roomId = roomType.substring(0, 1).toUpperCase() + "-" + (100 + (int)(Math.random() * 900));

            // Step 3: Prevent duplicate allocation
            while (allocatedRooms.contains(roomId)) {
                roomId = roomType.substring(0, 1).toUpperCase() + "-" + (100 + (int)(Math.random() * 900));
            }

            // Step 4: Atomic update of inventory and allocation list
            roomInventory.put(roomType, currentCount - 1);
            allocatedRooms.add(roomId);

            System.out.println("CONFIRMED: " + guestName + " allocated " + roomType + " (ID: " + roomId + ")");
            System.out.println("Remaining " + roomType + "s: " + (currentCount - 1));
        } else {
            System.out.println("REJECTED: No " + roomType + " available for " + guestName);
        }
        System.out.println("-------------------------------------------\n");
    }

    public static void main(String[] args) {
        System.out.println("=== Reservation & Allocation System ===\n");

        // Simulate FIFO booking requests
        confirmReservation("Nirmal", "Suite Room");
        confirmReservation("Vivek", "Single Room");
        confirmReservation("Alice", "Suite Room");
        confirmReservation("Bob", "Suite Room"); // This should fail

        System.out.println("Total Rooms Allocated: " + allocatedRooms);
    }
}
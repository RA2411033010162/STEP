import java.util.*;

public class ParkingLotSystem {

    // parking spot status
    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    // parking spot class
    static class ParkingSpot {

        String licensePlate;
        long entryTime;
        Status status;

        ParkingSpot() {
            status = Status.EMPTY;
        }
    }

    // total spots
    private static final int SIZE = 500;

    private ParkingSpot[] table = new ParkingSpot[SIZE];

    private int totalProbes = 0;
    private int parkedVehicles = 0;

    public ParkingLotSystem() {

        for (int i = 0; i < SIZE; i++)
            table[i] = new ParkingSpot();
    }

    // hash function
    private int hash(String plate) {

        return Math.abs(plate.hashCode()) % SIZE;
    }

    // park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);

        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {

            probes++;

            index = (index + 1) % SIZE;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        totalProbes += probes;
        parkedVehicles++;

        System.out.println(
                "parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)"
        );
    }

    // exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        int probes = 0;

        while (table[index].status != Status.EMPTY) {

            if (table[index].status == Status.OCCUPIED &&
                table[index].licensePlate.equals(plate)) {

                long duration =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / 3600000.0;

                double fee = hours * 5;

                table[index].status = Status.DELETED;

                parkedVehicles--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        plate, index, hours, fee
                );

                return;
            }

            probes++;
            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found.");
    }

    // statistics
    public void getStatistics() {

        double occupancy = (parkedVehicles * 100.0) / SIZE;

        double avgProbes = parkedVehicles == 0 ? 0 :
                totalProbes / (double) parkedVehicles;

        System.out.println("\nParking Statistics");

        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
    }

    // simulation
    public static void main(String[] args) {

        ParkingLotSystem system = new ParkingLotSystem();

        system.parkVehicle("ABC-1234");
        system.parkVehicle("ABC-1235");
        system.parkVehicle("XYZ-9999");

        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        system.exitVehicle("ABC-1234");

        system.getStatistics();
    }
}
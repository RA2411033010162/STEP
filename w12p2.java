import java.util.*;

class InventoryManager {

    private HashMap<String, Integer> stock = new HashMap<>();
    private LinkedHashMap<Integer, String> waitingList = new LinkedHashMap<>();

    public InventoryManager() {
        stock.put("IPHONE15_256GB", 100);
    }

    // check stock
    public synchronized void checkStock(String productId) {
        int count = stock.getOrDefault(productId, 0);
        System.out.println(productId + " → " + count + " units available");
    }

    // purchase item
    public synchronized void purchaseItem(String productId, int userId) {

        int count = stock.getOrDefault(productId, 0);

        if (count > 0) {
            stock.put(productId, count - 1);
            System.out.println("User " + userId + " purchase SUCCESS → Remaining: " + (count - 1));
        } 
        else {
            waitingList.put(userId, productId);
            System.out.println("User " + userId + " added to waiting list → Position #" + waitingList.size());
        }
    }

    // show waiting list
    public void showWaitingList() {
        System.out.println("Waiting List: " + waitingList.keySet());
    }
}

public class FlashSaleSystem {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.checkStock("IPHONE15_256GB");

        manager.purchaseItem("IPHONE15_256GB", 12345);
        manager.purchaseItem("IPHONE15_256GB", 67890);

        // simulate stock finish
        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        manager.purchaseItem("IPHONE15_256GB", 99999);

        manager.showWaitingList();
    }
}
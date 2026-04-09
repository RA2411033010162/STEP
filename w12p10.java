import java.util.*;

public class MultiLevelCacheSystem {

    // Video class
    static class Video {
        String id;
        String data;

        Video(String id, String data) {
            this.id = id;
            this.data = data;
        }
    }

    // LRU Cache using LinkedHashMap
    static class LRUCache<K,V> extends LinkedHashMap<K,V> {

        private int capacity;

        LRUCache(int capacity) {
            super(capacity,0.75f,true);
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
            return size() > capacity;
        }
    }

    // L1 Cache (RAM)
    private LRUCache<String, Video> L1 = new LRUCache<>(10000);

    // L2 Cache (SSD)
    private LRUCache<String, Video> L2 = new LRUCache<>(100000);

    // L3 Database (all videos)
    private HashMap<String, Video> database = new HashMap<>();

    // Access count
    private HashMap<String,Integer> accessCount = new HashMap<>();

    // statistics
    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;


    // get video
    public Video getVideo(String videoId) {

        // L1 check
        if(L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 check
        if(L2.containsKey(videoId)) {

            L2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            Video v = L2.get(videoId);

            promoteToL1(v);

            return v;
        }

        System.out.println("L2 Cache MISS");

        // L3 database
        if(database.containsKey(videoId)) {

            L3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            Video v = database.get(videoId);

            addToL2(v);

            return v;
        }

        System.out.println("Video not found");
        return null;
    }


    // add video to L2
    private void addToL2(Video v) {

        L2.put(v.id,v);

        accessCount.put(v.id,
                accessCount.getOrDefault(v.id,0)+1);
    }


    // promote to L1
    private void promoteToL1(Video v) {

        L1.put(v.id,v);
    }


    // add video to database
    public void addVideo(Video v) {

        database.put(v.id,v);
    }


    // cache statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = total==0 ? 0 : (L1Hits*100.0/total);
        double l2Rate = total==0 ? 0 : (L2Hits*100.0/total);
        double l3Rate = total==0 ? 0 : (L3Hits*100.0/total);

        System.out.println("\nCache Statistics:");

        System.out.printf("L1 Hit Rate: %.2f%%\n",l1Rate);
        System.out.printf("L2 Hit Rate: %.2f%%\n",l2Rate);
        System.out.printf("L3 Hit Rate: %.2f%%\n",l3Rate);
    }


    // simulation
    public static void main(String[] args) {

        MultiLevelCacheSystem system = new MultiLevelCacheSystem();

        system.addVideo(new Video("video_123","Movie A"));
        system.addVideo(new Video("video_999","Movie B"));

        System.out.println("Request 1:");
        system.getVideo("video_123");

        System.out.println("\nRequest 2:");
        system.getVideo("video_123");

        System.out.println("\nRequest 3:");
        system.getVideo("video_999");

        system.getStatistics();
    }
}
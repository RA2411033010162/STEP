import java.util.*;

public class RealTimeAnalytics {

    // page → visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // page → unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source → count
    private HashMap<String, Integer> trafficSources = new HashMap<>();


    // process incoming event
    public void processEvent(String url, String userId, String source) {

        // count page views
        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        // track unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // count traffic source
        trafficSources.put(source,
                trafficSources.getOrDefault(source, 0) + 1);
    }


    // get top 10 pages
    public List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        List<Map.Entry<String, Integer>> topPages = new ArrayList<>();

        for (int i = 0; i < 10 && !pq.isEmpty(); i++) {

            topPages.add(pq.poll());
        }

        return topPages;
    }


    // show dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL TIME DASHBOARD =====");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        System.out.println("\nTop Pages:");

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + ". " + page +
                    " - " + views + " views (" +
                    unique + " unique)");

            rank++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;

        for (int count : trafficSources.values())
            total += count;

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);

            double percent = (count * 100.0) / total;

            System.out.printf("%s : %.2f%%\n", source, percent);
        }
    }


    // main simulation
    public static void main(String[] args) throws Exception {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        // simulate incoming events
        analytics.processEvent("/article/breaking-news", "user1", "google");
        analytics.processEvent("/article/breaking-news", "user2", "facebook");
        analytics.processEvent("/sports/championship", "user3", "google");
        analytics.processEvent("/sports/championship", "user4", "direct");
        analytics.processEvent("/article/breaking-news", "user1", "google");
        analytics.processEvent("/tech/ai", "user5", "google");
        analytics.processEvent("/tech/ai", "user6", "direct");
        analytics.processEvent("/tech/ai", "user7", "facebook");

        // update dashboard every 5 seconds
        for (int i = 0; i < 3; i++) {

            analytics.getDashboard();

            Thread.sleep(5000);
        }
    }
}
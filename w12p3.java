import java.util.*;

public class DNSCacheSystem {

    // Entry class
    static class DNSEntry {

        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttl) {

            this.domain = domain;
            this.ipAddress = ipAddress;

            // expiry time = current time + TTL
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {

            return System.currentTimeMillis() > expiryTime;
        }
    }

    // DNS Cache Class
    static class DNSCache {

        private int capacity = 5;

        private LinkedHashMap<String, DNSEntry> cache =
                new LinkedHashMap<>(capacity, 0.75f, true);

        private int hits = 0;
        private int misses = 0;

        public String resolve(String domain) {

            DNSEntry entry = cache.get(domain);

            // CACHE HIT
            if (entry != null && !entry.isExpired()) {

                hits++;

                System.out.println(domain + " → Cache HIT → " + entry.ipAddress);

                return entry.ipAddress;
            }

            // CACHE MISS
            misses++;

            String ip = queryUpstreamDNS(domain);

            cache.put(domain, new DNSEntry(domain, ip, 5));

            // LRU eviction
            if (cache.size() > capacity) {

                String firstKey = cache.keySet().iterator().next();
                cache.remove(firstKey);
            }

            System.out.println(domain + " → Cache MISS → " + ip);

            return ip;
        }

        // simulate upstream DNS lookup
        private String queryUpstreamDNS(String domain) {

            try {

                Thread.sleep(100);

            } catch (Exception e) {
            }

            return "172.217.14." + new Random().nextInt(255);
        }

        public void getCacheStats() {

            int total = hits + misses;

            double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

            System.out.println("\nCache Statistics");
            System.out.println("Hits: " + hits);
            System.out.println("Misses: " + misses);
            System.out.println("Hit Rate: " + hitRate + "%");
        }
    }

    // MAIN METHOD
    public static void main(String[] args) throws Exception {

        DNSCache cache = new DNSCache();

        cache.resolve("google.com");
        cache.resolve("google.com");

        Thread.sleep(6000);

        cache.resolve("google.com");

        cache.getCacheStats();
    }
}
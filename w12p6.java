import java.util.*;

public class RateLimiterSystem {

    // Token Bucket Class
    static class TokenBucket {

        int tokens;
        int maxTokens;
        double refillRate;
        long lastRefillTime;

        TokenBucket(int maxTokens, int refillRatePerHour) {

            this.maxTokens = maxTokens;
            this.tokens = maxTokens;

            // tokens per millisecond
            this.refillRate = refillRatePerHour / 3600000.0;

            this.lastRefillTime = System.currentTimeMillis();
        }

        // refill tokens based on time passed
        private void refill() {

            long now = System.currentTimeMillis();

            long timePassed = now - lastRefillTime;

            int newTokens = (int) (timePassed * refillRate);

            if (newTokens > 0) {

                tokens = Math.min(maxTokens, tokens + newTokens);

                lastRefillTime = now;
            }
        }

        // try consuming a token
        synchronized boolean allowRequest() {

            refill();

            if (tokens > 0) {

                tokens--;

                return true;
            }

            return false;
        }

        int getRemainingTokens() {

            refill();

            return tokens;
        }
    }

    // Rate limiter map
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private int limit = 1000;

    // check rate limit
    public boolean checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(limit, limit));

        TokenBucket bucket = clients.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {

            System.out.println(
                    "Allowed (" + bucket.getRemainingTokens() + " requests remaining)"
            );
        }
        else {

            System.out.println(
                    "Denied (0 requests remaining, retry later)"
            );
        }

        return allowed;
    }

    // get client status
    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {

            System.out.println("Client not found");
            return;
        }

        int used = limit - bucket.getRemainingTokens();

        long resetTime = bucket.lastRefillTime + 3600000;

        System.out.println(
                "{used: " + used +
                ", limit: " + limit +
                ", reset: " + resetTime + "}"
        );
    }


    // Main simulation
    public static void main(String[] args) {

        RateLimiterSystem limiter = new RateLimiterSystem();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {

            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}
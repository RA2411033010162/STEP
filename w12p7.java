import java.util.*;

public class AutocompleteSystem {

    // Trie Node
    static class TrieNode {

        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        String query = "";
    }

    // root of trie
    private TrieNode root = new TrieNode();

    // query -> frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();


    // insert query into trie
    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());

            node = node.children.get(c);
        }

        node.isEnd = true;
        node.query = query;
    }


    // update frequency when query searched
    public void updateFrequency(String query) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);

        insert(query);
    }


    // search suggestions
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();

        dfs(node, results);

        PriorityQueue<String> pq =
                new PriorityQueue<>((a, b) ->
                        frequencyMap.get(a) - frequencyMap.get(b));

        for (String q : results) {

            pq.offer(q);

            if (pq.size() > 10)
                pq.poll();
        }

        List<String> top = new ArrayList<>();

        while (!pq.isEmpty())
            top.add(pq.poll());

        Collections.reverse(top);

        return top;
    }


    // DFS to collect queries
    private void dfs(TrieNode node, List<String> results) {

        if (node.isEnd)
            results.add(node.query);

        for (TrieNode child : node.children.values())
            dfs(child, results);
    }


    // main simulation
    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java download");
        system.updateFrequency("java tutorial");
        system.updateFrequency("java tutorial");
        system.updateFrequency("javascript");
        system.updateFrequency("java 21 features");

        List<String> suggestions = system.search("jav");

        System.out.println("Search suggestions for 'jav':");

        int rank = 1;

        for (String s : suggestions) {

            System.out.println(
                    rank + ". " + s +
                    " (" + system.frequencyMap.get(s) + " searches)"
            );

            rank++;
        }
    }
}
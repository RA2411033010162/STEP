import java.util.*;

public class PlagiarismDetector {

    // n-gram size
    static final int N = 5;

    // ngram -> set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    // store documents
    private HashMap<String, String> documents = new HashMap<>();


    // add document to system
    public void addDocument(String docId, String text) {

        documents.put(docId, text);

        List<String> ngrams = generateNGrams(text);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());

            index.get(gram).add(docId);
        }
    }


    // analyze a new document
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = generateNGrams(text);

        System.out.println("Extracted " + ngrams.size() + " n-grams");

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String existingDoc : index.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity > 60)
                System.out.println(" (PLAGIARISM DETECTED)");
            else if (similarity > 10)
                System.out.println(" (suspicious)");
            else
                System.out.println();
        }
    }


    // generate n-grams
    private List<String> generateNGrams(String text) {

        List<String> result = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {

                gram.append(words[i + j]).append(" ");
            }

            result.add(gram.toString().trim());
        }

        return result;
    }


    // main program
    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 =
                "artificial intelligence is transforming the world of technology and education";

        String essay2 =
                "artificial intelligence is transforming the world of technology and society";

        String essay3 =
                "machine learning and artificial intelligence are important technologies";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);

        System.out.println("Analyzing new document...\n");

        detector.analyzeDocument("essay_123.txt", essay3);
    }
}
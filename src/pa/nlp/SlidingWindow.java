package pa.nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.simple.*;
import pa.nlp.CoreNLP;


public class SlidingWindow {

	// Load list of n-grams to be merged
	private static String ngramFile = "resources/ngrams.txt";
	private static List<String> ngrams;

	private Map<String, List<String>> map;
	public SlidingWindow(Map<String, List<String>> tokMap) {
		map = tokMap;
	}

	// Merge frequent n-grams using a sliding window over input tokens
	public Map<String, List<String>> mergeNgrams() throws IOException {

		// Load or generate n-grams
		File f = new File(ngramFile);
		if(f.exists() && !f.isDirectory()) {
			try {
				System.out.println("Loading n-grams...");
				ngrams = loadNgrams();
			} catch(IOException e) {
        		e.printStackTrace();
    		}   
		}
		else {
			System.out.println("Generating n-grams...");
			ngrams = generateNgrams();
		}

		// Filter and merge tokens based on n-grams
        for (Map.Entry<String, List<String>> entry: map.entrySet()) {

            // Join to string of tokens
            String stringToks = String.join(" ", entry.getValue());

            for (String ngram: ngrams) {
                // Merge n-gram
                String merged = ngram.replace(" ", "_");
                // Replace n-gram in string of tokens
                stringToks = stringToks.replace(ngram, merged);
            }

            // Convert string of tokens back to list
            List<String> toks = Arrays.asList(stringToks.split(" "));
            
            // Filter remaining tokens
            List<String> filtered = new ArrayList<String>();
            for (String tok: toks) {
            	if (ngrams.contains(tok.replace("_", " "))) {
                	filtered.add(tok);
                }
            }

            // Update map
            entry.setValue(filtered);
        }
        return map;
	}

	// Load n-grams
	private static List<String> loadNgrams() throws IOException {

		Scanner scanner = new Scanner(new File(ngramFile));
		List<String> ngrams = new ArrayList<String>();

	    // Read list of stop words line by line
	    while (scanner.hasNextLine()){
	        ngrams.add(scanner.nextLine());
	    }
	    scanner.close(); 
	    return ngrams;
	}

	// Generate n-grams
	private List<String> generateNgrams() throws IOException {

		List<String> ngrams = new ArrayList<String>();

		// Minimum frequency counts for each n
		Map<Integer, Integer> minCounts = new HashMap<Integer, Integer>();
		minCounts.put(1, 3);
        minCounts.put(2, 6);
        minCounts.put(3, 15);

        // Get maximum and minimum length of n-grams
        int max = Collections.max(minCounts.keySet());
        int min = Collections.min(minCounts.keySet());

        for (int n=max; n>=min; n--) {

        	System.out.println(n);

			Map<String, Integer> freqs = new HashMap<String, Integer>();

			// Get n-grams and frequencies
        	for (Map.Entry<String, List<String>> entry: map.entrySet()) {

        		// Get n-grams from tokens
        		List<String> docNgrams = getNgrams(n, entry.getValue());

        		// Update frequency count
        		for (String ngram: docNgrams) {
                	// Check if n-gram has valid part-of-speech (POS) pattern
                	boolean isValid = isValidPOS(ngram);
                	if (isValid) {
                    	freqs.put(ngram, freqs.getOrDefault(ngram, 0) + 1);
                	}
            	}
			}
			Map<String, Integer> sorted = sortByValues(freqs, minCounts.get(n));
			ngrams.addAll(sorted.keySet());
        }
    	// Write n-grams to file
		FileWriter writer = new FileWriter(ngramFile); 
		for(String ngram: ngrams) {
		  writer.write(ngram + "\n");
		}
		writer.close();
        return ngrams;
	}

	private List<String> getNgrams(int n, List<String> toks) {

        List<String> ngrams = new ArrayList<String>();
        
        for (int k=0; k<toks.size()-n+1; k++) {

            int start = k;
            int end = k+n;
            String ngram = "";

            for(int i=start; i<end; i++) {
                // Get token by index
                ngram += toks.get(i);
                // Normalize whitespace
                if (i != end-1) {
                    ngram += " ";
                }
            }
            // Add ngram to list of ngrams
            ngrams.add(ngram);
        }
        return ngrams;
    }

    // Check if text has "keyword-like" part-of-speech (POS) pattern
    private boolean isValidPOS(String text) {

        // Process text using CoreNLP
        Sentence ngram = new Sentence(text);

        // Get list of POS tags
        List posTags = ngram.posTags();

        String first = posTags.get(0).toString();
        String last = posTags.get(posTags.size()-1).toString();

        // Valid n-grams start with adjective, adverb, or noun
        if (first.startsWith("JJ") || first.startsWith("RB") || first.startsWith("NN")) {
            // Phrasal head should be noun
            if (last.startsWith("NN"))  {
                return true;
            }
        }
        return false;
    }

    // Sort map by values and minimum count
    @SuppressWarnings("unchecked")
    public static HashMap sortByValues(Map map, int minCount) { 

        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                    .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();

            if (Integer.valueOf((int)entry.getValue()) >= minCount) {
                sortedHashMap.put(entry.getKey(), entry.getValue());
            } 
        } 
        return sortedHashMap;
    }

}


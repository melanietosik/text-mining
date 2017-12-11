package pa.nlp;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.simple.*;


public class CoreNLP {

	// Load stop words
	private static String stopFile = "resources/stopwords.txt";
	private static Set<String> stop;
	static {
		try {
			stop = loadStop();
		} catch(IOException e) {
        	e.printStackTrace();
    	}
    }

	private String text; 
	public CoreNLP(String doc) {
		text = doc;
	}

    // Print text
	public void printText() {
		System.out.println(text);
	}

	// Get preprocessed document tokens
	public List<String> getToks() {

		List<String> toks;

		toks = ner();
		return filter(toks);
	}

	// Apply named-entity recognition (NER)
    private List<String> ner() {

        Document doc = new Document(text);
        List<String> toks = new ArrayList<String>();

        for (Sentence sent: doc.sentences()) {

        	int len = sent.length();
			String ne = "";
            List<String> sentToks = new ArrayList<String>();
            
            for (int i=0; i<len; i++){

                // Get lemma, NE tag, and word
                String lemma = sent.lemma(i);
                String tag = sent.nerTag(i);
                String word = sent.word(i);
                
                // Get NE tag of next token
                String tagNext;
                if (i == len - 1) {
                    tagNext = "O";
                } else {
                    tagNext = sent.nerTag(i+1);
                }
                // No NE tag
                if (tag.equals("O")) {
                    sentToks.add(lemma.toLowerCase());
                } else {
                    // Next token is same NE
                    if (tagNext.equals(tag)) {
                        ne += word;
                        ne += " ";
                    } else {
                        ne += word;
                        sentToks.add(ne);
                        ne = "";
                    }
                }
            }
            toks.addAll(sentToks);
        }
        return toks;
    }

    // Filter stop words and punctuation
    private List<String> filter(List<String> toks) {

        for (Iterator<String> iter = toks.iterator(); iter.hasNext(); ) {
      
            String tok = iter.next();
            
            // Filter punctuation
            boolean nonAlpha = tok.matches("^.*[^a-zA-Z0-9 ].*$");
            if (nonAlpha || stop.contains(tok)) {
                iter.remove();
            }
        }
        return toks;
    }

	// Load stop words
	private static Set<String> loadStop() throws IOException {

		Scanner scanner = new Scanner(new File(stopFile));
		List<String> toks = new ArrayList<String>();

	    // Read list of stop words line by line
	    while (scanner.hasNext()){
	        toks.add(scanner.next());
	    }
	    scanner.close(); 

	    Set<String> stop = new HashSet<String>(toks);
	    return stop;
	}

}


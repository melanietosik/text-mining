package pa.knn;

import java.io.*;
import java.util.*;

import pa.nlp.*;
import pa.tfidf.TFIDF;


public class Preprocessor {

    private static String ngramFile = "resources/ngrams.txt";

    private static List<List<String>> _docs;    // Documents
    private static List<String> _terms;         // Terms

    public Preprocessor(List<List<String>> docs, List<String> terms) {
        _docs = docs;
        _terms = terms;
    }

    public double[] process(String doc) throws IOException {

        // Parse document into tokens
        List<String> toks = preprocess(doc);

        // Generate TF-IDF vector
        double[] vec = vectorize(toks);
        return vec;
    }

    // Preprocess unseen document
    private List<String> preprocess(String doc) throws IOException {

        // Process document using CoreNLP
        CoreNLP nlp = new CoreNLP(doc);
        // Get document tokens
        List<String> toks = nlp.getToks();

        // Sliding window
        SlidingWindow slide = new SlidingWindow();

        // Load n-grams
        File f = new File(ngramFile);
        List<String> ngrams = new ArrayList<String>();

        if (f.exists() && !f.isDirectory()) {
            try {
                ngrams = slide.loadNgrams();
            } catch(IOException e) {
                e.printStackTrace();
            }   
        } else {
            System.out.println("Missing n-gram file, exiting");
            System.exit(1);
        }

        // Merge n-grams
        toks = slide.mergeNgrams(toks, ngrams);
        return toks;
    }

    // Generate TF-IDF vector
    private double[] vectorize(List<String> toks) throws IOException {

        TFIDF scorer = new TFIDF();
        double[] vec = new double[_terms.size()];
        double[] tok = new double[toks.size()];

        // Get token TF-IDF scores
        for (int i=0; i<toks.size(); i++) {
            String term = toks.get(i);
            double score = scorer.tfIdf(toks, _docs, term);
            tok[i] = score;
        }

        // Generate term vector
        int i = 0;
        for (String term: _terms) {

            if (toks.contains(term)) {
                int idx = toks.indexOf(term);
                vec[i] = tok[idx];
            } else {
                vec[i] = 0;
            }
            i++;
        }
        return vec;
    }

}


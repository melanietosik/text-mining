package pa.knn;

import java.io.*;
import java.util.*;

import pa.kmeans.*;
import pa.nlp.*;
import pa.tfidf.*;


public class KNN {

    private static int _nrows;  // Number of rows in matrix
    private static int _ndims;  // Number of dimensions in matrix

    private static String idFile = "resources/ids.txt";
    private static String matrixFile = "resources/matrix.txt";
    private static String nameFile = "resources/names.txt";
    private static String ngramFile = "resources/ngrams.txt";

    private static double[][] _matrix;          // TF-IDF matrix
    private static List<String> _ids;           // Document IDs
    private static List<String> _labels;        // Document labels
    private static List<List<String>> _docs;    // Document tokens

    public KNN(double[][] matrix, List<List<String>> docs, List<String> ids, List<String> labels) {
        _matrix = matrix;
        _docs = docs;
        _ids = ids;
        _labels = labels;
    }

    public String getLabel(String doc, String metric, int k) throws IOException {

        // Preprocess input document
        List<String> toks = preprocess(doc);

        // Generate TF-IDF vector
        double[] vec = vectorize(toks);

        return "hello";

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

        if(f.exists() && !f.isDirectory()) {
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

        int len = toks.size();
        double[] vec = new double[len];

        for (int i=0; i<len; i++) {

            String term = toks.get(i);
            double score = scorer.tfIdf(toks, _docs, term);
            System.out.println(score);
            vec[i] = score;
        }
        return vec;

    }

    
}


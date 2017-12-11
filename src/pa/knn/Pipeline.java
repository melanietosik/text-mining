package pa.knn;

import java.io.*;
import java.util.*;

import pa.kmeans.Distance;
import pa.utils.IOUtils;


public class Pipeline {

    private static int _k;
    private static String _query;   // Document query ID
    private static String _metric;  // Distance metric, "cosine" or "euclid"

    private static IOUtils utils = new IOUtils();

    private static String idFile        = "resources/ids.txt";
    private static String docFile       = "resources/docs.txt";
    private static String matrixFile    = "resources/matrix.txt";
    private static String nameFile      = "resources/names.txt";
    private static String termFile      = "resources/terms.txt";

    public static void main(String[] args) throws IOException {

        _k = 5;
        _metric = "cosine";

        // Read document text
        String doc = "Is Earth trying to eject us from the planet? Again and again and again the harshest of winds and hardest of rains has pounded on the most-defenseless territories we have. The Caribbean islands, hanging out in open sea. The Florida peninsula, jutting out into danger.";

        // Read documents and terms
        List<List<String>> docs = utils.readDocs(docFile);
        List<String> terms = utils.readLines(termFile);

        // Preprocess and vectorize input document
        System.out.println("Processing input document...");
        Preprocessor tfidf = new Preprocessor(docs, terms);
        double[] vec = tfidf.process(doc);

        // Read TF-IDF matrix
        double[][] matrix = utils.readMatrix(matrixFile);

        // K-NN
        System.out.println("Computing " + _k + "-nearest neighbors/topics...");
        KNN knn = new KNN(matrix);
        int[] neighbors = knn.getNearestNeighbors(vec, _k, _metric);

        // Read document IDs and topic names
        List<String> ids = utils.readLines(idFile);
        List<String> topics = utils.readLines(nameFile);

        // Print results and get neighbors' topics
        List<String> nearestTopics = new ArrayList<String>();

        for (int i: neighbors) {
            System.out.println("\t" + ids.get(i) + ", " + topics.get(i));
            nearestTopics.add(topics.get(i));
        }

        // Get majority label for input document
        String label = knn.getLabel(nearestTopics);
        System.out.println("Majority label: " + label);

        // 10-fold cross-validation
        System.out.println("Cross-validation...");
        CrossValidation cv = new CrossValidation(matrix, ids, topics);
        Map<Integer, Double> acc = cv.crossValidate(10);
        System.out.println("Average accuracies: " + acc);
    }

}


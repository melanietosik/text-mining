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

        // // Read TF-IDF matrix
        // double[][] matrix = readMatrix(matrixFile);
        // // Read document IDs
        // List<String> ids = readStrings(idFile);
        // // Read document labels
        // List<String> labels = readStrings(nameFile);
        // // Read document tokens
        // List<List<String>> docs = readDocs(docFile);

        // Read document text
        String doc = "Is Earth trying to eject us from the planet? Again and again and again the harshest of winds and hardest of rains has pounded on the most-defenseless territories we have. The Caribbean islands, hanging out in open sea. The Florida peninsula, jutting out into danger.";

        // Read documents and terms
        List<List<String>> docs = utils.readDocs(docFile);
        List<String> terms = utils.readLines(termFile);

        // Preprocess and vectorize input document
        Preprocessor tfidf = new Preprocessor(docs, terms);
        double[] vec = tfidf.process(doc);

        utils.printDoubleArr(vec);




        // // K-NN classification
        // System.out.println("K-NN classification...");
        // KNN nearest = new KNN(matrix, docs, ids, labels);
        // String label = nearest.getLabel(doc, _metric, _k);

        // // Document similarity
        // System.out.println("Document similarity...");
        // Distance dist = new Distance(matrix);
        // List<Integer> similar = dist.getSimilar(ids.indexOf(_query), 5, _metric);
        // for (int idx: similar) {
        //     System.out.println(ids.get(idx));
        // }

    }







}


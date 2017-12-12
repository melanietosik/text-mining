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

        _k = 3;
        _metric = "cosine";

        // Usage
        if (args.length != 2) {
            System.out.println("Usage: java pa.knn.Pipeline <test_file> <test_data_folder>");
        }
        String testFile = args[0];
        File[] testData = new File(args[1]).listFiles();

        // Read test document text
        String doc = utils.readText(testFile);

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
            System.out.println(" " + ids.get(i) + ", " + topics.get(i));
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

        // Choose best k based on cross-validation accuracies
        Map.Entry<Integer, Double> max = null;
        for (Map.Entry<Integer, Double> entry : acc.entrySet()) {
            if (max == null || entry.getValue() > max.getValue()) { max = entry; }
        }
        int best = max.getKey();
        System.out.println("Best: k=" + best);

        // Gold labels per test folder
        Map<String, String> gold = new HashMap<String, String>();
        gold.put("1", "irma_harvey");
        gold.put("2", "predictive_analytics"); // ADD ADDITIONAL FOLDERS/LABELS HERE

        /*
            airline_safety
            amphertamine
            china_spy_plan_captives
            hoof_mouth_disease
            iran_nuclear
            korea_nuclear
            mortgage_rates
            ocean_pollution
            satanic_cult
            storm_irene
            volcano
            saddam_hussein
            kim_jong_un
            predictive_analytics
            irma_harvey
        */ 

        // Predicted labels 
        List<String> predLabels = new ArrayList<String>();
        // True labels
        List<String> trueLabels = new ArrayList<String>();

        for (File folder: testData) {

            String folderName = folder.getName();
            if (folderName.startsWith(".")) {
                continue;
            }
            System.out.println(folderName);

            File[] testDocs = folder.listFiles();
            for (File testDoc: testDocs) {

                // Get document ID
                String id = folder.getName() + "_" + testDoc.getName();
                id = id.substring(0, id.lastIndexOf("."));
                
                // Preprocess
                String text = utils.readText(testDoc.getAbsolutePath());
                double[] testVec = tfidf.process(text);
                
                // Get k-nearest neighbors and topics
                int[] testNeighbors = knn.getNearestNeighbors(testVec, best, "cosine");
                List<String> testNearestTopics = new ArrayList<String>();
                for (int i: testNeighbors) { testNearestTopics.add(topics.get(i)); }
                
                // Get label and add to list of predicted labels
                String testLabel = knn.getLabel(testNearestTopics);
                predLabels.add(testLabel);

                // Get true label and add to list of true labels
                String trueLabel = gold.get(id.split("_")[0]);
                trueLabels.add(trueLabel);
            }
        }
        utils.printConfusionMatrix(predLabels, trueLabels);
    }
}


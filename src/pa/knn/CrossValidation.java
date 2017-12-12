package pa.knn;

import java.io.*;
import java.util.*;


public class CrossValidation {

    private static double[][] _matrix;      // TF-IDF matrix
    private static List<String> _ids;       // Document IDs
    private static List<String> _topics;    // Document topics
    
    public CrossValidation(double[][] matrix, List<String> ids, List<String> topics) {
        _matrix = matrix;
        _ids = ids;
        _topics = topics;
    }

    public Map<Integer, Double> crossValidate(int kfold) {

        int len = _ids.size();

        // Gold labels
        Map<String, String> gold = new HashMap<String, String>();
        for (int i=0; i<len; i++) {
            gold.put(_ids.get(i), _topics.get(i));
        }

        // Randomly shuffe document IDs for training
        List<String> shuffled = new ArrayList<String>(_ids);
        Collections.shuffle(shuffled);

        // Split training data into folds
        int foldSize = len / kfold;
        List<List<String>> folds = new LinkedList<List<String>>();
        for (int i=0; i<len; i+=foldSize) {
            folds.add(shuffled.subList(i, Math.min(i+foldSize, len)));
        }

        // Accuracy map
        Map<Integer, Double> accuracy = new HashMap<Integer, Double>();

        for (int k=1; k<16; k++) {
            System.out.println("k=" + k);

            // Accuracy per fold
            double[] acc = new double[kfold];

            for (int fold=0; fold<kfold; fold++) {

                // Get validation set
                List<Integer> validation = new ArrayList<Integer>();
                for (List<String> data: folds) {
                    if (folds.indexOf(data) != fold) { continue; }
                    // Get document indices
                    for (String id: data) {
                        validation.add(_ids.indexOf(id));
                    }
                }

                // K-NN, hold out validation set
                KNN knn = new KNN(_matrix, validation);

                // Number of correctly predicted labels
                int correct = 0;

                for (String id: folds.get(fold)) {

                    // Get document vector
                    int idx = _ids.indexOf(id);
                    double[] vec = _matrix[idx];
                    // Get k-nearest neighbors and topics
                    int[] neighbors = knn.getNearestNeighbors(vec, k, "cosine");
                    List<String> nearestTopics = new ArrayList<String>();
                    for (int i: neighbors) { nearestTopics.add(_topics.get(i)); }
                    // Get label
                    String label = knn.getLabel(nearestTopics);
                    // Check if predicted label is correct
                    String trueLabel = _topics.get(idx);
                    if (label.equals(trueLabel)) {
                        correct++;
                    }
                }
                // Compute accuracy
                double total = folds.get(fold).size();
                acc[fold] = correct / total;
            }
            // Average accuracy across folds
            double avg = 0.0;
            double sum = 0.0;
            double total = acc.length;
            for(int i=0; i<total; i++) {
                sum = sum + acc[i];
            }
            avg = sum / total;
            accuracy.put(k, avg);
        }
        return accuracy;
    }

}


package pa.knn;

import java.io.*;
import java.util.*;

import pa.kmeans.Distance;

public class KNN {

    private static double[][] _matrix;          // TF-IDF matrix
    private static List<Integer> _validation;   // Validation set
    
    public KNN(double[][] matrix) {
        _matrix = matrix;
        _validation = new ArrayList<Integer>();
    }

    public KNN(double[][] matrix, List<Integer> validation) {
        _matrix = matrix;
        _validation = validation;
    }

    private static Distance distance = new Distance();

    // Get k-nearest neighbors
    public int[] getNearestNeighbors(double[] vec, int k, String metric) {

        int[] neighbors = new int[k];

        // Create list of reference indices
        List<Integer> refs = new ArrayList<Integer>();
        for (int i=0; i<_matrix.length; i++) {
            refs.add(i);
        }

        // Hold out validation set
        for (int i=0; i<_validation.size(); i++) {
            refs.remove(new Integer(_validation.get(i)));
        }

        // Remove previously predicted neighbors to prevent duplicates
        for (int i=0; i<k; i++) {
            int label = nearest(vec, refs, metric);
            neighbors[i] = label;
            refs.remove(new Integer(label));
        }
        return neighbors;
    }

    // Get nearest document vector to input vector
    private int nearest(double[] vec, List<Integer> refs, String metric) {

        if (!metric.equals("euclid") && !metric.equals("cosine")) {
            throw new IllegalArgumentException("Invalid metric");
        }

        double max = 0.0;
        if (metric.equals("euclid")) {
            max = distance.euclid(vec, _matrix[refs.get(0)]);
        } else {
            max = distance.cosine(vec, _matrix[refs.get(0)]);
        }

        int label = 0;
        for (int ref: refs) {

            double dist = 0.0;
            if (metric.equals("euclid")) {
                dist = distance.euclid(vec, _matrix[ref]);
            } else {
                dist = distance.cosine(vec, _matrix[ref]);
            }
            if (dist > max) {
                max = dist;
                label = ref;
            }
        }
        return label;
    }

    // Get majority label
    public String getLabel(List<String> topics) {

        Map<String, Integer> counts = new HashMap<String, Integer>();
        topics.forEach(s -> counts.put(s, counts.getOrDefault(s, 0) + 1));

        String max = counts.keySet().stream().reduce((s1, s2) -> {
            if (counts.get(s1) > counts.get(s2)) {
                return s1;
            } return s2;
        }).orElseThrow(() -> new IllegalStateException("Eut"));
        return max;
    }

}


package pa.kmeans;

import java.io.*;
import java.util.*;

import pa.utils.IOUtils;


public class Pipeline {

    private static int _k;          // Number of clusters
    private static String _query;   // Document query ID
    private static String _metric;  // Distance metric, "cosine" or "euclid"

    private static IOUtils utils = new IOUtils();

    private static String idFile     = "resources/ids.txt";
    private static String labelsFile = "resources/labels_kmeans.txt";
    private static String matrixFile = "resources/matrix.txt";

    public static void main(String[] args) throws IOException {

        _k = 15;
        _query = "C8_article02";
        _metric = "cosine";

        // Read TF-IDF matrix
        double[][] matrix = utils.readMatrix(matrixFile);

        // Read document IDs
        List<String> ids = utils.readLines(idFile);

        // Document similarity
        System.out.println("Document similarity...");
        Distance dist = new Distance(matrix);
        List<Integer> similar = dist.getSimilar(ids.indexOf(_query), 5, _metric);
        for (int idx: similar) {
            System.out.println(ids.get(idx));
        }

        // SVD
        System.out.println("Dimensionality reduction (SVD)...");
        DimReduction dim = new DimReduction(matrix);
        double[][] reduced = dim.reduceDimensions(_k);

        // K-Means
        System.out.println("K-Means...");
        KMeans kmeans = new KMeans(reduced, _metric, false);
        int[] labels = kmeans.clusterDocs(_k);
        utils.printIntArr(labels);

        // Project into 2-dimensional space
        double[][] projected = dim.reduceDimensions(2);

        // Write x-/y-coordinates, label
        FileWriter writer = new FileWriter(labelsFile);
        for (int i=0; i<projected.length; i++) {
            for (int j=0; j<2; j++) {
                writer.write(projected[i][j] + ",");
            }
            writer.write(labels[i] + "\n");
        }
        writer.close();
    }

}


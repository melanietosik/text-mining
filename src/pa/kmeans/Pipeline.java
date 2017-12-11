package pa.kmeans;

import java.io.*;
import java.util.*;


public class Pipeline {

    private static int _nrows;  // Number of rows in matrix
    private static int _ndims;  // Number of dimensions in ma

    private static int _k;          // Number of clusters
    private static String _query;   // Document query ID
    private static String _metric;  // Distance metric, "cosine" or "euclid"

    private static String idFile = "resources/ids.txt";
    private static String matrixFile = "resources/matrix.txt";
    private static String labelsFile = "resources/labels_kmeans.txt";

    public static void main(String[] args) throws IOException {

        _k = 15;
        _query = "C8_article02";
        _metric = "cosine";

        // Read TF-IDF matrix
        double[][] matrix = readMatrix(matrixFile);

        // Read document IDs
        List<String> ids = readIds(idFile);

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
        printIntArr(labels);

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

    // Read matrix
    private static double[][] readMatrix(String fileName) throws IOException {

        _nrows = 0;
        _ndims = 0;

        // Get number of rows
        Scanner input = new Scanner(new File(fileName));
        input.useDelimiter(",");
        while (input.hasNextLine()) {
            _nrows++;
            input.nextLine();
        }
        input.close();

        // Get number of dimensions
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String[] firstLine = br.readLine().split(",");
        _ndims = firstLine.length;

        // Matrix
        double[][] a = new double[_nrows][_ndims];

        // Read TF-IDF scores
        input = new Scanner(new File(fileName));
        input.useDelimiter(",");

        int i = 0;
        while(input.hasNextLine()) {
            Scanner c = new Scanner(input.nextLine());
            c.useDelimiter(",");
            for(int j=0; j<_ndims; j++) {
                if(c.hasNextDouble()) {
                    a[i][j] = c.nextDouble();
                }
            }
            i++;
        }
        return a;
    }

    // Read document IDs
    private static List<String> readIds(String fileName) throws IOException {

        Scanner scanner = new Scanner(new File(fileName));
        List<String> ids = new ArrayList<String>();

        // Read list of IDs line by line
        while (scanner.hasNext()){
            ids.add(scanner.next());
        }
        scanner.close(); 
        return ids;
    }

    // Print integer array
    public static void printIntArr(int[] arr){

        List<Integer> print = new ArrayList<Integer>();

        for (int i=0; i<arr.length; i++) {
            print.add(arr[i]);
        }
        System.out.println(print);
    }

}


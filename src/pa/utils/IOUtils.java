package pa.utils;

import java.io.*;
import java.util.*;


public class IOUtils {

    // Read text file
    public static String readText(String fileName) throws IOException {

        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line;
        String text = "";
        while((line = buf.readLine()) != null){
            text += line + " ";
        }
        return text;
    }

    // Read text line by line
    public static List<String> readLines(String fileName) throws IOException {

        Scanner scanner = new Scanner(new File(fileName));
        List<String> lines = new ArrayList<String>();

        // Read list of strings line by line
        while (scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }
        scanner.close(); 
        return lines;
    }

    // Read documents line by line
    public static List<List<String>> readDocs(String fileName) throws IOException {

        Scanner scanner = new Scanner(new File(fileName));
        List<List<String>> docs = new ArrayList<List<String>>();

        // Read documents line by line
        while (scanner.hasNextLine()){
            // Split tokens
            List<String> toks = new ArrayList<String>();
            for(String tok: scanner.nextLine().split(" ")) {
                toks.add(tok);
            }
            docs.add(toks);
        }
        scanner.close();
        return docs;
    }

    // Print integer array
    public static void printIntArr(int[] arr){
        System.out.println(Arrays.toString(arr));
    }

    // Print double array
    public static void printDoubleArr(double[] arr){
        System.out.println(Arrays.toString(arr));
    }

    // Read matrix
    public static double[][] readMatrix(String fileName) throws IOException {

        int nrows = 0;
        int ndims = 0;

        // Get number of rows
        Scanner input = new Scanner(new File(fileName));
        input.useDelimiter(",");
        while (input.hasNextLine()) {
            nrows++;
            input.nextLine();
        }
        input.close();

        // Get number of dimensions
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String[] firstLine = br.readLine().split(",");
        ndims = firstLine.length;

        // Matrix
        double[][] a = new double[nrows][ndims];

        // Read TF-IDF scores
        input = new Scanner(new File(fileName));
        input.useDelimiter(",");

        int i = 0;
        while (input.hasNextLine()) {
            Scanner c = new Scanner(input.nextLine());
            c.useDelimiter(",");
            for (int j=0; j<ndims; j++) {
                if (c.hasNextDouble()) {
                    a[i][j] = c.nextDouble();
                }
            }
            i++;
        }
        return a;
    }

    // Print matrix
    public static void printMatrix(int[][] a) {
        for (int r=0; r<a.length; r++) {
            for (int c=0; c<a[r].length; c++) {
                System.out.print(String.format("%4s", a[r][c]));
            }
            System.out.println();
        }
    }

    // Write matrix to file
    public static void writeMatrix(List<List<Double>> matrix, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for(List<Double> row: matrix) {
            for (Double score: row) {
                writer.write(score + ",");
            }
            writer.write("\n");
        }
        writer.close();
    }

    // Write list of strings to file
    public static void writeList(List<String> toks, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName); 
        for(String tok: toks) {
          writer.write(tok + "\n");
        }
        writer.close();
    }

    // Write document tokens to file
    public static void writeDocs(List<List<String>> docs, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for(List<String> doc: docs) {
            for(String tok: doc) {
                writer.write(tok + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    // Print confusion matrix
    public static void printConfusionMatrix(List<String> predLabels, List<String> trueLabels) {

        assert (predLabels.size() == trueLabels.size());

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("airline_safety", 1);
        map.put("amphertamine", 2);
        map.put("china_spy_plan_captives", 3);
        map.put("hoof_mouth_disease", 4);
        map.put("iran_nuclear", 5);
        map.put("korea_nuclear", 6);
        map.put("mortgage_rates", 7);
        map.put("ocean_pollution", 8);
        map.put("satanic_cult", 9);
        map.put("storm_irene", 10);
        map.put("volcano", 11);
        map.put("saddam_hussein", 12);
        map.put("kim_jong_un", 13);
        map.put("predictive_analytics", 14);
        map.put("irma_harvey", 15);

        int[][] matrix = new int[15][15];

        for (int i=0; i<predLabels.size(); i++) {

            int row = map.get(predLabels.get(i));
            int col = map.get(trueLabels.get(i));

            matrix[row-1][col-1]++;
        }
        printMatrix(matrix);
    }
    
}


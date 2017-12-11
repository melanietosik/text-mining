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
        while(input.hasNextLine()) {
            Scanner c = new Scanner(input.nextLine());
            c.useDelimiter(",");
            for(int j=0; j<ndims; j++) {
                if(c.hasNextDouble()) {
                    a[i][j] = c.nextDouble();
                }
            }
            i++;
        }
        return a;
    }
    
}


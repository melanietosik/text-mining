package pa.tfidf;

import java.io.*;
import java.util.*;

import pa.nlp.CoreNLP;


public class Pipeline {

	public static void main(String[] args) throws IOException {

		// List data folder contents
        File[] data = new File(args[0]).listFiles();

        // Map documents to list of tokens
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (File folder: data) {

            if (!folder.getName().startsWith("C")) {
                continue;
            }

            File[] docs;
            docs = folder.listFiles();

            for (File doc: docs) {

            	String id = folder + "_" + doc.getName();
            	id = id.substring(0, id.lastIndexOf("."));

            	// Read text
            	String text = readFile(doc.getAbsolutePath());

            	// Process document using CoreNLP
            	CoreNLP nlp = new CoreNLP(text);
            	// Get document tokens
                List<String> toks = nlp.getToks();
                System.out.println(toks);
                // Map ID to tokens
                map.put(id, toks);
            }
		}
	}

	// Read text file
	public static String readFile(String file) throws IOException {

	    InputStream is = new FileInputStream(file);
	    BufferedReader buf = new BufferedReader(new InputStreamReader(is));

	    String line;
	    String text = "";

	    // Read text line by line
	    while((line = buf.readLine()) != null){
	        text += line + " ";
	    }
	    return text;
	}
}

package pa.tfidf;

import java.io.*;
import java.util.*;

import pa.nlp.*;
import pa.utils.IOUtils;


public class Pipeline {

    private static IOUtils utils = new IOUtils();

    public static void main(String[] args) throws IOException {

        // List data folder contents
        File[] data = new File(args[0]).listFiles();

        // Map documents to list of tokens
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        System.out.println("Processing documents...");
        for (File folder: data) {

            String folderName = folder.getName();
            if (folderName.startsWith(".")) {
                continue;
            }
            System.out.println(folderName);

            File[] docs;
            docs = folder.listFiles();

            for (File doc: docs) {

                String id = folder.getName() + "_" + doc.getName();
                id = id.substring(0, id.lastIndexOf("."));

                // Read text
                String text = utils.readText(doc.getAbsolutePath());
                // Process document using CoreNLP
                CoreNLP nlp = new CoreNLP(text);
                // Get document tokens
                List<String> toks = nlp.getToks();
                // Map ID to tokens
                map.put(id, toks);
            }
        }

        // Sliding window
        System.out.println("Sliding window...");
        SlidingWindow ngrams = new SlidingWindow(map);
        map = ngrams.processNgrams();

        // TF-IDF
        System.out.println("Calculating TF-IDF scores...");
        TFIDF scorer = new TFIDF(map);
        // Compute matrix and topics
        scorer.computeTfidfMatrix();

        System.out.println("Done");
    }

}


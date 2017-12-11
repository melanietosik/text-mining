package pa.kmeans;

import java.util.*;


public class Distance {

    Distance(){};

    double[][] _matrix;
    Distance(double[][] matrix) {
        _matrix = matrix;
    }

    public List<Integer> getSimilar(int idx, int k, String metric) {

        if (!metric.equals("euclid") && !metric.equals("cosine")) {
            throw new IllegalArgumentException("Invalid metric");
        }

        HashMap<Integer, Double> map = new HashMap<Integer, Double>();

        for (int i=0; i<_matrix.length; i++) {
            if (i == idx) {
                continue;
            }
            double sim = 0.0;
            if (metric.equals("euclid")) {
                sim = euclid(_matrix[i], _matrix[idx]);
            } else {
                sim = cosine(_matrix[i], _matrix[idx]);
            }
            map.put(i, sim);
        }

        List<Integer> similar = new ArrayList<Integer>(sortByValue(map).keySet());
        Collections.reverse(similar);

        return similar.subList(0, k);
    }

    // Euclidean distance
    public double euclid(double[] vec1, double[] vec2) {

        assert vec1.length == vec2.length;
        double sum = 0.0;

        for (int i=0; i<vec1.length; i++) {
            sum = sum + Math.pow((vec1[i]) - vec2[i], 2);
        }
        return -1 * Math.sqrt(sum);
    }
    
    // Cosine similarity
    public double cosine(double[] vec1, double[] vec2) {

        assert vec1.length == vec2.length;
        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i=0; i<vec1.length; i++) {
            dot += vec1[i] * vec2[i];
            norm1 += Math.pow(vec1[i], 2);
            norm2 += Math.pow(vec2[i], 2);
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Sort map by values
    @SuppressWarnings("unchecked")
    public static HashMap sortByValue(HashMap map) {

        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                    .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

}


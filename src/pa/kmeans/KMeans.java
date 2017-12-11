package pa.kmeans;

import java.util.*;


public class KMeans {

    double[][] _matrix;
    String _metric;
    boolean _pp;

    KMeans(double[][] matrix, String metric, boolean pp) {
        _matrix = matrix;
        _metric = metric;
        _pp = pp;
    }

    private int _nrows;
    private int _ndims;
    private double[][] _centroids;
    private int[] _labels;

    Distance _dist = new Distance();

    // Cluster documents
    public int[] clusterDocs(int k) {

        if (!_metric.equals("euclid") && !_metric.equals("cosine")) {
            throw new IllegalArgumentException("invalid metric");
        }

        // Matrix size
        _nrows = _matrix.length;
        _ndims = _matrix[0].length;

        // Initialize centroids (simple or ++)
        if (_pp && _metric.equals("euclid")) {
            _centroids = initCentroidsPlusPlus(k);
        } else {
            _centroids = initCentroids(k);
        }

        double [][] c1 = _centroids;
        int epoch = 0;
        
        while (true) {

            _centroids = c1;
            _labels = new int[_nrows];

            for (int i=0; i<_nrows; i++) {
                _labels[i] = nearest(_matrix[i], k);
            }

            c1 = updateCentroids(k);
            epoch++;

            if ((epoch >= 100) || converge(_centroids, c1)) {
                break;
            }
        }
        System.out.println("Converged at epoch " + epoch);
        return _labels;
    }

    // Randomly initialize centroids
    private double[][] initCentroids(int k) {

        double[][] _centroids = new double[k][_ndims];

        ArrayList<Integer> idx = new ArrayList<Integer>();

        for (int i=0; i<k; i++) {
            int rand;
            do {
                rand = (int)(Math.random() * _nrows);
            } while (idx.contains(rand));
            idx.add(rand);

            _centroids[i] = new double[_ndims];
            for (int j=0; j<_ndims; j++) {
                _centroids[i][j] = _matrix[rand][j];
            }
        }
        System.out.println("Centroids " + idx);
        return _centroids;
    }

    // Randomly initialize centroids++
    private double[][] initCentroidsPlusPlus(int k) {

        double[][] _centroids = new double[k][_ndims];
        List<Integer> used = new ArrayList<Integer>();
        List<Integer> cent = new ArrayList<Integer>();

        // Choose first centroid uniformly at random from the data points
        Random rnd = new Random();
        int idx = rnd.nextInt(_nrows);
        
        for(int i=0; i<_ndims; i++) {
            _centroids[0][i] = _matrix[idx][i];
        }
        used.add(idx);
        cent.add(idx);

        // For each x, compute D(x), Euclidean distance between x and the nearest centroid
        for (int c=1; c<k; c++) {

            double[] d2 = new double[_nrows];   // Distances to closest centroid
            int nc = -1;                        // Index of new centroid

            // Iterate over document vectors
            for (int i=0; i<_nrows; i++) {
                // Avoid duplicates
                if (used.contains(i) == true) continue;
                // Compute distances from _matrix[i] to each existing centroid
                double[] distances = new double[c];
                for (int j=0; j<c; j++) {
                    distances[j] = -1 * _dist.euclid(_matrix[i], _centroids[c]);
                }
                // Get index of closest centroid
                int m = minIdx(distances);
                // Save distance-squared
                d2[i] = distances[m] * distances[m];
            }

            // Choose one data point as new centroid, based on the squared distances
            double p = rnd.nextDouble();

            double sum = 0.0;   // Sum of squared distances
            for (int i=0; i<d2.length; i++) {
                sum += d2[i];
            }
            double cumsum = 0.0;

            int ii = 0;
            int sanity = 0;
            while (sanity < _nrows * 2) {
                // Cumulative sum
                cumsum += d2[ii] / sum;
                if (cumsum >= p && used.contains(ii) == false) {
                    nc = ii;    // New chosen index
                    used.add(nc);
                    break;
                }
                ii++;
                if (ii >= d2.length) ii = 0;
                sanity++;
            }

            // Copy centroid vector
            for (int i=0; i<_ndims; i++) {
                _centroids[c][i] = _matrix[nc][i];
            }
            cent.add(nc);
        }
        assert(_centroids.length == k);
        System.out.println("Centroids " + cent);
        return _centroids;
    }

    // Compute nearest centroid for each document vector
    private int nearest(double[] vec, int k) {

        double max = 0.0;
        if (_metric.equals("euclid")) {
            max = _dist.euclid(vec, _centroids[0]);
        } else {
            max = _dist.cosine(vec, _centroids[0]);
        }

        int label = 0;
        for (int i=1; i<k; i++) {

            double dist = 0.0;
            if (_metric.equals("euclid")) {
                dist = _dist.euclid(vec, _centroids[i]);
            } else {
                dist = _dist.cosine(vec, _centroids[i]);
            }
            if (dist > max) {
                max = dist;
                label = i;
            }
        }
        return label;
    }

    // Update centroids
    private double[][] updateCentroids(int k) {

        double[][] newc = new double[k][];
        int[] cnt = new int[k];

        // Initialize clusters to zero
        for (int i=0; i<k; i++) {
            cnt[i] = 0;
            newc[i] = new double[_ndims];
                for (int j=0; j<_ndims; j++)
                    newc[i][j] = 0;
        }

        for (int i=0; i<_nrows; i++) {
            int cn = _labels[i];
            for (int j=0; j<_ndims; j++) {
                newc[cn][j] += _matrix[i][j];
            }
            cnt[cn]++;
        }

        // Average
        for (int i=0; i<k; i++) {
            for (int j=0; j<_ndims; j++) {
                newc[i][j] /= cnt[i];
            }
        }
        return newc;
    }

    // Converge when clusters stop changing
    private boolean converge(double [][] c1, double [][] c2) {

        assert (c1.length == c2.length);

        for (int i=0; i<c1.length; i++) {
            for (int j=0; j<c2.length; j++) {
                if (c1[i][j] != c2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Get minimum index in array
    private static int minIdx(double[] distances) {
      
        int idx = 0;
        double min = distances[0];

        for (int k=0; k<distances.length; k++) {
            if (distances[k] < min) {
                min = distances[k];
                idx = k;
            }
        }
        return idx;
    }

}


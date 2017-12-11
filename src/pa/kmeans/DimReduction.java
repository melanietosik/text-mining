package pa.kmeans;

import java.util.*;

import Jama.Matrix;
import Jama.SingularValueDecomposition;


public class DimReduction {

    double[][] matrix;
    DimReduction(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] reduceDimensions(int pc) {

        assert (pc < matrix.length);

        Matrix M = new Matrix(matrix);

        // Transpose M since Jama expects _nrows >= _ndims
        Matrix A = M.transpose();
        SingularValueDecomposition s = A.svd();

        Matrix U = s.getV().transpose();        // Swap and transpose
        Matrix S = s.getS().transpose();        // Transpose
        Matrix V = s.getU().transpose();        // Swap and transpose

        // Project matrix into lower-dimensional space
        Matrix USigma = U.times(S);

        double[][] projected = USigma.transpose().getArray();
        assert (projected.length == matrix.length);

        double[][] reduced = new double[projected.length][pc];
        for (int i=0; i<projected.length; i++) {
            for (int j=0; j<pc; j++) {
                reduced[i][j] = projected[i][j];
            }
        }
        assert (reduced.length == pc);
        return reduced;
    }

}


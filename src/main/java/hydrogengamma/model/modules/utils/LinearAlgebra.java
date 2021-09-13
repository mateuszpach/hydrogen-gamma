package hydrogengamma.model.modules.utils;

import hydrogengamma.model.variables.MatrixVariable;
import hydrogengamma.utils.Pair;


public interface LinearAlgebra {

    // for A finds L, U and permutation p such that LU = p(A)
    static Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> decompositionLUPivoted(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new ModuleException("Matrix is not square, but an operation requires it to be so");

        Integer[] permutation = identityPermutation(n);

        Pair<MatrixVariable, MatrixVariable> initialLU = prepareLU(matrix);
        double[][] L = initialLU.first.getValue();
        double[][] U = initialLU.second.getValue();

        for (int i = 0; i < n; i++) {

            int maxIdx = rowWithMaxVal(U, i);
            swapRows(U, i, maxIdx);
            swapRows(L, i, maxIdx);
            swapElems(permutation, i, maxIdx);

            performGaussElimStep(L, U, i);
        }

        return new Pair<>(new Pair<>(new MatrixVariable(L), new MatrixVariable(U)), permutation);
    }

    static void performGaussElimStep(double[][] L, double[][] U, int i) {
        // changed to double[][] from Matrix,
        // I assume author must've been drunk to keep converting array to MatrixVar just to use it like array
        // and than convert it back, all within utility method that has no point nor benefit using Variable

        //also it was the only method explicitly modifying value  of Variable
        int n = L.length;
        for (int j = i + 1; j < n; j++) {
            if (U[j][i] == 0.0)
                continue;

            L[j][i] = U[j][i] / U[i][i];

            for (int k = i; k < n; k++) {
                U[j][k] -= L[j][i] * U[i][k];
            }
        }

        L[i][i] = 1.0;
    }


    static Pair<MatrixVariable, MatrixVariable> prepareLU(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();

        double[][] L = new double[n][m];
        double[][] U = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                L[i][j] = 0.0;
                U[i][j] = 0.0;
            }
        }

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                U[i][j] = matrix.get(i, j);

        return new Pair<>(new MatrixVariable(L), new MatrixVariable(U));
    }

    static MatrixVariable solveSystemUpper(MatrixVariable U, MatrixVariable b, Integer[] perm) {
        int n = b.rowsNum();
        double[][] ans = new double[n][1];
        for (int i = n - 1; i >= 0; i--) {
            ans[i][0] = b.get(perm[i], 0);
            for (int j = i + 1; j < n; j++) {
                ans[i][0] -= ans[j][0] * U.get(i, j);
            }
            ans[i][0] /= U.get(i, i);
        }
        return new MatrixVariable(ans);
    }

    static MatrixVariable solveSystemLower(MatrixVariable L, MatrixVariable b, Integer[] perm) {
        int n = b.rowsNum();
        double[][] ans = new double[n][1];
        for (int i = 0; i < n; i++) {
            ans[i][0] = b.get(perm[i], 0);
            for (int j = 0; j < i; j++) {
                ans[i][0] -= ans[j][0] * L.get(i, j);
            }
            ans[i][0] /= L.get(i, i);
        }
        return new MatrixVariable(ans);
    }

    static Integer[] identityPermutation(int n) {
        Integer[] id = new Integer[n];
        for (int i = 0; i < n; i++)
            id[i] = i;
        return id;
    }

    static int permSign(Integer[] a) {
        int s = 1;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j])
                    s *= -1;
            }
        }
        return s;
    }

    private static int rowWithMaxVal(double[][] A, int beginRow) {
        double maxi = Math.abs(A[beginRow][beginRow]);
        int idx = beginRow;
        for (int i = beginRow + 1; i < A.length; i++) {
            if (Math.abs(A[i][beginRow]) > maxi) {
                idx = i;
                maxi = Math.abs(A[i][beginRow]);
            }
        }
        return idx;
    }

    private static void swapRows(double[][] A, int row1, int row2) {
        double[] temp = A[row1].clone();
        A[row1] = A[row2];
        A[row2] = temp;
    }

    private static void swapElems(Integer[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
}

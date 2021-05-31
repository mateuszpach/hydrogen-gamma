package model.modules.utils;

import utils.Pair;
import model.variables.MatrixVariable;


public interface LinearAlgebra {

    // for A finds L, U and permutation p such that LU = p(A)
    static Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> decompositionLUPivoted(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new MatrixNotSquareException();

        Integer[] permutation = identityPermutation(n);

        Pair<MatrixVariable, MatrixVariable> initialLU = prepareLU(matrix);
        double[][] L = initialLU.first.value;
        double[][] U = initialLU.second.value;

        for (int i = 0; i < n; i++) {

            int maxIdx = rowWithMaxVal(U, i);
            swapRows(U, i, maxIdx);
            swapRows(L, i, maxIdx);
            swapElems(permutation, i, maxIdx);

            performGaussElimStep(new MatrixVariable(L), new MatrixVariable(U), i);
        }

        return new Pair<>(new Pair<>(new MatrixVariable(L), new MatrixVariable(U)), permutation);
    }

    static void performGaussElimStep(MatrixVariable L, MatrixVariable U, int i) {
        int n = L.value.length;
        for (int j = i + 1; j < n; j++) {
            if (U.value[j][i] == 0.0)
                continue;

            L.value[j][i] = U.value[j][i] / U.value[i][i];

            for (int k = i; k < n; k++) {
                U.value[j][k] -= L.value[j][i] * U.value[i][k];
            }
        }

        L.value[i][i] = 1.0;
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

    class MatrixNotSquareException extends RuntimeException {}
}

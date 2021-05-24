package model.modules;

import utils.Pair;
import model.variables.MatrixVariable;


public interface LinearAlgebra {


    public static Double determinant(MatrixVariable A) {
        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = decompositionLUPivoted(A);
        MatrixVariable U = lu.first.second;
        double det = 1.0;
        for (int i = 0; i < U.rowsNum(); i++) {
            det *= U.get(i, i);
        }
        return det;
    }

    // solves linear system of shape Ax = b, where A is square matrix
    public static MatrixVariable solveLinearSystem(MatrixVariable A, MatrixVariable b) {
        Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> lu = decompositionLUPivoted(A);
        MatrixVariable L = lu.first.first;
        MatrixVariable U = lu.first.second;
        Integer[] perm = lu.second;
        int n = A.rowsNum();

        double[][] Y = solveSystemLower(L, b, perm);
        MatrixVariable yMatrix = new MatrixVariable(Y);
        double[][] ans = solveSystemUpper(U, yMatrix, identity(n));

        return new MatrixVariable(ans);
    }

    // given A finds matrices L and U such that LU = A, L is lower triangular and U is upper triangular
    public static Pair<MatrixVariable, MatrixVariable> decompositionLU(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new MatrixNotSquareException();

        Pair<double[][], double[][]> initialLU = prepareLU(matrix);
        double[][] L = initialLU.first;
        double[][] U = initialLU.second;

        for (int i = 0; i < n; i++) {
            performGaussElimStep(L, U, i);
        }

        return new Pair<MatrixVariable, MatrixVariable>(new MatrixVariable(L), new MatrixVariable(U));
    }


    // PRIVATE


    // for A finds L, U and permutation p such that LU = p(A)
    private static Pair<Pair<MatrixVariable, MatrixVariable>, Integer[]> decompositionLUPivoted(MatrixVariable matrix) {
        int n = matrix.rowsNum();
        int m = matrix.colsNum();
        if (n != m) throw new MatrixNotSquareException();

        Integer[] permutation = identity(n);

        Pair<double[][], double[][]> initialLU = prepareLU(matrix);
        double[][] L = initialLU.first;
        double[][] U = initialLU.second;


        for (int i = 0; i < n; i++) {

            int maxIdx = rowWithMaxVal(U, i);
            swapRows(U, i, maxIdx);
            swapRows(L, i, maxIdx);
            swapElems(permutation, i, maxIdx);

            performGaussElimStep(L, U, i);
        }

        return new Pair<>(new Pair<>(new MatrixVariable(L), new MatrixVariable(U)), permutation);
    }

    private static void performGaussElimStep(double[][] L, double[][] U, int i) {
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

    private static Pair<double[][], double[][]> prepareLU(MatrixVariable matrix) {
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

        return new Pair<>(L, U);
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

    private static double[][] solveSystemUpper(MatrixVariable U, MatrixVariable b, Integer[] perm) {
        int n = b.rowsNum();
        double[][] ans = new double[n][1];
        for (int i = n - 1; i >= 0; i--) {
            ans[i][0] = b.get(perm[i], 0);
            for (int j = i + 1; j < n; j++) {
                ans[i][0] -= ans[j][0] * U.get(i, j);
            }
            ans[i][0] /= U.get(i, i);
        }
        return ans;
    }

    private static double[][] solveSystemLower(MatrixVariable L, MatrixVariable b, Integer[] perm) {
        int n = b.rowsNum();
        double[][] ans = new double[n][1];
        for (int i = 0; i < n; i++) {
            ans[i][0] = b.get(perm[i], 0);
            for (int j = 0; j < i; j++) {
                ans[i][0] -= ans[j][0] * L.get(i, j);
            }
            ans[i][0] /= L.get(i, i);
        }
        return ans;
    }

    private static Integer[] identity(int n) {
        Integer[] id = new Integer[n];
        for (int i = 0; i < n; i++)
            id[i] = i;
        return id;
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

    public static void main(String[] args) {
        double[][] A = {{1.0, 1.0, 1.0}, {2.0, -7.0, 4.0}, {-5.0, 1.0, -1.0}};
        double[][] B = {{10.0}, {3.0}, {1.0}};

        MatrixVariable X = solveLinearSystem(new MatrixVariable(A), new MatrixVariable(B));
        System.out.println(X);
        System.out.println(determinant(new MatrixVariable(A)));
    }

    public static class MatrixNotSquareException extends RuntimeException {}
}

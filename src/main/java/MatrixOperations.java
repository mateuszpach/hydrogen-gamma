import java.util.function.Function;

public interface MatrixOperations {
    static MatrixVariable matrixMultiplication(MatrixVariable a, MatrixVariable b){
        if(a.colsNum()!=b.rowsNum())
            throw new IllegalArgumentException("Matrix sizes are mismatched.");
        double[][] c=new double[a.rowsNum()][b.colsNum()];
        for (int i=0;i<a.rowsNum();++i)
            for (int j=0;j<b.colsNum();++j)
                for (int k=0;k<a.colsNum();++k)
                    c[i][j] += a.get(i, k) * b.get(k, j);
        return new MatrixVariable(c);
    }
    static MatrixVariable matrixAddition(MatrixVariable a, MatrixVariable b){
        if(a.colsNum()!=b.colsNum() || a.rowsNum()!=b.rowsNum())
            throw new IllegalArgumentException("Matrix sizes are mismatched.");
        double[][] c=new double[a.rowsNum()][a.colsNum()];
        for (int i=0;i<a.rowsNum();++i)
            for (int j=0;j<a.colsNum();++j)
                    c[i][j]= a.get(i,j)+b.get(i,j);
        return new MatrixVariable(c);
    }
    static MatrixVariable matrixScalar(MatrixVariable a, NumericVariable b){
        double[][] c=new double[a.rowsNum()][a.colsNum()];
        for (int i=0;i<a.rowsNum();++i)
            for (int j=0;j<a.colsNum();++j)
                c[i][j]= a.get(i,j)*b.value;
        return new MatrixVariable(c);
    }
    static MatrixVariable matrixScalar(NumericVariable b, MatrixVariable a){
        double[][] c=new double[a.rowsNum()][a.colsNum()];
        for (int i=0;i<a.rowsNum();++i)
            for (int j=0;j<a.colsNum();++j)
                c[i][j]= a.get(i,j)*b.value;
        return new MatrixVariable(c);
    }
    //rather as developer tool than something customer should use
    //could implement matrix scalar and alike
    static MatrixVariable matrixApplyLambda(MatrixVariable a, Function<Double, Double> b){
        double[][] c=new double[a.rowsNum()][a.colsNum()];
        for (int i=0;i<a.rowsNum();++i)
            for (int j=0;j<a.colsNum();++j)
                c[i][j]= b.apply(a.get(i,j));
        return new MatrixVariable(c);
    }
}

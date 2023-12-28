public class matrixEngineThreaded extends Thread {
    private long[][] matrix1;
    private long[][] matrix2;
    private long[][] resultMatrix;
    private int startRow;
    private int endRow;

    public matrixEngineThreaded(long[][] matrix1, long[][] matrix2, long[][] resultMatrix, int startRow, int endRow) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.resultMatrix = resultMatrix;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        multiply();
    }

    private void multiply() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix2.length; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }
}

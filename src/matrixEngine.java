import java.util.Random;

public class matrixEngine {
    public matrixResult GenerateMatrixes(){
        long[][] matrix1 = new long[1000][1000];
        long[][] matrix2 = new long[1000][1000];

        fillMatrix(matrix1);
        fillMatrix(matrix2);

        return new matrixResult(matrix1, matrix2);
    }

    public void fillMatrix(long[][] matrix){
        Random random = new Random();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }
    }

    public long[][] multiplyMatrices(long[][] matrix1, long[][] matrix2) {
        long[][] resultMatrix = new long[1000][1000];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix2.length; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return resultMatrix;
    }
}

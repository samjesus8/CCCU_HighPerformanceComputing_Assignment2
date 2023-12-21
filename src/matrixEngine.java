import java.util.Random;

public class matrixEngine {
    public matrixResult GenerateBaseMatrixes(){
        //This method should only be used at the very start to generate 2 base matrixes to start the multiplying process
        long[][] matrix1 = new long[1000][1000];
        long[][] matrix2 = new long[1000][1000];

        //This method generates numbers between 1-100 and fills every section of the matrix
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
        long[][] resultMatrix = new long[1000][1000]; //Create empty 1000x1000 matrix to store the result

        //For loop through each dimension of the matrix and perform dot product multiplication
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

class matrixResult {
    //This class is used to be able to return 2 values at once in a method
    long[][] matrix1;
    long[][] matrix2;

    public matrixResult(long[][] matrix1, long[][] matrix2){
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
    }
}

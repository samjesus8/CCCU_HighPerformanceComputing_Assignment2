import java.util.Random;

public class task1 {
    public matrixResult GenerateMatrixes(){
        int[][] matrix1 = new int[10][10];
        int[][] matrix2 = new int[10][10];

        fillMatrix(matrix1);
        fillMatrix(matrix2);

        return new matrixResult(matrix1, matrix2);
    }

    public void fillMatrix(int[][] matrix){
        Random random = new Random();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }
    }

    public String MultiplyMatrixes(){
        return "Test";
    }
}

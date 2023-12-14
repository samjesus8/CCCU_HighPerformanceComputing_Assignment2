public class App {
    public static void main(String[] args) throws Exception {
        //Initialize the main Matrix generation class
        var MatrixEngine = new matrixEngine();

        //Execute method to generate 2 basic matrixes
        var matrixes = MatrixEngine.GenerateMatrixes();

        //Display the 2 starting matrixes which will be multiplied together to give our first iteration
        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrixes.matrix1, 10, 10);
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrixes.matrix2, 10, 10);

        //Executing the multiplication method to get our first iteration
        var result1 = MatrixEngine.multiplyMatrices(matrixes.matrix1, matrixes.matrix2);
        System.out.println("1st Multiplication: \n");
        printMatrixPreview(result1, 10, 10);

        //Now a 2nd randomly generated 1000x1000 matrix will be created
        int[][] secondIterationMatrix = new int[1000][1000];
        MatrixEngine.fillMatrix(secondIterationMatrix);

        //We will multiply this matrix by the result of the 1st multiplication
        var result2 = MatrixEngine.multiplyMatrices(result1, secondIterationMatrix);
        System.out.println("2nd Multiplication: \n");
        printMatrixPreview(result2, 10, 10);

        //And we will do this a 3rd time, for our 3rd iteration
        int[][] thirdIterationMatrix = new int[1000][1000];
        MatrixEngine.fillMatrix(thirdIterationMatrix);

        //Multiplying the 3rd matrix by the result of the 2nd multiplication
        var result3 = MatrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
        System.out.println("3rd Multiplication: \n");
        printMatrixPreview(result3, 10, 10);
    }

    //Method to display a matrix with the option to only display a specific portion
    //This is because a whole 1000x1000 matrix can be cumbersome to view entirely
    private static void printMatrixPreview(int[][] matrix, int previewRows, int previewCols) {
        for (int i = 0; i < Math.min(previewRows, matrix.length); i++) {
            for (int j = 0; j < Math.min(previewCols, matrix[i].length); j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}

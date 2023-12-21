import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Samuel Jesuthas - HPC Assignment 2 (Matrix Multiplication) \n" +
                            "====================================================================================================\n" +
                            "This program will only display the first 10x10 portion of the 1000x1000 matrix \n" +
                            "This is because displaying a whole 1000x1000 matrix can be cumbersome during the testing process \n" +
                            "However, you can change edit the size of the displayed matrix by editing \nthe previewRows/previewCols whenever the method printMatrixPreview() is executed \n"+
                            "====================================================================================================\n\n");

        Scanner threadingInputOption = new Scanner(System.in);
        System.out.println("Please choose an option to run this program \n" +
                            "1. Run with No Threading \n" +
                            "2. Run with Threading");
        int input = threadingInputOption.nextInt();

        if (input == 1){
            performMatrixMultiplicationNoThreading();
        }
        else if (input == 2){
            performMatrixMultiplicationThreading();
        }
        else{
            System.out.println("Please enter a valid option number!!!");
        }
    }

    private static void performMatrixMultiplicationNoThreading() {
        //Initialize the main Matrix generation class
        var MatrixEngine = new matrixEngine();

        //Execute method to generate 2 basic matrixes
        var matrixes = MatrixEngine.GenerateBaseMatrixes();

        //Display the 2 starting matrixes which will be multiplied together to give our first iteration
        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrixes.matrix1, 10, 10); //We will only display the first 10x10 portion of the matrix
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrixes.matrix2, 10, 10);

        //Executing the multiplication method to get our first iteration
        var result1 = MatrixEngine.multiplyMatrices(matrixes.matrix1, matrixes.matrix2);
        System.out.println("1st Multiplication: \n");
        printMatrixPreview(result1, 10, 10);

        //Now a 2nd randomly generated 1000x1000 matrix will be created
        long[][] secondIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(secondIterationMatrix);

        //We will multiply this matrix by the result of the 1st multiplication
        var result2 = MatrixEngine.multiplyMatrices(result1, secondIterationMatrix);
        System.out.println("2nd Multiplication: \n");
        printMatrixPreview(result2, 10, 10);

        //And we will do this a 3rd time, for our 3rd iteration
        long[][] thirdIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(thirdIterationMatrix);

        //Multiplying the 3rd matrix by the result of the 2nd multiplication
        var result3 = MatrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
        System.out.println("3rd Multiplication: \n");
        printMatrixPreview(result3, 10, 10);
    }

    private static void performMatrixMultiplicationThreading() {
        System.out.println("Test");
    }

    //Method to display a matrix with the option to only display a specific portion
    //This is because a whole 1000x1000 matrix can be cumbersome to view entirely
    private static void printMatrixPreview(long[][] matrix, int previewRows, int previewCols) {
        for (int i = 0; i < Math.min(previewRows, matrix.length); i++) {
            for (int j = 0; j < Math.min(previewCols, matrix[i].length); j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}

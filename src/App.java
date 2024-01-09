import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Samuel Jesuthas - HPC Assignment 2 (Matrix Multiplication) \n" +
                            "====================================================================================================\n" +
                            "This program will only display the first 10x10 portion of the 1000x1000 matrix \n" +
                            "This is because displaying a whole 1000x1000 matrix can be cumbersome during the testing process \n" +
                            "However, you can change edit the size of the displayed matrix by editing \nthe previewRows/previewCols whenever the method printMatrixPreview() is executed \n"+
                            "====================================================================================================\n\n");

        //Ask the user if they want to run the program threaded/non-threaded
        Scanner threadingInputOption = new Scanner(System.in);
        System.out.println("Please choose an option to run this program \n" +
                            "1. Run /w No Threading \n" +
                            "2. Run /w Threading \n" +
                            "3. Run using Thread Pool \n" +
                            "4. Verify Threaded vs Non-Threaded \n" +
                            "5. Exit Program");
        int input = threadingInputOption.nextInt();

        long startTime, endTime; //These 2 variables will be used to show how long it took for our methods to execute
        
        matrixEngine MatrixEngine = new matrixEngine();
        var startingMatrix = MatrixEngine.GenerateBaseMatrixes();

        if (input == 1) {
            startTime = System.currentTimeMillis();
            performMatrixMultiplicationNoThreading(startingMatrix.matrix1, startingMatrix.matrix2);
            endTime = System.currentTimeMillis();
        } else if (input == 2) {
            startTime = System.currentTimeMillis();
            performMatrixMultiplicationThreaded(startingMatrix.matrix1, startingMatrix.matrix2);
            endTime = System.currentTimeMillis();
        } else if (input == 3) {
            startTime = System.currentTimeMillis();
            performMatrixMultiplicationThreadPool();
            endTime = System.currentTimeMillis();
        } else if (input == 4) {
            startTime = System.currentTimeMillis();
            var nonThreaded = performMatrixMultiplicationNoThreading(startingMatrix.matrix1, startingMatrix.matrix2);
            var threaded = performMatrixMultiplicationThreaded(startingMatrix.matrix1, startingMatrix.matrix2);

            verifyResult(threaded, nonThreaded);

            endTime = System.currentTimeMillis();
        } else if (input == 5) {
            System.out.println("Exiting Program...");
            threadingInputOption.close();
            return; // Exit the program
        } else {
            System.out.println("Please enter a valid option number!!!");
            threadingInputOption.close();
            return; // Exit the program
        }

        long executionTime = endTime - startTime; //Subtracting the times will give us the actual execution time of the program
        System.out.println("\nProgram Execution time: " + executionTime + " milliseconds");

        threadingInputOption.close();
    }

    private static long[][] performMatrixMultiplicationNoThreading(long[][] matrix1, long[][] matrix2) {
        var MatrixEngine = new matrixEngine();

        //Display the 2 starting matrixes which will be multiplied together to give our first iteration
        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrix1, 10, 10); //We will only display the first 10x10 portion of the matrix
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrix2, 10, 10);

        //Executing the multiplication method to get our first iteration
        var result1 = MatrixEngine.multiplyMatrices(matrix1, matrix2);
        System.out.println("1st Multiplication /w No Threading: \n");
        printMatrixPreview(result1, 10, 10);

        //Now a 2nd randomly generated 1000x1000 matrix will be created
        long[][] secondIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(secondIterationMatrix);

        //We will multiply this matrix by the result of the 1st multiplication
        var result2 = MatrixEngine.multiplyMatrices(result1, secondIterationMatrix);
        System.out.println("2nd Multiplication /w No Threading: \n");
        printMatrixPreview(result2, 10, 10);

        //And we will do this a 3rd time, for our 3rd iteration
        long[][] thirdIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(thirdIterationMatrix);

        //Multiplying the 3rd matrix by the result of the 2nd multiplication
        var result3 = MatrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
        System.out.println("3rd Multiplication /w No Threading: \n");
        printMatrixPreview(result3, 10, 10);

        //Return the final result
        return result3;
    }

    private static long[][] performMatrixMultiplicationThreaded(long[][] matrix1, long[][] matrix2) {
        //Initialize Class
        var MatrixEngine = new matrixEngine();

        //Print out the first 10x10 portion of each matrix
        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrix1, 10, 10);
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrix2, 10, 10);

        //Multiplication 1 (Using 4 threads)
        var result1 = MatrixEngine.multiplyMatricesThreaded(matrix1, matrix2, 4);
        System.out.println("1st Multiplication /w Threading: \n");
        printMatrixPreview(result1, 10, 10);

        //Multiplication 2
        long[][] secondIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(secondIterationMatrix);

        var result2 = MatrixEngine.multiplyMatricesThreaded(result1, secondIterationMatrix, 4);
        System.out.println("2nd Multiplication /w Threading: \n");
        printMatrixPreview(result2, 10, 10);

        //Multiplication 3
        long[][] thirdIterationMatrix = new long[1000][1000];
        MatrixEngine.fillMatrix(thirdIterationMatrix);

        var result3 = MatrixEngine.multiplyMatricesThreaded(result2, thirdIterationMatrix, 4);
        System.out.println("3rd Multiplication /w Threading: \n");
        printMatrixPreview(result3, 10, 10);

        //Return the final result
        return result3;
    }

    private static void performMatrixMultiplicationThreadPool() {
        var matrixThreadingEngine = new matrixEngineThreadPool();
        matrixThreadingEngine.performMatrixMultiplicationThreaded();
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

    private static void verifyResult(long[][] result, long[][] goldStandardResult) {
        boolean verificationPassed = true;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (result[i][j] != goldStandardResult[i][j]) {
                    verificationPassed = false;
                    break;
                }
            }
            if (!verificationPassed) {
                break;
            }
        }

        if (verificationPassed) {
            System.out.println("Verification successful! Results match the gold standard.");
        } else {
            System.err.println("Verification failed! Results differ.");
        }
    }
}

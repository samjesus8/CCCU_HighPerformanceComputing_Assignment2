import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class matrixEngineThreadPool {
    private static final int MATRIX_SIZE = 1000; // Working with our 1000x1000 matrix
    private static final int NUM_THREADS = 4; // Number of threads which we can change

    public void performMatrixMultiplicationThreaded() {
        //Time variables
        long startTime, endTime;

        //Time at the start of the method
        startTime = System.currentTimeMillis();

        //Generate our 2 base matrixes
        var matrixEngine = new matrixEngine();
        var matrices = matrixEngine.GenerateBaseMatrixes();

        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrices.matrix1, 10, 10);
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrices.matrix2, 10, 10);

        //Then run our threads to perform this multiplication
        long[][] result1 = performMatrixMultiplication(matrices.matrix1, matrices.matrix2, NUM_THREADS);
        System.out.println("1st Multiplication with Threading: \n");
        printMatrixPreview(result1, 10, 10);

        //2nd Iteration
        long[][] secondIterationMatrix = new long[MATRIX_SIZE][MATRIX_SIZE];
        matrixEngine.fillMatrix(secondIterationMatrix);
        long[][] result2 = performMatrixMultiplication(result1, secondIterationMatrix, NUM_THREADS);
        System.out.println("2nd Multiplication with Threading: \n");
        printMatrixPreview(result2, 10, 10);

        //3rd Iteration
        long[][] thirdIterationMatrix = new long[MATRIX_SIZE][MATRIX_SIZE];
        matrixEngine.fillMatrix(thirdIterationMatrix);
        long[][] result3 = performMatrixMultiplication(result2, thirdIterationMatrix, NUM_THREADS);
        System.out.println("3rd Multiplication with Threading: \n");
        printMatrixPreview(result3, 10, 10);

        //Time at the end of the method
        endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Thread Pool Execution time: " + executionTime + " milliseconds \n");

        // Now we verify the threaded results, against the non threaded to see if they are the same
        var goldStandardResult1 = matrixEngine.multiplyMatrices(matrices.matrix1, matrices.matrix2);
        verifyResult(result1, goldStandardResult1);

        var goldStandardResult2 = matrixEngine.multiplyMatrices(result1, secondIterationMatrix);
        verifyResult(result2, goldStandardResult2);

        var goldStandardResult3 = matrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
        verifyResult(result3, goldStandardResult3);
    }

    private static long[][] performMatrixMultiplication(long[][] matrix1, long[][] matrix2, int numThreads) {
        //Using ExecutorService because it is a built-in framework for managing threads
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        //Distributing the workload (in this case the 1000x1000 matrix) among multiple threads
        int chunkSize = MATRIX_SIZE / numThreads;

        //Making an empty 1000x1000 matrix to store the result (MATRIX_SIZE is hardcoded to 1000)
        long[][] resultMatrix = new long[MATRIX_SIZE][MATRIX_SIZE];
        Runnable[] tasks = new Runnable[numThreads];

        //For every thread that exists, assign a range of rows to process
        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkSize;
            int endRow = (i + 1) * chunkSize;
            tasks[i] = () -> multiplySubMatrix(matrix1, matrix2, resultMatrix, startRow, endRow);
            executorService.submit(tasks[i]);
        }

        //Initiates an orderly shutdown of the thread pool
        executorService.shutdown();

        //Wait for all the threads to finish their execution since the shutdown has been called
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Return the final result
        return resultMatrix;
    }

    //This method is called concurrently by multiple threads
    private static void multiplySubMatrix(long[][] matrix1, long[][] matrix2, long[][] resultMatrix, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }

    private static void verifyResult(long[][] result, long[][] goldStandardResult) {
        //Minus the Threaded result with the Non-Threaded result and it should be 0
        boolean verificationPassed = true;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
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

    //Same method in the App.java to print a matrix at variable length
    private static void printMatrixPreview(long[][] matrix, int rows, int cols) {
        for (int i = 0; i < rows && i < matrix.length; i++) {
            for (int j = 0; j < cols && j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

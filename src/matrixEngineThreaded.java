import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class matrixEngineThreaded {
    private static final int MATRIX_SIZE = 1000; // Working with our 1000x1000 matrix
    private static final int NUM_THREADS = 4; // Number of threads which we can change

    public void performMatrixMultiplicationThreaded() {
        var matrixEngine = new matrixEngine();
        var matrices = matrixEngine.GenerateBaseMatrixes();

        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrices.matrix1, 10, 10);
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrices.matrix2, 10, 10);

        long[][] result1 = performMatrixMultiplication(matrices.matrix1, matrices.matrix2, NUM_THREADS);
        System.out.println("1st Multiplication with Threading: \n");
        printMatrixPreview(result1, 10, 10);

        long[][] secondIterationMatrix = new long[MATRIX_SIZE][MATRIX_SIZE];
        matrixEngine.fillMatrix(secondIterationMatrix);
        long[][] result2 = performMatrixMultiplication(result1, secondIterationMatrix, NUM_THREADS);
        System.out.println("2nd Multiplication with Threading: \n");
        printMatrixPreview(result2, 10, 10);

        long[][] thirdIterationMatrix = new long[MATRIX_SIZE][MATRIX_SIZE];
        matrixEngine.fillMatrix(thirdIterationMatrix);
        long[][] result3 = performMatrixMultiplication(result2, thirdIterationMatrix, NUM_THREADS);
        System.out.println("3rd Multiplication with Threading: \n");
        printMatrixPreview(result3, 10, 10);

        // Verify the results against the gold standard
        var goldStandardResult1 = matrixEngine.multiplyMatrices(matrices.matrix1, matrices.matrix2);
        verifyResult(result1, goldStandardResult1);

        var goldStandardResult2 = matrixEngine.multiplyMatrices(result1, secondIterationMatrix);
        verifyResult(result2, goldStandardResult2);

        var goldStandardResult3 = matrixEngine.multiplyMatrices(result2, thirdIterationMatrix);
        verifyResult(result3, goldStandardResult3);
    }

    private static long[][] performMatrixMultiplication(long[][] matrix1, long[][] matrix2, int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int chunkSize = MATRIX_SIZE / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startRow = i * chunkSize;
            int endRow = (i + 1) * chunkSize;
            executorService.submit(() -> multiplySubMatrix(matrix1, matrix2, startRow, endRow));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return matrix1; // matrix1 is modified in-place, containing the final result
    }

    private static void multiplySubMatrix(long[][] matrix1, long[][] matrix2, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    matrix1[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }

    private static void verifyResult(long[][] result, long[][] goldStandardResult) {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (result[i][j] != goldStandardResult[i][j]) {
                    System.err.println("Verification failed! Results differ.");
                    return;
                }
            }
        }
        System.out.println("Verification successful! Results match the gold standard.");
    }

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

public class App {
    public static void main(String[] args) throws Exception {
        var task1 = new task1();
        var matrixes = task1.GenerateMatrixes();

        System.out.println("Matrix 1: \n");
        printMatrix(matrixes.matrix1);
        System.out.println("Matrix 2: \n");
        printMatrix(matrixes.matrix2);
    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

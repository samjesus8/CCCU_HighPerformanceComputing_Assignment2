public class App {
    public static void main(String[] args) throws Exception {
        var Task1 = new task1();
        var matrixes = Task1.GenerateMatrixes();

        System.out.println("Matrix 1: \n");
        printMatrixPreview(matrixes.matrix1, 10, 10);
        System.out.println("Matrix 2: \n");
        printMatrixPreview(matrixes.matrix2, 10, 10);

        var result1 = Task1.multiplyMatrices(matrixes.matrix1, matrixes.matrix2);
        System.out.println("1st Multiplication: \n");
        printMatrixPreview(result1, 10, 10);
    }

    private static void printMatrixPreview(int[][] matrix, int previewRows, int previewCols) {
        for (int i = 0; i < Math.min(previewRows, matrix.length); i++) {
            for (int j = 0; j < Math.min(previewCols, matrix[i].length); j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}

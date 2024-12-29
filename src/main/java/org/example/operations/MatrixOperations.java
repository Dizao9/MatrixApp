package org.example.operations;

import org.example.exception.MatrixException;
import org.example.matrix.Matrix;

import java.util.Arrays;

/**
 * Класс {@code MatrixOperations} предоставляет набор статических методов
 * для выполнения различных операций над матрицами, таких как сложение, вычитание,
 * умножение на скаляр и умножение матриц.
 */
public class MatrixOperations {

    /**
     * Складывает две матрицы.
     * <p>
     *  Матрицы должны иметь одинаковое количество строк и столбцов.
     * </p>
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @return Результирующая матрица - сумма matrix1 и matrix2.
     * @throws MatrixException Если матрицы имеют несовместимые размеры (разное количество строк или столбцов).
     */
    public Matrix add(Matrix matrix1, Matrix matrix2) throws MatrixException {
        validateMatricesForAdditionAndSubtraction(matrix1, matrix2);
        return performMatrixOperation(matrix1, matrix2, (a, b) -> a + b);
    }

    /**
     * Вычитает одну матрицу из другой.
     * <p>
     *  Матрицы должны иметь одинаковое количество строк и столбцов.
     * </p>
     *
     * @param matrix1 Первая матрица (уменьшаемое).
     * @param matrix2 Вторая матрица (вычитаемое).
     * @return Результирующая матрица - разность matrix1 и matrix2.
     * @throws MatrixException Если матрицы имеют несовместимые размеры (разное количество строк или столбцов).
     */
    public Matrix subtract(Matrix matrix1, Matrix matrix2) throws MatrixException {
        validateMatricesForAdditionAndSubtraction(matrix1, matrix2);
        return performMatrixOperation(matrix1, matrix2, (a, b) -> a - b);
    }

    /**
     * Умножает матрицу на скалярное значение.
     *
     * @param matrix Исходная матрица.
     * @param scalar  Скалярное значение, на которое умножается каждый элемент матрицы.
     * @return Результирующая матрица - произведение matrix и scalar.
     */
    public Matrix multiplyByScalar(Matrix matrix, double scalar) {
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setElement(i, j, matrix.getElement(i, j) * scalar);
            }
        }
        return result;
    }

    /**
     * Умножает две матрицы.
     * <p>
     *  Количество столбцов первой матрицы должно быть равно количеству строк второй матрицы.
     * </p>
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @return Результирующая матрица - произведение matrix1 и matrix2.
     * @throws MatrixException Если матрицы имеют несовместимые размеры (количество столбцов первой матрицы не равно количеству строк второй матрицы).
     */
    public Matrix multiply(Matrix matrix1, Matrix matrix2) throws MatrixException {
        if (matrix1.getCols() != matrix2.getRows()) {
            throw new MatrixException("Количество столбцов первой матрицы должно быть равно количеству строк второй матрицы для умножения.");
        }

        int rows1 = matrix1.getRows();
        int cols1 = matrix1.getCols();
        int cols2 = matrix2.getCols();

        Matrix result = new Matrix(rows1, cols2);

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                double sum = 0;
                for (int k = 0; k < cols1; k++) {
                    sum += matrix1.getElement(i, k) * matrix2.getElement(k, j);
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Вычисляет определитель матрицы.
     * <p>
     *  Матрица должна быть квадратной (иметь одинаковое количество строк и столбцов).
     * </p>
     *
     * @param matrix Исходная матрица.
     * @return Определитель матрицы.
     * @throws MatrixException Если матрица не является квадратной.
     */
    public double determinant(Matrix matrix) throws MatrixException {
        if (!isSquareMatrix(matrix)) {
            throw new MatrixException("Определитель можно вычислить только для квадратной матрицы.");
        }
        return determinantRecursive(matrix.getData());
    }

    private double determinantRecursive(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        double det = 0;
        for (int i = 0; i < n; i++) {
            double[][] subMatrix = getSubMatrix(matrix, 0, i);
            det += Math.pow(-1, i) * matrix[0][i] * determinantRecursive(subMatrix);
        }
        return det;
    }

    private double[][] getSubMatrix(double[][] matrix, int rowToRemove, int colToRemove) {
        int n = matrix.length;
        double[][] subMatrix = new double[n - 1][n - 1];
        int subRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == rowToRemove) {
                continue;
            }
            int subCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == colToRemove) {
                    continue;
                }
                subMatrix[subRow][subCol] = matrix[i][j];
                subCol++;
            }
            subRow++;
        }
        return subMatrix;
    }

    /**
     * Проверяет, что матрицы имеют одинаковое количество строк и столбцов для операций сложения и вычитания.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @throws MatrixException Если матрицы имеют несовместимые размеры.
     */
    private void validateMatricesForAdditionAndSubtraction(Matrix matrix1, Matrix matrix2) throws MatrixException {
        if (matrix1.getRows() != matrix2.getRows() || matrix1.getCols() != matrix2.getCols()) {
            throw new MatrixException("Матрицы должны иметь одинаковые размеры для данной операции.");
        }
    }

    /**
     * Выполняет операцию над двумя матрицами, применяя заданную функцию.
     *
     * @param matrix1 Первая матрица.
     * @param matrix2 Вторая матрица.
     * @param operation Функция для выполнения операции над элементами матриц.
     * @return Результирующая матрица.
     */
    private Matrix performMatrixOperation(Matrix matrix1, Matrix matrix2, MatrixElementOperation operation) {
        int rows = matrix1.getRows();
        int cols = matrix1.getCols();
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = operation.apply(matrix1.getElement(i, j), matrix2.getElement(i, j));
                result.setElement(i, j, value);
            }
        }
        return result;
    }

    /**
     * Проверяет, является ли матрица квадратной (количество строк равно количеству столбцов).
     *
     * @param matrix Матрица для проверки.
     * @return true, если матрица квадратная, иначе false.
     */
    private boolean isSquareMatrix(Matrix matrix) {
        return matrix.getRows() == matrix.getCols();
    }

    /**
     * Функциональный интерфейс для выполнения операций над элементами матриц.
     */
    @FunctionalInterface
    private interface MatrixElementOperation {
        double apply(double a, double b);
    }
}
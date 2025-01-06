import org.example.exception.MatrixException;
import org.example.matrix.Matrix;
import org.example.operations.MatrixOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс {@code MatrixOperationsTest} предназначен для тестирования функциональности класса {@link MatrixOperations}.
 * <p>
 *   Содержит тесты для проверки корректности операций сложения, вычитания, умножения на скаляр,
 *   умножения матриц и вычисления определителя.
 * </p>
 */
public class MatrixOperationsTest {

    private MatrixOperations matrixOperations;
    private Matrix matrix1;
    private Matrix matrix2;
    private Matrix matrix3;

    /**
     * Метод, выполняемый перед каждым тестовым методом.
     * Инициализирует объект {@link MatrixOperations} и тестовые матрицы {@link Matrix}.
     *
     * @throws MatrixException если при создании матриц возникает ошибка
     */
    @BeforeEach
    void setUp() throws MatrixException {
        matrixOperations = new MatrixOperations();
        matrix1 = new Matrix(new double[][]{{1, 2}, {3, 4}});
        matrix2 = new Matrix(new double[][]{{5, 6}, {7, 8}});
        matrix3 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    }

    @Test
    void testAdd_ValidMatrices() throws MatrixException {
        Matrix result = matrixOperations.add(matrix1, matrix2);
        assertArrayEquals(new double[][]{{6, 8}, {10, 12}}, result.getData(), "Сложение матриц выполнено некорректно");
    }

    @Test
    void testAdd_IncompatibleMatrices() {
        Matrix matrix4 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertThrows(MatrixException.class, () -> matrixOperations.add(matrix1, matrix4),
                "Ожидалось исключение при сложении матриц несовместимого размера");
    }

    @Test
    void testSubtract_ValidMatrices() throws MatrixException {
        Matrix result = matrixOperations.subtract(matrix2, matrix1);
        assertArrayEquals(new double[][]{{4, 4}, {4, 4}}, result.getData(), "Вычитание матриц выполнено некорректно");
    }

    @Test
    void testSubtract_IncompatibleMatrices() {
        Matrix matrix4 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertThrows(MatrixException.class, () -> matrixOperations.subtract(matrix1, matrix4),
                "Ожидалось исключение при вычитании матриц несовместимого размера");
    }

    @Test
    void testMultiplyByScalar_ValidMatrix() throws MatrixException {
        Matrix result = matrixOperations.multiplyByScalar(matrix1, 2);
        assertArrayEquals(new double[][]{{2, 4}, {6, 8}}, result.getData(), "Умножение матрицы на скаляр выполнено некорректно");
    }

    @Test
    void testMultiply_ValidMatrices() throws MatrixException {
        Matrix result = matrixOperations.multiply(matrix1, matrix2);
        assertArrayEquals(new double[][]{{19, 22}, {43, 50}}, result.getData(), "Умножение матриц выполнено некорректно");
    }

    @Test
    void testMultiply_IncompatibleMatrices() {
        Matrix matrix4 = new Matrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        assertThrows(MatrixException.class, () -> matrixOperations.multiply(matrix1, matrix4),
                "Ожидалось исключение при умножении матриц несовместимого размера");
    }

    @Test
    void testDeterminant_ValidMatrix() throws MatrixException {
        double result = matrixOperations.determinant(matrix1);
        assertEquals(-2, result, "Определитель матрицы 2x2 вычислен некорректно");

        double result2 = matrixOperations.determinant(matrix3);
        assertEquals(0, result2, "Определитель матрицы 3x3 вычислен некорректно");
    }

    @Test
    void testDeterminant_NonSquareMatrix() {
        Matrix matrix4 = new Matrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        assertThrows(MatrixException.class, () -> matrixOperations.determinant(matrix4),
                "Ожидалось исключение при вычислении определителя неквадратной матрицы");
    }
}

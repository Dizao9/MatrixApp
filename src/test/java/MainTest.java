import org.example.exception.MatrixException;
import org.example.matrix.Matrix;
import org.example.operations.MatrixOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс {@code MainTest} содержит набор тестов для проверки корректности
 * работы класса {@link org.example.operations.MatrixOperations}.
 * Тесты проверяют основные матричные операции, такие как сложение, вычитание,
 * умножение на скаляр, умножение матриц и вычисление определителя, а также
 * обрабатывают исключительные ситуации.
 */
class MainTest {

    private MatrixOperations matrixOperations;

    /**
     * Выполняется перед каждым тестовым методом для инициализации объекта {@code MatrixOperations}.
     */
    @BeforeEach
    void setUp() {
        matrixOperations = new MatrixOperations();
    }

    /**
     * Тест проверяет корректность сложения двух матриц.
     *
     * @throws MatrixException Если во время сложения возникнет ошибка, связанная с несовместимыми размерами матриц.
     */
    @Test
    void testAddition() throws MatrixException {
        Matrix matrix1 = new Matrix(new double[][]{
                {1, 2},
                {3, 4}
        });
        Matrix matrix2 = new Matrix(new double[][]{
                {5, 6},
                {7, 8}
        });

        Matrix expected = new Matrix(new double[][]{
                {6, 8},
                {10, 12}
        });

        assertEquals(expected, matrixOperations.add(matrix1, matrix2));
    }

    /**
     * Тест проверяет корректность вычисления определителя матрицы 2x2.
     *
     * @throws MatrixException Если во время вычисления определителя возникнет ошибка.
     */
    @Test
    void testDeterminant() throws MatrixException {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2},
                {3, 4}
        });

        assertEquals(-2, matrixOperations.determinant(matrix));
    }

    /**
     * Тест проверяет корректность умножения матрицы на скаляр.
     *
     * @throws MatrixException Если во время умножения на скаляр возникнет ошибка.
     */
    @Test
    void testScalarMultiplication() throws MatrixException {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2},
                {3, 4}
        });

        Matrix expected = new Matrix(new double[][]{
                {2, 4},
                {6, 8}
        });

        assertEquals(expected, matrixOperations.multiplyByScalar(matrix, 2));
    }

    /**
     * Тест проверяет, что при сложении матриц с несовместимыми размерами выбрасывается исключение {@link MatrixException}.
     *
     * @throws MatrixException Если во время сложения возникнет ошибка.
     */
    @Test
    void testInvalidDimensionsForAddition() throws MatrixException {
        final Matrix matrix1 = new Matrix(new double[][]{
                {1, 2}
        });

        final Matrix matrix2 = new Matrix(new double[][]{
                {3, 4},
                {5, 6}
        });

        assertThrows(MatrixException.class, () -> matrixOperations.add(matrix1, matrix2));
    }


    /**
     * Тест проверяет корректность сложения двух матриц 1x1.
     *
     * @throws MatrixException Если во время сложения возникнет ошибка.
     */
    @Test
    void testAdditionWith1x1Matrices() throws MatrixException {
        Matrix matrix1 = new Matrix(new double[][]{{1}});
        Matrix matrix2 = new Matrix(new double[][]{{2}});
        Matrix expected = new Matrix(new double[][]{{3}});

        assertEquals(expected, matrixOperations.add(matrix1, matrix2));
    }

    /**
     * Тест проверяет корректность сохранения матрицы в файл и чтения ее из файла.
     *
     * @throws IOException Если во время сохранения или чтения файла возникнет ошибка.
     * @throws MatrixException Если во время работы с матрицами возникнет ошибка.
     */
    @Test
    void testSaveMatrixToFile() throws IOException, MatrixException {
        Matrix matrix = new Matrix(new double[][]{{1, 2}, {3, 4}});
        String filePath = "test_output.txt";

        // Сохраняем матрицу в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            double[][] data = matrix.getData();
            for (double[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(String.valueOf(row[i]));
                    if (i < row.length - 1) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }
        }

        // Проверяем, что файл существует и содержит корректные данные
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        assertEquals("1.0 2.0", lines.get(0));
        assertEquals("3.0 4.0", lines.get(1));

        // Удаляем файл после теста
        Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * Тест проверяет, что при попытке вычислить определитель неквадратной матрицы выбрасывается исключение {@link MatrixException}.
     *
     * @throws MatrixException Если во время вычисления определителя возникнет ошибка.
     */
    @Test
    void testDeterminantOfNonSquareMatrixThrowsException() throws MatrixException {
        final Matrix matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        assertThrows(MatrixException.class, () -> matrixOperations.determinant(matrix));
    }

    /**
     * Тест проверяет корректность умножения двух матриц.
     *
     * @throws MatrixException Если во время умножения матриц возникнет ошибка.
     */
    @Test
    void testMultiplication() throws MatrixException {
        Matrix matrix1 = new Matrix(new double[][]{
                {1, 2},
                {3, 4}
        });
        Matrix matrix2 = new Matrix(new double[][]{
                {2, 0},
                {1, 2}
        });

        Matrix expected = new Matrix(new double[][]{
                {4, 4},
                {10, 8}
        });

        assertEquals(expected, matrixOperations.multiply(matrix1, matrix2));
    }

    /**
     * Тест проверяет корректность умножения матрицы на отрицательный скаляр.
     *
     * @throws MatrixException Если во время умножения на скаляр возникнет ошибка.
     */
    @Test
    void testScalarMultiplicationWithNegativeScalar() throws MatrixException {
        Matrix matrix = new Matrix(new double[][]{
                {1, -2},
                {3, -4}
        });

        Matrix expected = new Matrix(new double[][]{
                {-2, 4},
                {-6, 8}
        });

        assertEquals(expected, matrixOperations.multiplyByScalar(matrix, -2));
    }
}
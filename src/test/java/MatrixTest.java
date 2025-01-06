import org.example.exception.MatrixException;
import org.example.matrix.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс {@code MatrixTest} предназначен для тестирования функциональности класса {@link Matrix}.
 * <p>
 *   Содержит тесты для проверки корректности операций получения и установки элементов,
 *   а также получения размеров матрицы и строкового представления.
 * </p>
 */
public class MatrixTest {

    private Matrix matrix;
    private double[][] testData;

    /**
     * Метод, выполняемый перед каждым тестовым методом.
     * Инициализирует тестовую матрицу {@link Matrix} и тестовые данные.
     */
    @BeforeEach
    void setUp() throws MatrixException {
        testData = new double[][]{{1, 2}, {3, 4}};
        matrix = new Matrix(testData);
    }

    /**
     * Тест для проверки корректного создания матрицы с заданным количеством строк и столбцов.
     * Проверяет, что создание матрицы с корректными размерами не выбрасывает исключение.
     */
    @Test
    void testMatrixCreation_ValidSize() {
        assertDoesNotThrow(() -> new Matrix(2, 2), "Создание матрицы с корректным размером не должно выбрасывать исключение");
    }

    /**
     * Тест для проверки выбрасывания исключения при создании матрицы с некорректным количеством строк и/или столбцов.
     * Проверяет, что создание матрицы с некорректными размерами выбрасывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testMatrixCreation_InvalidSize() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> new Matrix(0, 2), "Ожидалось исключение при создании матрицы с некорректным количеством строк");
        assertEquals("Количество строк и столбцов должно быть больше 0", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> new Matrix(2, 0), "Ожидалось исключение при создании матрицы с некорректными размерами");
        assertEquals("Количество строк и столбцов должно быть больше 0", exception2.getMessage());
    }

    /**
     * Тест для проверки корректного создания матрицы на основе заданного двумерного массива.
     * Проверяет, что создание матрицы с корректными данными не выбрасывает исключение.
     */
    @Test
    void testMatrixCreation_ValidData() {
        assertDoesNotThrow(() -> new Matrix(testData), "Создание матрицы с корректными данными не должно выбрасывать исключение");
    }

    /**
     * Тест для проверки выбрасывания исключения при создании матрицы с некорректными данными.
     * Проверяет, что создание матрицы с пустыми данными или null данными выбрасывает исключение {@link IllegalArgumentException}.
     */
    @Test
    void testMatrixCreation_InvalidData() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> new Matrix(new double[0][0]), "Ожидалось исключение при создании матрицы с пустыми данными");
        assertEquals("Матрица не может быть пустой", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> new Matrix(null), "Ожидалось исключение при создании матрицы с null данными");
        assertEquals("Матрица не может быть пустой", exception2.getMessage());
    }

    /**
     * Тест для проверки корректного получения количества строк матрицы.
     * Проверяет, что метод {@link Matrix#getRows()} возвращает правильное значение.
     */
    @Test
    void testGetRows() {
        assertEquals(2, matrix.getRows(), "Количество строк должно быть 2");
    }

    /**
     * Тест для проверки корректного получения количества столбцов матрицы.
     * Проверяет, что метод {@link Matrix#getCols()} возвращает правильное значение.
     */
    @Test
    void testGetCols() {
        assertEquals(2, matrix.getCols(), "Количество столбцов должно быть 2");
    }

    /**
     * Тест для проверки корректного получения элемента матрицы по заданным индексам.
     * Проверяет, что метод {@link Matrix#getElement(int, int)} возвращает правильные значения
     * для элементов матрицы в допустимых границах.
     */
    @Test
    void testGetElement_ValidIndices() {
        assertEquals(1, matrix.getElement(0, 0), "Значение элемента [0, 0] должно быть 1");
        assertEquals(2, matrix.getElement(0, 1), "Значение элемента [0, 1] должно быть 2");
        assertEquals(3, matrix.getElement(1, 0), "Значение элемента [1, 0] должно быть 3");
        assertEquals(4, matrix.getElement(1, 1), "Значение элемента [1, 1] должно быть 4");
    }

    /**
     * Тест для проверки выбрасывания исключения при получении элемента матрицы за пределами допустимых индексов.
     * Проверяет, что метод {@link Matrix#getElement(int, int)} выбрасывает исключение
     * {@link IndexOutOfBoundsException}, когда индексы выходят за границы матрицы.
     */
    @Test
    void testGetElement_InvalidIndices() {
        IndexOutOfBoundsException exception1 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.getElement(-1, 0), "Ожидалось исключение при выходе индекса строки за границы");
        assertEquals("Индексы за пределами матрицы", exception1.getMessage());

        IndexOutOfBoundsException exception2 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.getElement(0, -1), "Ожидалось исключение при выходе индекса столбца за границы");
        assertEquals("Индексы за пределами матрицы", exception2.getMessage());

        IndexOutOfBoundsException exception3 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.getElement(2, 0), "Ожидалось исключение при выходе индекса строки за границы");
        assertEquals("Индексы за пределами матрицы", exception3.getMessage());

        IndexOutOfBoundsException exception4 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.getElement(0, 2), "Ожидалось исключение при выходе индекса столбца за границы");
        assertEquals("Индексы за пределами матрицы", exception4.getMessage());
    }

    /**
     * Тест для проверки корректной установки значения элемента матрицы по заданным индексам.
     * Проверяет, что метод {@link Matrix#setElement(int, int, double)} устанавливает правильное значение
     * для элемента матрицы в допустимых границах.
     */
    @Test
    void testSetElement_ValidIndices() {
        matrix.setElement(0, 0, 5);
        assertEquals(5, matrix.getElement(0, 0), "Значение элемента [0, 0] должно быть изменено на 5");
        matrix.setElement(1, 1, 7);
        assertEquals(7, matrix.getElement(1, 1), "Значение элемента [1, 1] должно быть изменено на 7");
    }

    /**
     * Тест для проверки выбрасывания исключения при установке элемента матрицы за пределами допустимых индексов.
     * Проверяет, что метод {@link Matrix#setElement(int, int, double)} выбрасывает исключение
     * {@link IndexOutOfBoundsException}, когда индексы выходят за границы матрицы.
     */
    @Test
    void testSetElement_InvalidIndices() {
        IndexOutOfBoundsException exception1 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.setElement(-1, 0, 5), "Ожидалось исключение при выходе индекса строки за границы");
        assertEquals("Индексы за пределами матрицы", exception1.getMessage());

        IndexOutOfBoundsException exception2 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.setElement(0, -1, 5), "Ожидалось исключение при выходе индекса столбца за границы");
        assertEquals("Индексы за пределами матрицы", exception2.getMessage());

        IndexOutOfBoundsException exception3 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.setElement(2, 0, 5), "Ожидалось исключение при выходе индекса строки за границы");
        assertEquals("Индексы за пределами матрицы", exception3.getMessage());

        IndexOutOfBoundsException exception4 = assertThrows(IndexOutOfBoundsException.class, () -> matrix.setElement(0, 2, 5), "Ожидалось исключение при выходе индекса столбца за границы");
        assertEquals("Индексы за пределами матрицы", exception4.getMessage());
    }

    /**
     * Тест для проверки корректного получения внутреннего представления матрицы в виде двумерного массива.
     * Проверяет, что метод {@link Matrix#getData()} возвращает оригинальные данные матрицы.
     */
    @Test
    void testGetData() {
        assertArrayEquals(testData, matrix.getData(), "Метод getData должен возвращать оригинальные данные");
    }

    /**
     * Тест для проверки корректного преобразования матрицы в строковое представление.
     * Проверяет, что метод {@link Matrix#toString()} возвращает правильное строковое представление матрицы.
     */
    @Test
    void testToString() {
        String expected = "[1.0, 2.0]\n[3.0, 4.0]";
        assertEquals(expected, matrix.toString(), "Метод toString должен возвращать правильное строковое представление матрицы");
    }
}

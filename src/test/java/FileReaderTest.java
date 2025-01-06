import org.example.exception.MatrixException;
import org.example.matrix.Matrix;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для {@link org.example.file.FileReader}.
 * <p>
 * Содержит наборы тестов для проверки корректности чтения матриц из файла.
 * </p>
 */
class FileReaderTest {

    private org.example.file.FileReader fileReader;

    /**
     * Временная директория, создаваемая JUnit для тестов.
     */
    @TempDir
    Path tempDir;

    private Path testFile;

    /**
     * Выполняется перед каждым тестовым методом для создания экземпляра {@link org.example.file.FileReader}.
     */
    @BeforeEach
    void setUp() {
        fileReader = new org.example.file.FileReader();
    }

    /**
     * Выполняется после каждого тестового метода для очистки, например, удаления временных файлов.
     */
    @AfterEach
    void tearDown() {
        if (testFile != null && Files.exists(testFile)) {
            try {
                Files.delete(testFile);
            } catch (IOException e) {
                System.err.println("Не удалось удалить временный файл: " + testFile);
            }
        }
    }

    /**
     * Тест проверяет успешное чтение матрицы из корректного файла.
     *
     * @throws IOException    если возникает ошибка при создании временного файла.
     * @throws MatrixException если возникает ошибка при чтении матрицы.
     */
    @Test
    void readMatrixFromFile_validFile() throws IOException, MatrixException {
        testFile = tempDir.resolve("valid_matrix.txt");
        String content = "1.0 2.0\n3.0 4.0\n";
        Files.writeString(testFile, content);

        Matrix matrix = fileReader.readMatrixFromFile(testFile.toString());

        assertNotNull(matrix);
        assertEquals(2, matrix.getRows());
        assertEquals(2, matrix.getCols()); // Corrected method name
        assertEquals(1.0, matrix.getData()[0][0]);
        assertEquals(2.0, matrix.getData()[0][1]);
        assertEquals(3.0, matrix.getData()[1][0]);
        assertEquals(4.0, matrix.getData()[1][1]);
    }

    /**
     * Тест проверяет выбрасывание исключения {@link MatrixException} при чтении несуществующего файла.
     */
    @Test
    void readMatrixFromFile_nonExistentFile() {
        String nonExistentFilePath = tempDir.resolve("non_existent_matrix.txt").toString();
        MatrixException exception = assertThrows(MatrixException.class, () -> {
            fileReader.readMatrixFromFile(nonExistentFilePath);
        });
        assertTrue(exception.getMessage().startsWith("Ошибка при чтении файла"));
    }

    /**
     * Тест проверяет выбрасывание исключения {@link MatrixException} при чтении файла с разной длиной строк.
     *
     * @throws IOException если возникает ошибка при создании временного файла.
     */
    @Test
    void readMatrixFromFile_inconsistentRowLength() throws IOException {
        testFile = tempDir.resolve("invalid_length_matrix.txt");
        String content = "1.0 2.0\n3.0 4.0 5.0\n";
        Files.writeString(testFile, content);

        MatrixException exception = assertThrows(MatrixException.class, () -> {
            fileReader.readMatrixFromFile(testFile.toString());
        });
        assertEquals("Некорректный формат файла. Строки должны иметь одинаковую длину.", exception.getMessage());
    }

    /**
     * Тест проверяет выбрасывание исключения {@link MatrixException} при чтении файла с нечисловыми значениями.
     *
     * @throws IOException если возникает ошибка при создании временного файла.
     */
    @Test
    void readMatrixFromFile_nonNumericValue() throws IOException {
        testFile = tempDir.resolve("non_numeric_matrix.txt");
        String content = "1.0 abc\n3.0 4.0\n";
        Files.writeString(testFile, content);

        MatrixException exception = assertThrows(MatrixException.class, () -> {
            fileReader.readMatrixFromFile(testFile.toString());
        });
        assertEquals("Некорректный формат файла. Ожидается числовое значение", exception.getMessage());
    }

    /**
     * Тест проверяет выбрасывание исключения {@link MatrixException} при чтении пустого файла.
     *
     * @throws IOException если возникает ошибка при создании временного файла.
     */
    @Test
    void readMatrixFromFile_emptyFile() throws IOException {
        testFile = tempDir.resolve("empty_matrix.txt");
        Files.createFile(testFile);

        MatrixException exception = assertThrows(MatrixException.class, () -> {
            fileReader.readMatrixFromFile(testFile.toString());
        });
        assertEquals("Файл пустой", exception.getMessage());
    }
}
package org.example.file;

import org.example.exception.MatrixException;
import org.example.matrix.Matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс {@code FileReader} предоставляет функциональность для чтения матрицы из текстового файла.
 * <p>
 *     Этот класс обрабатывает чтение данных из файла, проверяет формат данных и преобразует их в объект {@link Matrix}.
 * </p>
 */
public class FileReader {

    /**
     * Читает матрицу из файла, представленного в виде текстовых данных, где строки матрицы разделены переносами строк,
     * а элементы в строке разделены пробелами.
     *
     * @param filePath путь к файлу, из которого будет читаться матрица.
     * @return матрицу, созданную на основе данных из файла.
     * @throws MatrixException Если произошла ошибка ввода/вывода, если файл имеет неверный формат
     *                          (например, строки разной длины или нечисловые значения), или если файл пуст.
     */
    public Matrix readMatrixFromFile(String filePath) throws MatrixException {
        Path path = Paths.get(filePath);
        List<double[]> matrixRows = new ArrayList<>();
        int expectedColumns = -1;

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                double[] row = parseLine(line, expectedColumns);
                if (expectedColumns == -1) {
                    expectedColumns = row.length;
                } else if (expectedColumns != row.length) {
                    throw new MatrixException("Некорректный формат файла. Строки должны иметь одинаковую длину.");
                }
                matrixRows.add(row);
            }
            if (matrixRows.isEmpty()) {
                throw new MatrixException("Файл пустой");
            }
            return new Matrix(matrixRows.toArray(new double[0][]));
        } catch (IOException e) {
            throw new MatrixException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
    }

    /**
     *  Разбирает строку файла на массив чисел типа double, проверяя длину и числовой формат.
     *
     * @param line строка для разбора
     * @param expectedColumns ожидаемое количество столбцов.
     * @return массив чисел double, представляющий строку матрицы.
     * @throws MatrixException если строка имеет неверный формат или содержит нечисловые значения.
     */
    private double[] parseLine(String line, int expectedColumns) throws MatrixException {
        String[] values = line.trim().split("\\s+");

        try {
            return java.util.Arrays.stream(values)
                    .mapToDouble(Double::parseDouble)
                    .toArray();

        } catch (NumberFormatException e) {
            throw new MatrixException("Некорректный формат файла. Ожидается числовое значение");
        }


    }
}
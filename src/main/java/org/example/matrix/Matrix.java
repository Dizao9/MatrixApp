package org.example.matrix;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Класс {@code Matrix} представляет собой реализацию матрицы с элементами типа double.
 * <p>
 *     Позволяет создавать матрицы, получать и устанавливать значения элементов,
 *     а также получать размеры матрицы и строковое представление матрицы.
 * </p>
 */
public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;

    /**
     * Создает новую матрицу с заданным количеством строк и столбцов.
     *
     * @param rows Количество строк матрицы. Должно быть больше 0.
     * @param cols Количество столбцов матрицы. Должно быть больше 0.
     * @throws IllegalArgumentException Если количество строк или столбцов меньше или равно 0.
     */
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Количество строк и столбцов должно быть больше 0");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    /**
     * Создает новую матрицу на основе заданного двумерного массива.
     *
     * @param data Двумерный массив, используемый для инициализации матрицы.
     *             Массив не должен быть null или пустым.
     * @throws IllegalArgumentException Если массив data равен null, пуст, или не имеет ни одного столбца.
     */
    public Matrix(double[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Матрица не может быть пустой");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    /**
     * Возвращает количество строк в матрице.
     *
     * @return Количество строк матрицы.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Возвращает количество столбцов в матрице.
     *
     * @return Количество столбцов матрицы.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Возвращает значение элемента матрицы по заданным индексам.
     *
     * @param row Индекс строки элемента.
     * @param col Индекс столбца элемента.
     * @return Значение элемента матрицы.
     * @throws IndexOutOfBoundsException Если row или col выходят за пределы допустимых границ матрицы.
     */
    public double getElement(int row, int col) {
        validateIndices(row, col);
        return data[row][col];
    }

    /**
     * Устанавливает значение элемента матрицы по заданным индексам.
     *
     * @param row Индекс строки элемента.
     * @param col Индекс столбца элемента.
     * @param value Новое значение элемента.
     * @throws IndexOutOfBoundsException Если row или col выходят за пределы допустимых границ матрицы.
     */
    public void setElement(int row, int col, double value) {
        validateIndices(row, col);
        data[row][col] = value;
    }

    /**
     * Возвращает внутреннее представление матрицы в виде двумерного массива.
     *
     * @return двумерный массив double, представляющий матрицу.
     */
    public double[][] getData() {
        return data;
    }


    /**
     * Проверяет, что индексы находятся в пределах границ матрицы.
     *
     * @param row Индекс строки элемента.
     * @param col Индекс столбца элемента.
     * @throws IndexOutOfBoundsException Если row или col выходят за пределы допустимых границ матрицы.
     */
    private void validateIndices(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Индексы за пределами матрицы");
        }
    }

    /**
     * Возвращает строковое представление матрицы.
     *  Каждая строка матрицы выводится на отдельной строке.
     *
     * @return Строковое представление матрицы.
     */
    @Override
    public String toString() {
        return Arrays.stream(data)
                .map(Arrays::toString)
                .collect(Collectors.joining("\n"));
    }
}
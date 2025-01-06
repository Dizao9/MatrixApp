package org.example;

import org.example.exception.MatrixException;
import org.example.file.FileReader;
import org.example.logging.AppLogger;
import org.example.matrix.Matrix;
import org.example.operations.MatrixOperations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Главный класс приложения для работы с матрицами.
 * Позволяет загружать матрицы из файлов, выполнять различные операции над ними
 * и сохранять результаты в файл.
 */
public class Main {

    /**
     * Логгер для записи информации о работе приложения.
     */
    private static final AppLogger logger = new AppLogger(Main.class);
    /**
     * Сканер для чтения ввода пользователя из консоли.
     */
    private static final Scanner scanner = new Scanner(System.in);
    /**
     * Объект для чтения матриц из файлов.
     */
    private static final FileReader fileReader = new FileReader();
    /**
     * Объект для выполнения операций над матрицами.
     */
    private static final MatrixOperations matrixOperations = new MatrixOperations();
    /**
     * Массив для хранения двух матриц, с которыми выполняются операции.
     */
    private static Matrix[] matrices = new Matrix[2];

    /**
     * Основной метод программы.
     * Запускает работу приложения, обрабатывает пользовательский ввод,
     * выполняет операции над матрицами и выводит результаты.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        logger.info("Начало работы программы");
        printSystemInfo();

        try {
            loadMatrices();
            performOperation();
        } catch (MatrixException e) {
            handleMatrixException(e, "общей работы программы");
        } catch (InputMismatchException e) {
            System.err.println("Некорректный ввод данных, проверьте введенные данные.");
            logger.error("Некорректный ввод данных: " + e.getMessage(), e);
            scanner.nextLine(); // Очистить некорректный ввод
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
            logger.error("Неизвестная ошибка: " + e.getMessage(), e);
        } finally {
            logger.info("Завершение работы программы");
        }
    }

    /**
     * Выводит информацию о системе, включая кодировку файла и кодировку по умолчанию.
     */
    private static void printSystemInfo() {
        System.out.println("File encoding: " + System.getProperty("file.encoding"));
        System.out.println("Default Charset: " + java.nio.charset.Charset.defaultCharset());
    }

    /**
     * Загружает матрицы из файлов, указанных пользователем.
     *
     * @throws MatrixException Если возникает ошибка при чтении матрицы из файла.
     */
    private static void loadMatrices() throws MatrixException {
        for (int i = 0; i < matrices.length; i++) {
            while (true) {
                try {
                    System.out.print("Введите путь к " + (i + 1) + " файлу с матрицей: ");
                    String filePath = scanner.nextLine();
                    matrices[i] = fileReader.readMatrixFromFile(filePath);
                    logger.info("Матрица " + (i + 1) + " успешно загружена из файла: " + filePath);
                    break; // Успешно загружено, выходим из цикла
                } catch (MatrixException e) {
                    System.err.println("Ошибка загрузки матрицы: " + e.getMessage());
                    logger.error("Ошибка загрузки матрицы: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Выполняет выбранную пользователем операцию над матрицами.
     *
     * @throws MatrixException Если возникает ошибка при выполнении операции над матрицами.
     */
    private static void performOperation() throws MatrixException {
        System.out.println("\nВыберите операцию:");
        System.out.println("1. Сложение");
        System.out.println("2. Вычитание");
        System.out.println("3. Умножение");
        System.out.println("4. Умножение на скаляр");
        System.out.println("5. Вычисление определителя первой матрицы");
        System.out.print("Введите номер операции: ");

        int choice = getUserChoice(1, 5);

        Matrix resultMatrix = null;
        double determinant = 0;

        try {
            switch (choice) {
                case 1 -> resultMatrix = matrixOperations.add(matrices[0], matrices[1]);
                case 2 -> resultMatrix = matrixOperations.subtract(matrices[0], matrices[1]);
                case 3 -> resultMatrix = matrixOperations.multiply(matrices[0], matrices[1]);
                case 4 -> {
                    System.out.print("Введите скаляр: ");
                    double scalar = scanner.nextDouble();
                    scanner.nextLine(); // Очистить буфер
                    resultMatrix = matrixOperations.multiplyByScalar(matrices[0], scalar);
                }
                case 5 -> determinant = matrixOperations.determinant(matrices[0]);
            }
        } catch (MatrixException e) {
            handleMatrixException(e, "выполнения операции");
        }

        printResult(resultMatrix, choice, determinant);
    }

    /**
     * Выводит результат операции на экран или в файл.
     *
     * @param resultMatrix Результат операции в виде матрицы.
     * @param choice       Выбранный номер операции.
     * @param determinant  Определитель матрицы (если была выбрана операция вычисления определителя).
     */
    private static void printResult(Matrix resultMatrix, int choice, double determinant) {
        if (resultMatrix != null) {
            System.out.println("Результат:\n" + resultMatrix);
            logger.info("Результат операции выведен на экран.");
            saveMatrixToFile(resultMatrix);
        } else if (choice == 5) {
            System.out.println("Определитель первой матрицы: " + determinant);
            logger.info("Определитель первой матрицы выведен на экран.");
        } else {
            System.err.println("Результат операции недоступен.");
            logger.warn("Результат операции недоступен.");
        }
    }

    /**
     * Предлагает пользователю сохранить результат в файл и выполняет сохранение при согласии.
     *
     * @param matrix Матрица для сохранения в файл.
     */
    private static void saveMatrixToFile(Matrix matrix) {
        System.out.println("Хотите сохранить результат в файл? (y/n)");
        String saveChoice = scanner.nextLine();

        if (saveChoice.equalsIgnoreCase("y")) {
            System.out.print("Введите путь к файлу для сохранения: ");
            String filePath = scanner.nextLine();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writeMatrixToFile(matrix, writer);
                System.out.println("Результат сохранен в файл: " + filePath);
                logger.info("Результат сохранен в файл: " + filePath);
            } catch (IOException e) {
                handleMatrixException(e, "сохранения файла");
            }
        }
    }

    /**
     * Записывает матрицу в файл.
     *
     * @param matrix Матрица для записи.
     * @param writer  BufferedWriter для записи в файл.
     * @throws IOException Если возникает ошибка при записи в файл.
     */
    private static void writeMatrixToFile(Matrix matrix, BufferedWriter writer) throws IOException {
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

    /**
     * Обрабатывает исключения, возникшие при работе с матрицами.
     * Выводит сообщение об ошибке и записывает ошибку в лог.
     *
     * @param e       Исключение, которое нужно обработать.
     * @param context Контекст, в котором возникло исключение (например, "загрузки матрицы").
     */
    private static void handleMatrixException(Exception e, String context) {
        System.err.println("Ошибка " + context + ": " + e.getMessage());
        logger.error("Ошибка " + context + ": " + e.getMessage(), e);
    }

    /**
     * Получает от пользователя корректный ввод номера операции в заданном диапазоне.
     *
     * @param min Минимальное значение выбора.
     * @param max Максимальное значение выбора.
     * @return Выбранный пользователем номер операции.
     */
    private static int getUserChoice(int min, int max) {
        while (true) {
            System.out.println("Введите число от " + min + " до " + max + ":");
            if (scanner.hasNextInt()) {  // Проверка, есть ли ввод для nextInt
                int choice = scanner.nextInt();
                scanner.nextLine();  // Очистить буфер
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Введите число от " + min + " до " + max + ":");
                }
            } else {
                System.out.println("Ошибка ввода. Введите корректное число.");
                scanner.nextLine();  // Очистить некорректный ввод
            }
        }
    }
}
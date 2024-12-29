package org.example;

import org.example.exception.MatrixException;
import org.example.file.FileReader;
import org.example.logging.AppLogger;
import org.example.matrix.Matrix;
import org.example.operations.MatrixOperations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Основной класс {@code Main} приложения для выполнения операций над матрицами.
 * <p>
 *  Позволяет пользователю вводить пути к файлам, содержащим матрицы, выбирать операцию
 *  (сложение, вычитание, умножение, умножение на скаляр, вычисление определителя) и
 *  выводить результат на экран.
 * </p>
 */
public class Main {

    private static final AppLogger logger = new AppLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final FileReader fileReader = new FileReader();
    private static final MatrixOperations matrixOperations = new MatrixOperations();
    private static Matrix[] matrices = new Matrix[2]; // Массив для хранения матриц

    /**
     * Главный метод приложения, запускающий программу и обрабатывающий пользовательский ввод.
     *
     * @param args Массив аргументов командной строки (не используется в данном приложении).
     */
    public static void main(String[] args) {
        logger.info("Начало работы программы");
        printSystemInfo();

        boolean continueCalculations = true;
        while (continueCalculations) {
            try {
                if (matrices[0] == null || matrices[1] == null) {
                    loadMatrices();
                }

                int choice = showMainMenu();

                switch (choice) {
                    case 1:
                        performOperation();
                        break;
                    case 2:
                        loadMatrices();
                        break;
                    case 0:
                        continueCalculations = false;
                        break;
                    default:
                        System.out.println("Некорректный выбор. Пожалуйста, выберите операцию из меню.");
                        logger.warn("Некорректный выбор операции из меню: " + choice);
                }
            } catch (MatrixException e) {
                System.err.println("Произошла ошибка: " + e.getMessage());
                logger.error("Произошла ошибка: " + e.getMessage(), e);
            } catch (InputMismatchException e){
                System.err.println("Некорректный ввод данных, проверьте введенные данные.");
                logger.error("Некорректный ввод данных. " + e.getMessage(), e);
                scanner.nextLine(); // Очистка буфера ввода
            }
            catch (Exception e) {
                System.err.println("Неизвестная ошибка " + e.getMessage());
                logger.error("Неизвестная ошибка " + e.getMessage(), e);
            }
        }

        logger.info("Завершение работы программы");
    }

    /**
     * Выводит информацию о кодировке и charset системы.
     */
    private static void printSystemInfo() {
        System.out.println("File encoding: " + System.getProperty("file.encoding"));
        System.out.println("Default Charset: " + java.nio.charset.Charset.defaultCharset());
    }

    /**
     * Показывает главное меню программы и возвращает выбор пользователя.
     *
     * @return Выбор пользователя.
     */
    private static int showMainMenu() {
        System.out.println("\nГлавное меню:");
        System.out.println("1. Выполнить операцию над матрицами");
        System.out.println("2. Загрузить новые матрицы из файлов");
        System.out.println("0. Выход");
        System.out.print("Введите номер действия: ");
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e){
            throw e;
        }finally {
            scanner.nextLine(); // Очистка буфера ввода
        }

    }

    /**
     * Загружает матрицы из файлов.
     * @throws MatrixException Если произошла ошибка при чтении файла.
     */
    private static void loadMatrices() throws MatrixException {
        for (int i = 0; i < matrices.length; i++) {
            boolean isLoaded = false;
            while (!isLoaded){
                try{
                    System.out.print("Введите путь к " + (i + 1) + " файлу с матрицей: ");
                    String filePath = scanner.nextLine();
                    matrices[i] = fileReader.readMatrixFromFile(filePath);
                    isLoaded = true;
                }catch (MatrixException e){
                    System.out.println("Ошибка чтения файла. Попробуйте ещё раз: " + e.getMessage());
                    logger.error("Ошибка чтения файла: " + e.getMessage(), e);
                }

            }

        }

        logger.info("Матрицы успешно загружены из файлов.");
    }

    /**
     * Выполняет выбранную пользователем операцию над матрицами.
     * @throws MatrixException Если произошла ошибка при выполнении операции.
     */
    private static void performOperation() throws MatrixException {
        Matrix[] selectedMatrices = chooseMatrices();

        System.out.println("\nВыберите операцию:");
        System.out.println("1. Сложение");
        System.out.println("2. Вычитание");
        System.out.println("3. Умножение");
        System.out.println("4. Умножение на скаляр");
        System.out.println("5. Вычисление определителя первой матрицы");

        System.out.print("Введите номер операции: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Matrix resultMatrix = null;
        double determinant = 0;

        switch (choice) {
            case 1:
                resultMatrix = matrixOperations.add(selectedMatrices[0], selectedMatrices[1]);
                logger.info("Выполнена операция сложения матриц");
                break;
            case 2:
                resultMatrix = matrixOperations.subtract(selectedMatrices[0], selectedMatrices[1]);
                logger.info("Выполнена операция вычитания матриц");
                break;
            case 3:
                resultMatrix = matrixOperations.multiply(selectedMatrices[0], selectedMatrices[1]);
                logger.info("Выполнена операция умножения матриц");
                break;
            case 4:
                System.out.print("Введите скаляр: ");
                double scalar = scanner.nextDouble();
                scanner.nextLine();
                resultMatrix = matrixOperations.multiplyByScalar(selectedMatrices[0], scalar);
                logger.info("Выполнена операция умножения матрицы на скаляр " + scalar);
                break;
            case 5:
                determinant = matrixOperations.determinant(selectedMatrices[0]);
                logger.info("Вычисление определителя первой матрицы");
                break;
            default:
                System.out.println("Некорректный выбор операции.");
                logger.warn("Некорректный выбор операции");
                return;
        }
        printResult(resultMatrix, choice, determinant);
    }

    /**
     * Выводит результат операции на экран и предлагает сохранить в файл.
     *
     * @param resultMatrix Результирующая матрица (может быть null).
     * @param choice       Выбранная пользователем операция.
     * @param determinant  Определитель матрицы (если вычислялся).
     */
    private static void printResult(Matrix resultMatrix, int choice, double determinant) {
        if (resultMatrix != null) {
            System.out.println("Результат:\n" + resultMatrix);
            logger.info("Результат операции выведен на экран");
            saveMatrixToFile(resultMatrix);
        } else if (choice == 5) {
            System.out.println("Определитель первой матрицы: " + determinant);
            logger.info("Определитель первой матрицы выведен на экран");
        }
    }

    /**
     * Предлагает пользователю сохранить матрицу в файл и выполняет сохранение.
     *
     * @param matrix Матрица для сохранения.
     */
    private static void saveMatrixToFile(Matrix matrix) {
        System.out.println("Хотите сохранить результат в файл? (y/n)");
        String saveChoice = scanner.nextLine();

        if (saveChoice.equalsIgnoreCase("y")) {
            System.out.println("Сохранить в существующий файл (1) или создать новый (2)?");
            int fileChoice = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Введите путь к файлу: ");
            String filePath = scanner.nextLine();

            try {
                if (fileChoice == 1){
                    saveMatrixToExistingFile(matrix, filePath);
                }else if (fileChoice == 2){
                    saveMatrixToNewFile(matrix, filePath);
                }else {
                    System.out.println("Некорректный выбор сохранения файла.");
                    logger.warn("Некорректный выбор сохранения файла.");
                }

                System.out.println("Результат сохранен в файл: " + filePath);
                logger.info("Результат сохранен в файл: " + filePath);

            } catch (IOException e) {
                System.err.println("Ошибка сохранения файла: " + e.getMessage());
                logger.error("Ошибка сохранения файла: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Сохраняет матрицу в существующий файл.
     *
     * @param matrix   Матрица для сохранения.
     * @param filePath Путь к существующему файлу.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    private static void saveMatrixToExistingFile(Matrix matrix, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("Файл не существует: " + filePath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writeMatrixToFile(matrix, writer);
        }

    }

    /**
     * Сохраняет матрицу в новый файл.
     *
     * @param matrix   Матрица для сохранения.
     * @param filePath Путь к новому файлу.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    private static void saveMatrixToNewFile(Matrix matrix, String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            throw new IOException("Файл с таким именем уже существует: " + filePath);
        }

        Files.createDirectories(path.getParent()); // Создаем директорию для файла если её нет
        Files.createFile(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writeMatrixToFile(matrix, writer);
        }

    }

    /**
     * Записывает матрицу в файл в формате, пригодном для чтения.
     *
     * @param matrix Матрица для сохранения.
     * @param writer Поток вывода для записи в файл.
     * @throws IOException Если произошла ошибка ввода-вывода.
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
     *  Предлагает пользователю выбрать, какие матрицы использовать для операции.
     *  @return Массив из двух выбранных матриц.
     */
    private static Matrix[] chooseMatrices() {
        while (true) {
            System.out.println("\nВыберите матрицы для операции (1 или 2):");
            System.out.print("Введите номер первой матрицы: ");
            int firstMatrixNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Введите номер второй матрицы: ");
            int secondMatrixNumber = scanner.nextInt();
            scanner.nextLine();


            if (isValidMatrixNumber(firstMatrixNumber) && isValidMatrixNumber(secondMatrixNumber)) {
                return new Matrix[]{matrices[firstMatrixNumber - 1], matrices[secondMatrixNumber - 1]};
            } else {
                System.out.println("Некорректный номер матрицы, повторите ввод.");
                logger.warn("Некорректный номер матрицы: " + firstMatrixNumber + " или " + secondMatrixNumber);
            }
        }

    }

    /**
     *  Проверяет, является ли введенный номер матрицы валидным.
     * @param matrixNumber номер матрицы для проверки.
     * @return true, если номер валидный, false в ином случае.
     */
    private static boolean isValidMatrixNumber(int matrixNumber) {
        return matrixNumber >= 1 && matrixNumber <= matrices.length;
    }
}
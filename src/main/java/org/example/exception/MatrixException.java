package org.example.exception;

/**
 *  Кастомное исключение для ошибок, связанных с матрицами.
 *  Это исключение используется для обозначения проблем, возникающих при работе с матрицами,
 *  таких как некорректные размеры, недопустимые операции и т.д.
 */
public class MatrixException extends Exception {

    /**
     *  Создает новое исключение MatrixException с указанным сообщением.
     *
     * @param message Сообщение об ошибке, которое будет передано вызывающей стороне.
     */
    public MatrixException(String message) {
        super(message);
    }

    /**
     *  Создает новое исключение MatrixException с указанным сообщением и причиной.
     *  Это позволяет отслеживать цепочку исключений и лучше понимать контекст ошибки.
     *
     * @param message Сообщение об ошибке.
     * @param cause   Исключение, которое привело к возникновению текущего исключения.
     */
    public MatrixException(String message, Throwable cause) {
        super(message, cause);
    }
}
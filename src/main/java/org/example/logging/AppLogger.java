package org.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Утилитный класс для работы с логированием.
 * <p>
 * Этот класс предоставляет удобные методы для записи логов разных уровней.
 * Использует Log4j2 в качестве библиотеки логирования.
 * </p>
 */
public class AppLogger {

    private final Logger logger;

    /**
     * Конструктор AppLogger.
     *
     * @param clazz Класс, для которого будет создаваться логгер.
     */
    public AppLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    /**
     * Логирует информационное сообщение.
     *
     * @param message Сообщение для логирования.
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Логирует предупреждающее сообщение.
     *
     * @param message Сообщение для логирования.
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Логирует предупреждающее сообщение с исключением.
     *
     * @param message Сообщение для логирования.
     * @param e       Исключение, которое будет добавлено в лог.
     */
    public void warn(String message, Throwable e) {
        logger.warn(message, e);
    }

    /**
     * Логирует сообщение об ошибке.
     *
     * @param message Сообщение для логирования.
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * Логирует сообщение об ошибке с исключением.
     *
     * @param message Сообщение для логирования.
     * @param e       Исключение, которое будет добавлено в лог.
     */
    public void error(String message, Throwable e) {
        logger.error(message, e);
    }
}

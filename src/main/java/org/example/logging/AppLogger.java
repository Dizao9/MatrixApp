package org.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс {@code AppLogger} предназначен для упрощения процесса логирования событий в приложении.
 * <p>
 *     Использует библиотеку Log4j2 для управления процессом логирования и предоставляет удобный интерфейс
 *     для записи сообщений разных уровней (INFO, WARN, ERROR).
 * </p>
 */
public class AppLogger {

    private final Logger logger;

    /**
     * Создает экземпляр {@code AppLogger} для конкретного класса.
     *
     * @param clazz Класс, для которого будут записываться логи. Имя этого класса будет использоваться
     *              в логах для идентификации источника сообщения.
     */
    public AppLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    /**
     * Записывает информационное сообщение в лог.
     * <p>
     *  Используется для регистрации обычных событий и ходом выполнения программы.
     * </p>
     *
     * @param message Сообщение, которое будет записано в лог.
     */
    public void info(String message) {
        logger.info(message);
    }

    /**
     * Записывает предупреждающее сообщение в лог.
     * <p>
     *  Используется для сообщений о потенциальных проблемах, не являющихся критичными.
     * </p>
     *
     * @param message Сообщение, которое будет записано в лог.
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Записывает сообщение об ошибке в лог, включая информацию об исключении.
     * <p>
     *  Используется для регистрации ошибок, которые могут повлиять на работу программы.
     * </p>
     *
     * @param message Сообщение, описывающее ошибку.
     * @param e       Исключение, информация о котором будет включена в лог.
     */
    public void error(String message, Throwable e) {
        logger.error(message, e);
    }
}
package com.unir.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

public class LogbackConfig {

    public static void configureLogbackForHibernateSQL() {
        // Obtener el contexto de logger de Logback
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Limpiar cualquier configuración previa
        context.reset();

        // Crear un codificador de layout de patrón
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        encoder.start();

        // Crear un appender de consola
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        // Configurar el paquete de Hibernate para mostrar solo SQL
        Logger hibernateSQLLogger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
        hibernateSQLLogger.setLevel(Level.DEBUG);
        hibernateSQLLogger.addAppender(consoleAppender);
        hibernateSQLLogger.setAdditive(false); // No heredar los appenders del logger raíz

        // Mostrar logs de la aplicacion
        Logger appLoger = (Logger) LoggerFactory.getLogger("com.unir");
        appLoger.setLevel(Level.ALL);
        appLoger.addAppender(consoleAppender);
        appLoger.setAdditive(false); // No heredar los appenders del logger raíz

        // Configurar el logger raíz en un nivel que no muestre otros logs
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ERROR); // Cambia a ERROR para suprimir la mayoría de los logs
        rootLogger.addAppender(consoleAppender);
    }
}

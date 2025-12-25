package com.haisy.app.Logs;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {

    private static final String FILE_NAME = "logs.txt";
    private static final Object LOCK = new Object();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void write(String level, String message) {
        synchronized (LOCK) {
            try {
                Path path = Paths.get(FILE_NAME);

                if (!Files.exists(path)) {
                    Files.createFile(path);
                }

                String logLine = String.format(
                        "[%s] [%s] %s%n",
                        LocalDateTime.now().format(FORMATTER),
                        level,
                        message
                );

                Files.write(
                        path,
                        logLine.getBytes(),
                        StandardOpenOption.APPEND
                );

            } catch (IOException e) {
                e.printStackTrace(); // fallback only
            }
        }
    }

    public static void info(String message) {
        write("INFO", message);
    }

    public static void error(String message) {
        write("ERROR", message);
    }

    public static void debug(String message) {
        write("DEBUG", message);
    }
}

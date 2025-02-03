package net.riezebos.bruus.tbd.game.util.performancelogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PerformanceLoggerManager {


    private static PerformanceLoggerManager instance = new PerformanceLoggerManager();

    private PerformanceLoggerManager(){

    }

    public static PerformanceLoggerManager getInstance(){
        return instance;
    }

    private final Map<String, PerformanceLogger> loggerMap = new HashMap<>();

    public void addLogger(String managerName, PerformanceLogger logger) {
        loggerMap.put(managerName, logger);
    }

    public void exportToJson(String baseName) {
        // Add a timestamp to the filename
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = baseName + "_" + timestamp + ".json";

        // Prepare data for export
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> exportData = new HashMap<>();

        for (Map.Entry<String, PerformanceLogger> entry : loggerMap.entrySet()) {
            exportData.put(entry.getKey(), entry.getValue().toMap());
        }

        // Write data to file
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(exportData, writer);
            System.out.println("Exported logs to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        for (PerformanceLogger logger : loggerMap.values()) {
            logger.reset();
        }
    }

    public static void timeAndLog(PerformanceLogger performanceLogger,String metricName, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        performanceLogger.logMetric(performanceLogger.getManagerName() + " - " +  metricName, (double) (end - start) / 1_000_000); // Convert to milliseconds
    }

    public static void logUpdateGameTick(PerformanceLogger performanceLogger, Runnable updateTask) {
        long start = System.nanoTime();
        updateTask.run(); // Run the update task
        long end = System.nanoTime();

        // Log total execution time
        double executionTimeMs = (end - start) / 1_000_000.0;
        performanceLogger.logExecution(executionTimeMs);

        // Optionally, log a high-level metric for updateGameTick
        performanceLogger.logMetric(performanceLogger.getManagerName() + " - Update GameTick Total", executionTimeMs);
    }


}

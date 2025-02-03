package net.riezebos.bruus.tbd.game.util.performancelogger;

import java.util.HashMap;
import java.util.Map;

public class PerformanceLogger {
    private final String managerName; // Indicates the type of manager
    private double totalExecutionTime;
    private double maxExecutionTime;
    private double minExecutionTime = Double.MAX_VALUE;
    private int executionCount;
    private int slowFrames;
    private double thresholdFactor = 3.5; // Times the average to count as a slow frame
    private final Map<String, PerformanceMetric> metrics = new HashMap<>(); // Tracks specific metrics

    public PerformanceLogger(String managerName) {
        this.managerName = managerName;
        PerformanceLoggerManager.getInstance().addLogger(managerName,this);
    }

    // Log total execution time
    public void logExecution(double executionTime) {
        totalExecutionTime += executionTime;
        maxExecutionTime = Math.max(maxExecutionTime, executionTime);
        minExecutionTime = Math.min(minExecutionTime, executionTime);
        executionCount++;

        double currentAverage = getAverageExecutionTime();
        if (executionTime > (currentAverage * thresholdFactor)) {
            slowFrames++;
        }
    }

    // Log specific metric
    public void logMetric(String metricName, double time) {
        metrics.computeIfAbsent(metricName, k -> new PerformanceMetric()).logTime(time);
    }

    // Get average time for total execution
    public double getAverageExecutionTime() {
        return executionCount == 0 ? 0 : (double) totalExecutionTime / executionCount;
    }

    // Get average time for a specific metric
    public double getAverageTime(String metricName) {
        PerformanceMetric metric = metrics.get(metricName);
        return (metric == null) ? 0 : metric.getAverageTime();
    }

    // Export metrics to a map (e.g., for JSON export)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
//        map.put("managerName", managerName);
        map.put("Average Update GameTick Time", getAverageExecutionTime());
        map.put("Max Update GameTick time", maxExecutionTime);
        map.put("Min Update Game Tick time", minExecutionTime);
        map.put("Slow game ticks (x3.5 average)", slowFrames);
        map.put("Amount of game ticks executed", executionCount);

        // Add specific metrics
        Map<String, Object> metricAverages = new HashMap<>();
        for (Map.Entry<String, PerformanceMetric> entry : metrics.entrySet()) {
            metricAverages.put(entry.getKey(), entry.getValue().getAverageTime());
        }
        map.put("metrics", metricAverages);

        return map;
    }

    // Reset all logged data
    public void reset() {
        totalExecutionTime = 0;
        maxExecutionTime = 0;
        minExecutionTime = Double.MAX_VALUE;
        executionCount = 0;
        slowFrames = 0;
        metrics.clear();
    }

    public String getManagerName () {
        return managerName;
    }
}

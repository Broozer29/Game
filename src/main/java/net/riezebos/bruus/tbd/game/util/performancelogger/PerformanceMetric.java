package net.riezebos.bruus.tbd.game.util.performancelogger;

public class PerformanceMetric {
    private double totalTime;
    private double gameTicks;

    public void logTime(double time) {
        totalTime += time;
        gameTicks++;
    }

    public double getAverageTime() {
        return gameTicks == 0 ? 0 : (double) totalTime / gameTicks;
    }

    public void reset() {
        totalTime = 0;
        gameTicks = 0;
    }

    @Override
    public String toString() {
        return "PerformanceMetric{" +
                "averageTime=" + getAverageTime() +
                ", totalTime=" + totalTime +
                ", gameTicks=" + gameTicks +
                '}';
    }
}

package game.objects.timers;

public interface TimerInterface {
    void startOfTimer();
    void endOfTimer();
    boolean shouldActivate();
    void setCurrentTime(float currentTime);
    boolean getLoopable();
    TimerStatusEnums getStatus();

}

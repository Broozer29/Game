package game.objects.timers;

public interface TimerInterface {
    void startOfTimer();
    void endOfTimer();
    boolean shouldActivate(float currentTime);
    void setCurrentTime(float currentTime);
    boolean getLoopable();
    TimerStatusEnums getStatus();

}

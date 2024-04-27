package game.deprecated.pregeneratedlevels;

import java.util.List;

import game.objects.powerups.timers.DeprecatedEnemySpawnTimer;
import VisualAndAudioData.audio.enums.AudioEnums;

public interface Level {
	public void initRepeatableTimers();
	public void initSingleFireTimers();
	public AudioEnums getSong();
	public List<DeprecatedEnemySpawnTimer> getTimers();
}

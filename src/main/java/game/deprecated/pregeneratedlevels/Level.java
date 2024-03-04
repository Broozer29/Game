package game.deprecated.pregeneratedlevels;

import java.util.List;

import game.spawner.EnemySpawnTimer;
import VisualAndAudioData.audio.enums.AudioEnums;

public interface Level {
	public void initRepeatableTimers();
	public void initSingleFireTimers();
	public AudioEnums getSong();
	public List<EnemySpawnTimer> getTimers();
}

package game.levels;

import java.util.List;

import data.audio.AudioEnums;
import game.spawner.EnemySpawnTimer;

public interface Level {
	public void initRepeatableTimers();
	public void initSingleFireTimers();
	public AudioEnums getSong();
	public List<EnemySpawnTimer> getTimers();
}

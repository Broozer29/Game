package game.levels;

import java.util.List;

import game.spawner.EnemySpawnTimer;
import gamedata.audio.AudioEnums;

public interface Level {
	public void initRepeatableTimers();
	public void initSingleFireTimers();
	public AudioEnums getSong();
	public List<EnemySpawnTimer> getTimers();
}

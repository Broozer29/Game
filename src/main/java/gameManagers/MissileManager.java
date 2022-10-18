package gameManagers;

import java.util.ArrayList;
import java.util.List;

import gameObjectes.Missile;


public class MissileManager {

	
	private MissileManager instance = new MissileManager();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
	
	private MissileManager() {
		
	}
	
	public MissileManager getInstance() {
		return this.instance;
	}
	
	public void addFriendlyMissile(Missile friendlyMissile) {
		this.friendlyMissiles.add(friendlyMissile);
	}
	
	public void addEnemyMissile(Missile enemyMissile) {
		this.enemyMissiles.add(enemyMissile);
	}
	
}

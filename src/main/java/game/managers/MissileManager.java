package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import game.objects.enemies.Enemy;
import game.objects.enemies.Tazer;
import game.objects.missiles.AlienLaserbeam;
import game.objects.missiles.BombaProjectile;
import game.objects.missiles.BulldozerProjectile;
import game.objects.missiles.DefaultPlayerLaserbeam;
import game.objects.missiles.EnergizerProjectile;
import game.objects.missiles.FlamerProjectile;
import game.objects.missiles.Missile;
import game.objects.missiles.SeekerProjectile;
import game.objects.missiles.TazerProjectile;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private MovementManager movementManager = MovementManager.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
	private List<AlienLaserbeam> alienLaserbeams = new ArrayList<AlienLaserbeam>();
	private List<FlamerProjectile> flamerProjectiles = new ArrayList<FlamerProjectile>();
	private List<BombaProjectile> bombaProjectiles = new ArrayList<BombaProjectile>();
	private List<BulldozerProjectile> bulldozerProjectiles = new ArrayList<BulldozerProjectile>();
	private List<EnergizerProjectile> energizerProjectiles = new ArrayList<EnergizerProjectile>();
	private List<SeekerProjectile> seekerProjectiles = new ArrayList<SeekerProjectile>();
	private List<TazerProjectile> tazerProjectiles = new ArrayList<TazerProjectile>();

	private MissileManager() {
	}

	public void resetManager() {
		enemyMissiles = new ArrayList<Missile>();
		friendlyMissiles = new ArrayList<Missile>();
	}

	public static MissileManager getInstance() {
		return instance;
	}

	public List<Missile> getEnemyMissiles() {
		return enemyMissiles;
	}

	public List<Missile> getFriendlyMissiles() {
		return friendlyMissiles;
	}

	public void firePlayerMissile(int xCoordinate, int yCoordinate, String missileType, String explosionType,
			int angleModuloDivider, String rotation, float scale) {
		switch (missileType) {
		case ("Player Laserbeam"):
			Missile newMissile = new DefaultPlayerLaserbeam(xCoordinate, yCoordinate, missileType, explosionType,
					"Right", angleModuloDivider, rotation, scale);
			this.friendlyMissiles.add(newMissile);
		}

	}

	// Called by all enemy classes when fireAction() is called.
	public void addEnemyMissile(int xCoordinate, int yCoordinate, String missileType, String explosionType,
			int angleModuloDivider, String missileDirection, String rotation, float scale) {
		switch (missileType) {
		case ("Alien Laserbeam"):
			Missile alienMissile = new AlienLaserbeam(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(alienMissile);
			this.alienLaserbeams.add((AlienLaserbeam) alienMissile);
			break;
		case ("Seeker Projectile"):
			Missile seekerMissile = new SeekerProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(seekerMissile);
			this.seekerProjectiles.add((SeekerProjectile) seekerMissile);
			break;
		case ("Flamer Projectile"):
			Missile flamerMissile = new FlamerProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(flamerMissile);
			this.flamerProjectiles.add((FlamerProjectile) flamerMissile);
			break;
		case ("Tazer Projectile"):
			Missile tazerMissile = new TazerProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(tazerMissile);
			this.tazerProjectiles.add((TazerProjectile) tazerMissile);
			break;
		case ("Bulldozer Projectile"):
			Missile bulldozerMissile = new BulldozerProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(bulldozerMissile);
			this.bulldozerProjectiles.add((BulldozerProjectile) bulldozerMissile);
			break;
		case ("Bomba Projectile"):
			Missile bombaMissile = new BombaProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(bombaMissile);
			this.bombaProjectiles.add((BombaProjectile) bombaMissile);
			break;
		case ("Energizer Projectile"):
			Missile energizerMissile = new EnergizerProjectile(xCoordinate, yCoordinate, missileType, explosionType,
					missileDirection, angleModuloDivider, rotation, scale);
			this.enemyMissiles.add(energizerMissile);
			this.energizerProjectiles.add((EnergizerProjectile) energizerMissile);
			break;
		}

	}

	public void updateGameTick() {
		moveMissiles();
		checkFriendlyMissileCollision();
		checkEnemyMissileCollision();
		triggerMissileAction();
	}

	// Checks collision between friendly missiles and enemies
	private void checkFriendlyMissileCollision() {
		if(enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}
		for (Missile m : friendlyMissiles) {
			if (m.isVisible()) {
				Rectangle r1 = m.getBounds();
				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
						enemy.takeDamage(m.getMissileDamage());
						if (m.getExplosionAnimation() != null) {
							animationManager.addUpperAnimation(m.getExplosionAnimation());
						}
						m.setVisible(false);
					}
				}
			}
		}
	}

	// Checks collision between enemy missiles and the player shapeship
	private void checkEnemyMissileCollision() {
		if (animationManager == null || friendlyManager == null) {
			animationManager = AnimationManager.getInstance();
			friendlyManager = FriendlyManager.getInstance();
		}
		for (Missile m : enemyMissiles) {
			if (m.isVisible()) {
				Rectangle r1 = m.getAnimation().getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();
				
				if (r1.intersects(r2)) {
					friendlyManager.getSpaceship().takeHitpointDamage(m.getMissileDamage());
					animationManager.addUpperAnimation(m.getExplosionAnimation());
					m.setVisible(false);
//					animationManager.addPlayerShieldDamageAnimation();
				}
			}
		}
	}

	private void moveMissiles() {
		// Move friendly missiles
		for (int i = 0; i < friendlyMissiles.size(); i++) {
			Missile missile = friendlyMissiles.get(i);
			if (missile.isVisible()) {
				movementManager.moveMissile(missile);
			} else {
				removeFriendlyMissile(missile);
			}
		}

		// Move enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {
			Missile missile = enemyMissiles.get(i);

			if (missile.isVisible()) {
				movementManager.moveMissile(missile);
			} else {
				removeEnemyMissile(missile);
			}
		}
	}

	private void triggerMissileAction() {
		for (AlienLaserbeam missile : alienLaserbeams) {
			missile.missileAction();
		}
		for (FlamerProjectile missile : flamerProjectiles) {
			missile.missileAction();
		}
		for (BombaProjectile missile : bombaProjectiles) {
			missile.missileAction();
		}
		for (EnergizerProjectile missile : energizerProjectiles) {
			missile.missileAction();
		}
		for (SeekerProjectile missile : seekerProjectiles) {
			missile.missileAction();
		}
		for (TazerProjectile missile : tazerProjectiles) {
			missile.missileAction();
		}
	}

	private void removeFriendlyMissile(Missile missile) {
		switch (missile.getMissileType()) {
		case ("Player Laserbeam"):
			this.friendlyMissiles.remove(missile);
			break;
		}
	}

	private void removeEnemyMissile(Missile missile) {
		switch (missile.getMissileType()) {
		case ("Alien Laserbeam"):
			this.alienLaserbeams.remove(missile);
			break;
		case ("Seeker Projectile"):
			this.seekerProjectiles.remove(missile);
			break;
		case ("Flamer Projectile"):
			this.flamerProjectiles.remove(missile);
			break;
		case ("Tazer Projectile"):
			this.tazerProjectiles.remove(missile);
			break;
		case ("Bulldozer Projectile"):
			this.bulldozerProjectiles.remove(missile);
			break;
		case ("Bomba Projectile"):
			this.bombaProjectiles.remove(missile);
			break;
		case ("Energizer Projectile"):
			this.energizerProjectiles.remove(missile);
			break;
		}
		this.enemyMissiles.remove(missile);
	}

}

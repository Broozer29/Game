package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.enums.ImageEnums;
import game.objects.Explosion;
import image.objects.SpriteAnimation;

public class ExplosionManager {

	private static ExplosionManager instance = new ExplosionManager();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private List<Explosion> explosionList = new ArrayList<Explosion>();

	private ExplosionManager() {

	}

	public void updateGametick() {
		removeFinishedExplosions();
		checkExplosionCollision();
	}

	public static ExplosionManager getInstance() {
		return instance;
	}

	public void addExplosion(int xCoordinate, int yCoordinate, ImageEnums explosionType, float scale, float damage) {
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
		animation.setX(xCoordinate - animation.getWidth() / 2);
		animation.setY(yCoordinate - animation.getHeight() / 2);
		Explosion explosion = new Explosion(xCoordinate, yCoordinate, scale, animation, damage);
		explosion.setX(xCoordinate - animation.getWidth() / 2);
		explosion.setY(yCoordinate - animation.getHeight() / 2);
		this.explosionList.add(explosion);
	}

	public List<Explosion> getExplosions() {
		return this.explosionList;
	}

	private void removeFinishedExplosions() {
		for (int i = 0; i < explosionList.size(); i++) {
			if (!explosionList.get(i).getAnimation().isVisible()) {
				explosionList.get(i).setVisible(false);
				explosionList.get(i).getAnimation().setVisible(false);
				explosionList.remove(i);
			}
		}
	}

	private void checkExplosionCollision() {
		if (friendlyManager == null) {
			friendlyManager = FriendlyManager.getInstance();
		}
		for (Explosion explosion : explosionList) {
			if (explosion.isVisible()) {
				Rectangle r1 = explosion.getAnimation().getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();
				if (r1.intersects(r2)) {
					if (!explosion.getDealtDamage()) {
						friendlyManager.getSpaceship().takeHitpointDamage(explosion.getDamage());
						explosion.setDealtDamage(true);
//						animationManager.addPlayerShieldDamageAnimation();
					}
				}
			}
		}
	}

	public void resetManager() {
		this.explosionList = new ArrayList<Explosion>();
	}

}

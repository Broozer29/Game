package net.riezebos.bruus.tbd.bot;

import net.riezebos.bruus.tbd.controllerInput.BotInputReader;
import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.Portal;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.primary.FireFighterPrimaryGun;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

// Decides where the ship flies and when it fires. It is not a good player: it keeps away from enemies and enemy missiles,
// lines up with the nearest enemy, fires all the time and uses the special attack when it is crowded.
// The point is a ship that stays alive and busy long enough to put the game under load, not one that plays like a person.
class BotBrain {
    private static final double MISSILE_RANGE = 380;
    private static final double ENEMY_RANGE = 320;
    private static final double WALL_MARGIN = 120;
    private static final double HOME_X_FRACTION = 0.25; // the ship drifts back towards a quarter of the way across the screen
    private static final double SPECIAL_DANGER_LEVEL = 1.0;
    private static final int SPECIAL_CROWD_SIZE = 4;
    private static final double IMMUNE_STANDOFF = 250; // when immune, the ship parks this far left of the nearest enemy

    private long ticks = 0;
    private boolean refuelling = false;

    // One tick of play. The ship may be null: the player is dying or has not spawned yet.
    void play(BotInputReader reader, SpaceShip ship) {
        ticks++;
        if (ship == null) {
            reader.releaseGameplayInput();
            return;
        }
        int windowWidth = DataClass.getInstance().getWindowWidth();
        int windowHeight = DataClass.getInstance().getWindowHeight();
        double shipX = ship.getCenterXCoordinate();
        double shipY = ship.getCenterYCoordinate();
        double shipSize = (ship.getWidth() + ship.getHeight()) / 4.0;

        // Automatic: with player immunity on nothing can hurt the ship, so it ignores missiles and enemies instead of running from them
        boolean avoidDanger = BotSettings.isDodging();
        double[] push = new double[2];
        double danger = 0;
        if (avoidDanger) {
            for (Missile missile : MissileManager.getInstance().getMissilesByAllegiance(false)) {
                if (missile.isVisible()) {
                    danger += repel(push, shipX, shipY, shipSize, missile, MISSILE_RANGE, 2.5);
                }
            }
        }

        Enemy closestEnemy = null;
        double closestDistance = Double.MAX_VALUE;
        int enemyCount = 0;
        boolean bossOnScreen = false;
        for (Enemy enemy : EnemyManager.getInstance().getEnemies()) {
            if (!enemy.isVisible()) {
                continue;
            }
            enemyCount++;
            bossOnScreen |= isBoss(enemy);
            if (avoidDanger) {
                danger += repel(push, shipX, shipY, shipSize, enemy, ENEMY_RANGE, 1.5);
            }
            double distance = Math.hypot(enemy.getCenterXCoordinate() - shipX, enemy.getCenterYCoordinate() - shipY);
            if (BotSettings.targetsBoss() && isBoss(enemy)) {
                distance = -1; // the boss is the target, so the level can finish
            }
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }

        // Stay on screen
        if (shipX < WALL_MARGIN) {
            push[0] += (WALL_MARGIN - shipX) / WALL_MARGIN;
        } else if (shipX > windowWidth - WALL_MARGIN) {
            push[0] -= (shipX - (windowWidth - WALL_MARGIN)) / WALL_MARGIN;
        }
        if (shipY < WALL_MARGIN) {
            push[1] += (WALL_MARGIN - shipY) / WALL_MARGIN;
        } else if (shipY > windowHeight - WALL_MARGIN) {
            push[1] -= (shipY - (windowHeight - WALL_MARGIN)) / WALL_MARGIN;
        }

        // Without danger: line up with the nearest enemy to hit it, and drift back home
        double calm = 1 - Math.min(danger, 1);
        if (closestEnemy != null) {
            push[1] += clamp((closestEnemy.getCenterYCoordinate() - shipY) / 200.0) * 0.5 * calm;
        }
        if (avoidDanger) {
            push[0] += clamp((windowWidth * HOME_X_FRACTION - shipX) / (windowWidth * 0.5)) * 0.3 * calm;
        } else if (closestEnemy != null) {
            // Immune: close in on the nearest enemy to a short firing distance instead of drifting home
            push[0] += clamp((closestEnemy.getCenterXCoordinate() - IMMUNE_STANDOFF - shipX) / 300.0) * 0.8;
        }

        // A slow sway so the ship does not park itself in one spot
        push[0] += Math.sin(ticks / 90.0) * 0.15 * calm;
        push[1] += Math.cos(ticks / 63.0) * 0.15 * calm;

        applyMove(reader, push[0], push[1]);
        boolean fire = BotSettings.getFireMode() == BotScenario.FireMode.ALWAYS;
        if (fire && ship.getSpaceShipRegularGun() instanceof FireFighterPrimaryGun) {
            // The Firefighter's flames use fuel: hold fire below the gun's minimum and fire again once the tank is 90% full
            FireFighterPrimaryGun gun = (FireFighterPrimaryGun) ship.getSpaceShipRegularGun();
            float max = gun.getOrangeBarMaxValue(ship);
            float current = max <= 0 ? 0 : gun.getOrangeBarCurrentValue(ship);
            if (max <= 0 || current >= 0.9f * max) {
                refuelling = false;
            } else if (current < gun.getMinimumFuelToFire()) {
                refuelling = true;
            }
            fire = !refuelling;
        }
        reader.setFire(fire);

        boolean hasCharge = ship.getSpecialGun() != null && ship.getSpecialGun().getSpecialAttackCharges() > 0;
        boolean crowded = danger >= SPECIAL_DANGER_LEVEL || enemyCount >= SPECIAL_CROWD_SIZE || bossOnScreen;
        switch (BotSettings.getSpecialMode()) {
            case ALWAYS:
                reader.setSpecialAttack(hasCharge);
                break;
            case NEVER:
                reader.setSpecialAttack(false);
                break;
            default:
                reader.setSpecialAttack(hasCharge && crowded);
                break;
        }
    }

    // After the level: fly into the portal that ends it
    void flyToPortal(BotInputReader reader, SpaceShip ship) {
        Portal portal = FriendlyManager.getInstance().getFinishedLevelPortal();
        if (ship == null || portal == null) {
            reader.releaseGameplayInput();
            return;
        }
        double dx = portal.getCenterXCoordinate() - ship.getCenterXCoordinate();
        double dy = portal.getCenterYCoordinate() - ship.getCenterYCoordinate();
        applyMove(reader, dx / 150.0, dy / 150.0); // slows down when close, so it settles on the portal instead of overshooting
        reader.setFire(false);
        reader.setSpecialAttack(false);
    }

    // Pushes the vector away from the object when it is close. Returns how strong the push was.
    private double repel(double[] push, double shipX, double shipY, double shipSize, GameObject threat, double range, double weight) {
        double dx = shipX - threat.getCenterXCoordinate();
        double dy = shipY - threat.getCenterYCoordinate();
        double distance = Math.hypot(dx, dy);
        double gap = distance - (threat.getWidth() + threat.getHeight()) / 4.0 - shipSize; // distance between the edges, roughly
        if (gap >= range) {
            return 0;
        }
        double strength = Math.pow(1 - Math.max(gap, 0) / range, 2) * weight;
        if (distance < 1) {
            push[1] += strength; // exactly on top of it: any direction will do
        } else {
            push[0] += dx / distance * strength;
            push[1] += dy / distance * strength;
        }
        return strength;
    }

    private void applyMove(BotInputReader reader, double x, double y) {
        double length = Math.hypot(x, y);
        if (length > 1) {
            x /= length;
            y /= length;
        }
        reader.setMove((float) x, (float) y);
    }

    // EnemyManager.isBossAlive() is true before the first boss has spawned, so look at the enemies themselves
    static boolean isBoss(Enemy enemy) {
        return enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss);
    }

    private double clamp(double value) {
        return Math.max(-1, Math.min(1, value));
    }
}

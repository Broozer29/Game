package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.YellowBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ReflectiveBlocks extends Missile {

    private boolean isFinalBossProtectiveReflectiveBlock = false;
    private List<Missile> alreadyReflectedMissiles = new ArrayList<>();

    public ReflectiveBlocks(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration, movementConfiguration);
        this.isDestructable = false;
        this.isDamageable = true;
        this.knockbackStrength = 10;
    }


    private double lastCheckedTime = GameState.getInstance().getGameSeconds();

    @Override
    public void missileAction() {
        if (isFinalBossProtectiveReflectiveBlock && !this.shouldChangeTransparancy) {
            //do the special thing, then exit early
            this.allowedVisualsToRotate = true;
            this.rotateGameObjectTowards(this.ownerOrCreator.getCenterXCoordinate(), this.ownerOrCreator.getCenterYCoordinate(), true);
            this.allowedVisualsToRotate = false;

            float currentHitPoints = Math.max(0.05f, this.getCurrentHitpoints() / this.getMaxHitPoints());
            if (currentHitPoints <= 0.2f) {
                this.setVisible(false);
            } else {
                this.setTransparancyAlpha(false, currentHitPoints, 0.0f);
            }
            return;
        }


        if (GameState.getInstance().getGameSeconds() > lastCheckedTime + 0.25f) { //Can deal damage on a 0,5 second interval
            super.collidedObjects.clear();
            lastCheckedTime = GameState.getInstance().getGameSeconds();
        }

        // Clean up missiles that are no longer visible from the reflected list
        alreadyReflectedMissiles.removeIf(missile -> !missile.isVisible());

        //sloppy temporary (maybe) fix for the blocks not being destroyed when they are out of the screen
        if (this.xCoordinate <= 200 && this.ownerOrCreator.getOwnerOrCreator() instanceof YellowBoss) {
            setVisible(false);
        }
    }

    @Override
    public boolean isShowHealthBar() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void handleCollision(GameObject collidedObject) {
        if (!collidedObjects.contains(collidedObject)) {
            collidedObjects.add(collidedObject);
            super.dealDamageToGameObject(collidedObject);
        }
    }

    public void reflectMissile(Missile missile) {
        // Skip reflection if this block has already reflected this specific missile
        if (alreadyReflectedMissiles.contains(missile)) {
            return;
        }

        // Skip reflection if on same team AND missile has never been reflected
        // This allows previously reflected missiles to bounce between same-team blocks
        if (missile.isFriendly() == this.isFriendly() && (missile.getTimesReflected() == 0)) {
            return;
        }

        // Check if missile has reached max reflections
        if (!missile.canBeReflected()) {
            missile.destroyMissile();
            return;
        }

        missile.dealDamageToGameObject(this);
        if (this.getCurrentHitpoints() < this.getMaxHitPoints()) {
            this.setShowHealthBar(true);
        }

        if (missile.isExplosive()) {
            missile.detonateMissile();
            return;
        }


        if (this.getCurrentHitpoints() <= 0) {
            missile.setVisible(false);
            this.setVisible(false);
            return;
        }


        double newAngle = calculateMovementAngle(missile.getMovementConfiguration().getCurrentPath().getWaypoints());
        Point newDestination = calculatePositionBasedOnAngle(newAngle,
                300, missile.getCenterXCoordinate(), missile.getCenterYCoordinate());
        missile.resetMovementPath();
        missile.setPathFinder(new StraightLinePathFinder());
        missile.getMovementConfiguration().setMovementSpeed(Math.max(missile.getMovementConfiguration().getMovementSpeed() * 0.5f, 1.75f));
        newDestination.setX(newDestination.getX() - missile.getWidth() / 2);
        newDestination.setY(newDestination.getY() - missile.getHeight() / 2);
        missile.setAllowedVisualsToRotate(true);
        missile.getMovementConfiguration().setDestination(newDestination);
        missile.rotateObjectTowardsDestination(false);
        missile.setAllowedVisualsToRotate(false);
        missile.setDamage(this.damage);
        missile.setFriendly(this.isFriendly()); //change teams
        missile.setOwnerOrCreator(this.ownerOrCreator);
        missile.incrementTimesReflected(); // Track reflection count

        // Add to list of already reflected missiles to prevent immediate re-reflection
        alreadyReflectedMissiles.add(missile);
    }

    private double calculateMovementAngle(List<Point> waypoints) {
        if (waypoints == null || waypoints.size() < 2) {
            return 0.0; // Default angle if not enough points
        }

        // Use ONLY the first and last points in the waypoints list
        Point start = waypoints.get(0); // First point (start)
        Point end = waypoints.get(waypoints.size() - 1); // Last point (end)

        // Calculate the direction vector (dx, dy)
        int dx = end.getX() - start.getX();
        int dy = start.getY() - end.getY(); // Flip dy for Y-axis convention

        // Calculate the current angle in radians
        double angleRadians = Math.atan2(dy, dx);

        // Convert to degrees
        double angleDegrees = Math.toDegrees(angleRadians);

        // Normalize the angle [0, 360)
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }

        // Flip the direction by adding 180° and normalizing
        double flippedAngle = (angleDegrees + 180) % 360;

        // Return the flipped angle
        return flippedAngle;
    }


    private Point calculatePositionBasedOnAngle(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    @Override
    public void destroyMissile() {
        //overriden so it's impossible to destroy it
    }

    public boolean isFinalBossProtectiveReflectiveBlock() {
        return isFinalBossProtectiveReflectiveBlock;
    }

    public void setFinalBossProtectiveReflectiveBlock(boolean finalBossProtectiveReflectiveBlock) {
        isFinalBossProtectiveReflectiveBlock = finalBossProtectiveReflectiveBlock;
    }
}

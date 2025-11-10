package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.List;

public class ReflectiveBlocks extends Missile {

    public ReflectiveBlocks(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, missileConfiguration, movementConfiguration);
        this.isDestructable = false;
        this.isDamageable = true;
        this.knockbackStrength = 10;
    }


    private double lastCheckedTime = GameState.getInstance().getGameSeconds();
    @Override
    public void missileAction() {
        if(GameState.getInstance().getGameSeconds() > lastCheckedTime + 0.25f){ //Can deal damage on a 0,5 second interval
            super.collidedObjects.clear();
            lastCheckedTime = GameState.getInstance().getGameSeconds();
        }
    }

    @Override
    public void handleCollision (GameObject collidedObject) {
        if(!collidedObjects.contains(collidedObject)) {
            collidedObjects.add(collidedObject);
            super.dealDamageToGameObject(collidedObject);
        }
    }

    public void reflectMissile(Missile missile){
        if(missile.isFriendly() == this.isFriendly()){
            return; //they are on the same team
        }

        missile.dealDamageToGameObject(this);
        if(this.getCurrentHitpoints() < this.getMaxHitPoints()){
            this.setShowHealthBar(true);
        }

        if(missile.isExplosive()){
            missile.detonateMissile();
            return;
        }


        if(this.getCurrentHitpoints() <= 0){
            missile.setVisible(false);
            this.setVisible(false);
            return;
        }


        double newAngle = calculateMovementAngle(missile.getMovementConfiguration().getCurrentPath().getWaypoints());
        Point newDestination = calculatePositionBasedOnAngle(newAngle,
                300, missile.getCenterXCoordinate(), missile.getCenterYCoordinate());
        missile.resetMovementPath();
        missile.setPathFinder(new StraightLinePathFinder());
        newDestination.setX(newDestination.getX() - missile.getWidth() / 2);
        newDestination.setY(newDestination.getY() - missile.getHeight() / 2);
        missile.setAllowedVisualsToRotate(true);
        missile.getMovementConfiguration().setDestination(newDestination);
        missile.rotateObjectTowardsDestination(false);
        missile.setAllowedVisualsToRotate(false);
        missile.setDamage(this.damage);
        missile.setFriendly(this.isFriendly()); //change teams
        missile.setOwnerOrCreator(this.ownerOrCreator);
        missile.setDamage(missile.getDamage() * 0.5f);
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



    private Point calculatePositionBasedOnAngle (double angleDegrees, int distance, int centerX, int centerY) {
        // Convert to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    @Override
    public void destroyMissile () {
        //overriden so it's impossible to destroy it
    }
}

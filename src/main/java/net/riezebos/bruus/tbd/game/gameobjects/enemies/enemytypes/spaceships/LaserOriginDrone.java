package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class LaserOriginDrone extends Enemy {
    private LaserOriginDrone connectedDrone;

    private List<Laserbeam> laserbeamList = new ArrayList<>();
    private Laserbeam laserbeam;
    private Direction directionFromWhichTheLaserStarts;


    public LaserOriginDrone (SpriteConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig, 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

        this.hasAttack = false;
        this.detonateOnCollision = false;
        this.knockbackStrength = 10;
        this.damage = 10;
    }

    public void setConnectedDrone (LaserOriginDrone connectedDrone) {
        this.connectedDrone = connectedDrone;
    }
    public void initLaserbeam(int segmentAmount, Direction direction) {
        if (laserbeam == null) {
            laserbeamList = new ArrayList<>();

//            LaserbeamConfiguration configLeft = createLaserbeamConfig(Direction.LEFT);
//            TrackingLaserBeam beam = new TrackingLaserBeam(configLeft);
//            beam.setMaxRotationPerUpdate(0.15f);
//            beam.setBlocksMovement(true);
//            beam.setAngleDegrees(Direction.LEFT_DOWN.toAngle());
//            laserbeamList.add(beam);
//
            LaserbeamConfiguration configLeft = createLaserbeamConfig(Direction.LEFT);
            laserbeamList.add(new AngledLaserBeam(configLeft));

            LaserbeamConfiguration configRight = createLaserbeamConfig(Direction.RIGHT);
            laserbeamList.add(new AngledLaserBeam(configRight));

            LaserbeamConfiguration configUp = createLaserbeamConfig(Direction.UP);
            laserbeamList.add(new AngledLaserBeam(configUp));

            LaserbeamConfiguration configDown = createLaserbeamConfig(Direction.DOWN);
            laserbeamList.add(new AngledLaserBeam(configDown));

            for (Laserbeam laserbeam : laserbeamList) {
                MissileManager.getInstance().addLaserBeam(laserbeam);
            }
        }
    }

    private LaserbeamConfiguration createLaserbeamConfig(Direction dir) {
        LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(false, 0);
        laserbeamConfiguration.setOriginObject(this);
        laserbeamConfiguration.setOwner(this);
        laserbeamConfiguration.setAmountOfLaserbeamSegments(20);
//        laserbeamConfiguration.setDirection(dir);
//        laserbeamConfiguration.setAngleDegrees(dir.toAngle());

        laserbeamConfiguration.setTargetToAimAt(PlayerManager.getInstance().getSpaceship());
        Point offsetAmounts = getOffsetAmounts(dir);

        laserbeamConfiguration.setxOffset(offsetAmounts.getX());
        laserbeamConfiguration.setyOffset(offsetAmounts.getY());

        return laserbeamConfiguration;
    }

    private Point getOffsetAmounts(Direction direction) {
        Point point = new Point(0, 0);
        int halfEnemyWidth = this.getWidth() / 2;
        int halfEnemyHeight = this.getHeight() / 2;
        int halfLaserWidth = Laserbeam.bodyWidth / 2; // Assuming bodyWidth is the width of the laser beam segment
        int halfLaserHeight = Laserbeam.bodyWidth / 2; // Assuming square segments

        // Additional offsets to shift all beams further toward the top-left
        int globalXShift = halfLaserWidth;  // Shift to the left by half a laser beam's width
        int globalYShift = halfLaserHeight; // Shift upward by half a laser beam's height

        switch (direction) {
            case RIGHT:
                // Shift left slightly to move the beam left
                point.setX(halfEnemyWidth + halfLaserWidth - globalXShift);
                point.setY(0 - (globalYShift / 2)); // Apply global Y shift
                break;
            case LEFT:
                // Shift left even more to move the beam further left
                point.setX(-halfEnemyWidth - halfLaserWidth - globalXShift);
                point.setY(0 - (globalXShift / 2)); // Apply global Y shift
                break;
            case UP:
                // Shift upward further
                point.setX(0 - (globalXShift / 2)); // Apply global X shift
                point.setY(-halfEnemyHeight - halfLaserHeight - globalYShift);
                break;
            case DOWN:
                // Shift downward
                point.setX(0 - (globalXShift / 2)); // Apply global X shift
                point.setY(halfEnemyHeight + halfLaserHeight - globalYShift);
                break;
        }

        return point;
    }


    @Override
    public void fireAction () {
//        laserbeam.update();
    }

    public Direction getDirectionFromWhichTheLaserStarts () {
        return directionFromWhichTheLaserStarts;
    }

    public void setDirectionFromWhichTheLaserStarts (Direction directionFromWhichTheLaserStarts) {
        this.directionFromWhichTheLaserStarts = directionFromWhichTheLaserStarts;
    }

    public Laserbeam getLaserbeam () {
        return laserbeam;
    }
}

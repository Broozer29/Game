package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class LaserbeamMiniBoss extends Enemy {


    private int firstRotationAngleDegrees = 0;
    private int secondRotationAngleDegrees = 180;
    private int thirdRotationAngleDegrees = 90;
    private int fourthRotationAngleDegrees = 270;

    private Laserbeam firstLaserbeam;
    private Laserbeam secondLaserbeam;
    private Laserbeam thirdLaserbeam;
    private Laserbeam fourthLaserbeam;

    private boolean isFiring = false;

    public LaserbeamMiniBoss(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(3);
        this.damage = 6;
        this.detonateOnCollision = false;
        this.knockbackStrength = 10;
        this.attackSpeed = 0.025f;
    }


    @Override
    public void fireAction() {
        if (this.movementConfiguration.getPathFinder() instanceof DestinationPathFinder) {
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
        }

        allowedToFire = true;

        if(!isFiring && WithinVisualBoundariesCalculator.isWithinBoundaries(this) && allowedToFire){
            //init the laserbeams and fire
            firstLaserbeam = createLaserbeam(firstRotationAngleDegrees);
            MissileManager.getInstance().addLaserBeam(firstLaserbeam);

            secondLaserbeam = createLaserbeam(secondRotationAngleDegrees);
            MissileManager.getInstance().addLaserBeam(secondLaserbeam);

            thirdLaserbeam = createLaserbeam(thirdRotationAngleDegrees);
            MissileManager.getInstance().addLaserBeam(thirdLaserbeam);

            fourthLaserbeam = createLaserbeam(fourthRotationAngleDegrees);
            MissileManager.getInstance().addLaserBeam(fourthLaserbeam);

            this.isFiring = true;


        } else if(isFiring && WithinVisualBoundariesCalculator.isWithinBoundaries(this)){
            double currentTime = GameState.getInstance().getGameSeconds();
            if(currentTime >= lastAttackTime + this.getAttackSpeed()){
                this.firstRotationAngleDegrees = increaseRotationAngle(this.firstRotationAngleDegrees);
                this.secondRotationAngleDegrees = increaseRotationAngle(this.secondRotationAngleDegrees);
                this.thirdRotationAngleDegrees = increaseRotationAngle(this.thirdRotationAngleDegrees);
                this.fourthRotationAngleDegrees = increaseRotationAngle(this.fourthRotationAngleDegrees);
                lastAttackTime = currentTime;
            }


            firstLaserbeam.setOriginPoint(this.getCurrentCenterLocation());
            secondLaserbeam.setOriginPoint(this.getCurrentCenterLocation());
            thirdLaserbeam.setOriginPoint(this.getCurrentCenterLocation());
            fourthLaserbeam.setOriginPoint(this.getCurrentCenterLocation());

            firstLaserbeam.setAngleDegrees(this.firstRotationAngleDegrees);
            secondLaserbeam.setAngleDegrees(this.secondRotationAngleDegrees);
            thirdLaserbeam.setAngleDegrees(this.thirdRotationAngleDegrees);
            fourthLaserbeam.setAngleDegrees(this.fourthRotationAngleDegrees);
        }
    }



    private Laserbeam createLaserbeam(int angleDegrees) {
        LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(false, this.damage);
        laserbeamConfiguration.setOriginObject(this);
        laserbeamConfiguration.setOwner(this);
        laserbeamConfiguration.setAmountOfLaserbeamSegments(7);
        laserbeamConfiguration.setAngleDegrees(angleDegrees);

        AngledLaserBeam laserbeam = new AngledLaserBeam(laserbeamConfiguration);
        laserbeam.setxOffset(-Laserbeam.bodyWidth / 2);
        laserbeam.setyOffset(-Laserbeam.bodyWidth / 2);

        return laserbeam;
    }

    private int increaseRotationAngle(int rotationAngle) {
        int newAngle = rotationAngle + 1;
        if (newAngle > 360) {
            newAngle = 0;
        }

        return newAngle;
    }

}
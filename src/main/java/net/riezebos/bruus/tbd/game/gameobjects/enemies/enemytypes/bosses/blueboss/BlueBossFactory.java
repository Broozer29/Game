package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.behaviour.BlueBossFireLaserbeams;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.FloatingPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class BlueBossFactory extends Enemy {

    private boolean hasCreatedDefenders = false;
    private SpriteAnimation chargingAnimation;
    private Laserbeam primaryLaserbeam = null;
    private Laserbeam secondaryLaserbeam = null;

    private boolean startChargingLaserbeams = false;
    private boolean isFiringLaserbeams = false;
    private boolean canBuildNeedlers = false;
    private double lastTimeNeedlerBuild = 0;
    private double needlerBuildInterval = 7;
    private Random random = new Random();

    private Direction primaryDirection = Direction.NONE;
    private Direction secondaryDirection = Direction.NONE;
    private BlueBossFactorySpawnPosition spawnPosition = null;

    private boolean finishedPostInit = false;

    public BlueBossFactory(SpriteAnimationConfiguration spriteConfig, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfig, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfig.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 5;
        this.allowedToMove = false;
        this.attackSpeed = 1.5f;
        this.detonateOnCollision = false;
        this.knockbackStrength = 12;

        this.maxHitPoints = 1000000;
        this.currentHitpoints = this.maxHitPoints;

        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(Direction.UP, false);
        this.setAllowedVisualsToRotate(false);
    }

    @Override
    public void fireAction() {
        if(!this.finishedPostInit){
            initDirectionBasedOnSpawnLocation();
            initChargingAnimationConfig();
            this.finishedPostInit = true;
            this.canBuildNeedlers = true;
            lastTimeNeedlerBuild = GameState.getInstance().getGameSeconds();
        }

        if (this.movementConfiguration.getPathFinder() instanceof FloatingPathFinder && this.getMovementConfiguration().getCurrentPath() != null && this.getMovementConfiguration().getCurrentPath().getWaypoints().isEmpty()) {
            this.movementConfiguration.setCurrentPath(this.movementConfiguration.getPathFinder().findPath(this));
        }

        if (!hasCreatedDefenders) {
            createDefenders();
            hasCreatedDefenders = true;
        }

        if(startChargingLaserbeams && !isFiringLaserbeams){
            if(chargingAnimation.isPlaying()){
                this.chargingAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
                if(chargingAnimation.getCurrentFrame() == chargingAnimation.getTotalFrames() - 1){
                    isFiringLaserbeams = true;
                    startChargingLaserbeams = false;

                    primaryLaserbeam = createLaserbeam(primaryDirection.toAngle());
                    secondaryLaserbeam = createLaserbeam(secondaryDirection.toAngle());

                    updateLaserbeams();
                    MissileManager.getInstance().addLaserBeam(primaryLaserbeam);
                    MissileManager.getInstance().addLaserBeam(secondaryLaserbeam);
                }
            }
        }


        if(isFiringLaserbeams){
            this.updateLaserbeams();
        }

        if(!isFiringLaserbeams && primaryLaserbeam != null && primaryLaserbeam.isVisible() && secondaryLaserbeam != null && secondaryLaserbeam.isVisible()){
            primaryLaserbeam.setVisible(false);
            secondaryLaserbeam.setVisible(false);
        }


        if(canBuildNeedlers && lastTimeNeedlerBuild + (needlerBuildInterval + random.nextDouble(0, 2.0)) < GameState.getInstance().getGameSeconds()){
            playSpawnAnimation();
            spawnNeedler();
            lastTimeNeedlerBuild = GameState.getInstance().getGameSeconds();
        }
    }

    private void playSpawnAnimation(){
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setxCoordinate(this.getCenterXCoordinate());
        spriteConfiguration1.setyCoordinate(this.getCenterYCoordinate());
        spriteConfiguration1.setScale(1);
        spriteConfiguration1.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, false);
        SpriteAnimation spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        AnimationManager.getInstance().addUpperAnimation(spawnAnimation);
    }

    private void spawnNeedler(){
        Enemy needler = EnemyCreator.createEnemy(EnemyEnums.Needler, this.getCenterXCoordinate(), this.getCenterYCoordinate(), Direction.LEFT, EnemyEnums.Needler.getDefaultScale(), EnemyEnums.Needler.getMovementSpeed());
        needler.getMovementConfiguration().setPathFinder(new StraightLinePathFinder());
        needler.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());


        SpaceShip closestSpaceShip = PlayerManager.getInstance().getClosestSpaceShip(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        needler.getMovementConfiguration().setDestination(
                closestSpaceShip.getCurrentCenterLocation()
        );
        needler.setOwnerOrCreator(this);
        EnemyManager.getInstance().addEnemy(needler);
    }

    @Override
    public boolean isShowHealthBar() {
        return false;
    }


    private void createDefenders() {
        for (int i = 0; i < 2; i++) {
            BlueBossFactoryDefender blueBossFactoryDefender =
                    (BlueBossFactoryDefender) EnemyCreator.createEnemy(EnemyEnums.BlueBossFactoryDefender, this.getCenterXCoordinate(), this.getCenterYCoordinate(), Direction.LEFT,
                            EnemyEnums.BlueBossFactoryDefender.getDefaultScale(), EnemyEnums.BlueBossFactoryDefender.getMovementSpeed());

            blueBossFactoryDefender.setPathFinder(new OrbitPathFinder(this));
            blueBossFactoryDefender.getMovementConfiguration().setOrbitRadius(100);
            blueBossFactoryDefender.setOwnerOrCreator(this);
            blueBossFactoryDefender.getMovementConfiguration().setLastKnownTargetX(this.getCenterXCoordinate());
            blueBossFactoryDefender.getMovementConfiguration().setLastKnownTargetY(this.getCenterYCoordinate());
            blueBossFactoryDefender.setMaxHitPoints(100000000);
            blueBossFactoryDefender.setCurrentHitpoints(blueBossFactoryDefender.getMaxHitPoints());
            this.objectOrbitingThis.add(blueBossFactoryDefender);
            EnemyManager.getInstance().addEnemy(blueBossFactoryDefender);
        }
        OrbitingObjectsFormatter.reformatOrbitingObjects(this, 100);
    }

    public void fireLaserbeam() {
        this.startChargingLaserbeams = true;
        this.isFiringLaserbeams = false;

        this.chargingAnimation.refreshAnimation();
        this.chargingAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.canBuildNeedlers = false;
        AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
    }

    public void stopFiringLaserbeams(){
        this.isFiringLaserbeams = false;
        this.canBuildNeedlers = true;
    }

    private Laserbeam createLaserbeam(double angleDegrees) {
        float damage = this.getDamage() * BlueBossFireLaserbeams.damageRatio;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(BlueBossFireLaserbeams.laserbeamBodySegmentLength + 5);
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                this.getCenterXCoordinate(),
                this.getCenterYCoordinate()
        ));
        Laserbeam laserbeam = new AngledLaserBeam(upperLaserbeamConfiguration);
        laserbeam.setAngleDegrees(angleDegrees);
        laserbeam.setOwner(this);
        updateLaserbeamOriginPoints(laserbeam);
        laserbeam.update(); //force an update to snap it to the angleDegrees before adding it to the game
        return laserbeam;
    }

    private void updateLaserbeams(){
        float stepSize = BlueBossFireLaserbeams.laserBeamAngleStepSize;
        updateLaserbeamOriginPoints(primaryLaserbeam);
        updateLaserbeamOriginPoints(secondaryLaserbeam);

        switch(this.spawnPosition){
            case TopLeft -> {
                primaryLaserbeam.setAngleDegrees(primaryLaserbeam.getAngleDegrees() - stepSize);
                secondaryLaserbeam.setAngleDegrees(secondaryLaserbeam.getAngleDegrees() + stepSize);
            }
            case TopRight -> {
                primaryLaserbeam.setAngleDegrees(primaryLaserbeam.getAngleDegrees() - stepSize);
                secondaryLaserbeam.setAngleDegrees(secondaryLaserbeam.getAngleDegrees() + stepSize);
            }
            case BottomLeft -> {
                primaryLaserbeam.setAngleDegrees(primaryLaserbeam.getAngleDegrees() + stepSize);
                secondaryLaserbeam.setAngleDegrees(secondaryLaserbeam.getAngleDegrees() - stepSize);
            }
            case BottomRight -> {
                primaryLaserbeam.setAngleDegrees(primaryLaserbeam.getAngleDegrees() + stepSize);
                secondaryLaserbeam.setAngleDegrees(secondaryLaserbeam.getAngleDegrees() - stepSize);
            }
        }

        if(this.primaryLaserbeam.getAngleDegrees() >= 360){
            this.primaryLaserbeam.setAngleDegrees(this.primaryLaserbeam.getAngleDegrees() - 360);
        }
        if(this.secondaryLaserbeam.getAngleDegrees() >= 360){
            this.secondaryLaserbeam.setAngleDegrees(this.secondaryLaserbeam.getAngleDegrees() - 360);
        }
    }

    private void updateLaserbeamOriginPoints(Laserbeam laserbeam) {
        laserbeam.setOriginPoint(new Point(
                this.getCenterXCoordinate() - Laserbeam.bodyWidth / 2,
                this.getCenterYCoordinate() - Laserbeam.bodyWidth / 2
        ));
    }

    public void initDirectionBasedOnSpawnLocation(){
        int yCoordinate = this.getCenterYCoordinate();
        int xCoordinate = this.getCenterXCoordinate();
        float gameHalfHeight = DataClass.getInstance().getPlayableWindowMaxHeight() * 0.5f;
        float gameHalfWidth = DataClass.getInstance().getWindowWidth()* 0.5f;

        if(xCoordinate < gameHalfWidth && this.yCoordinate < gameHalfHeight){
            //Upper left corner
            primaryDirection = Direction.LEFT_DOWN;
            secondaryDirection = Direction.RIGHT_UP;
            this.spawnPosition = BlueBossFactorySpawnPosition.TopLeft;
        }

        if(xCoordinate < gameHalfWidth && yCoordinate > gameHalfHeight){
            //Lower left corner
            primaryDirection = Direction.LEFT_UP;
            secondaryDirection = Direction.RIGHT_DOWN;
            this.spawnPosition = BlueBossFactorySpawnPosition.BottomLeft;
        }

        if(xCoordinate > gameHalfWidth && yCoordinate < gameHalfHeight){
            //Upper Right corner
            primaryDirection = Direction.LEFT_UP;
            secondaryDirection = Direction.RIGHT_DOWN;
            this.spawnPosition = BlueBossFactorySpawnPosition.TopRight;
        }

        if(xCoordinate > gameHalfWidth && yCoordinate > gameHalfHeight){
            //Lower Right corner
            primaryDirection = Direction.LEFT_DOWN;
            secondaryDirection = Direction.RIGHT_UP;
            this.spawnPosition = BlueBossFactorySpawnPosition.BottomRight;
        }
    }

    private void initChargingAnimationConfig() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        spriteConfiguration.setScale(3);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        chargingAnimation = new SpriteAnimation(new SpriteAnimationConfiguration(spriteConfiguration, 1, false));
        chargingAnimation.setAnimationScale(3f);
        chargingAnimation.setFrameDelay(10);
        chargingAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        chargingAnimation.addYOffset(-15);
    }

    private enum BlueBossFactorySpawnPosition {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight;
    }
}
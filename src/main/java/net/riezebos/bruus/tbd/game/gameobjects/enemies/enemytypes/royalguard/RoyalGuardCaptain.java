package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamIndicator;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.HoverPathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class RoyalGuardCaptain extends Enemy {

    /*
        Idealiter een "sniper" rol, maar weet niet hoe dit te implementeren
        Idee:
            Gebruik een laserbeam die door het hele scherm gaat voor 0.5 seconden.
            Gebruik een chargeLaserbeam animatie maar GEEN audio effect
            Belangrijk: deze aanval moet telegraphed worden door een lijn te tekenen die de laserbeam gaat zijn. Hier kan simpelweg g.drawLine voor gebruikt worden
                - De lijn moet dikker en feller van kleur worden wanneer de laserbeam bijna afgaat
                - De lijn/laserbeam mag maar een X aantal angleDegrees per stap veranderen zodat de speler de laserbeam kan ontwijken (denk aan de carrierboss laser)
                -
     */

    private LaserbeamIndicator laserbeamIndicator;
    private AngledLaserBeam laserbeam;
    private GameObject target = null;
    private float angleDegreeIncrement = 0.11f;
    private boolean isLiningUp = false;

    public RoyalGuardCaptain(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.attackSpeed = 2.5f - (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.1f);
        this.knockbackStrength = 8;
        this.allowedVisualsToRotate = false;
        angleDegreeIncrement += (EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.0075f);
        this.chargingUpAttackAnimation.changeImagetype(ImageEnums.PinkLaserbeamCharging);
        this.chargingUpAttackAnimation.setFrameDelay(4);
    }

    @Override
    public void fireAction() {
        if (this.movementConfiguration.getPathFinder() instanceof HoverPathFinder pathfinder) {
            allowedToFire = this.movementConfiguration.getCurrentPath().getWaypoints().isEmpty();
            pathfinder.setSecondsToHoverStill(3);
        }
        double currentTime = GameState.getInstance().getGameSeconds();

        // als de attack cooldown weg is, init de laserbeam indicator en start de chargeUp animatie
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
            super.allowedVisualsToRotate = true;
            this.rotateGameObjectTowards(this.getMovementConfiguration().getRotation(), true);
            super.allowedVisualsToRotate = false;
            target = PlayerManager.getInstance().getClosestSpaceShip(this);
            updateChargingAttackAnimationCoordination();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                this.isLiningUp = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }
        }

        if(this.isLiningUp && this.laserbeamIndicator == null){
            laserbeamIndicator = new LaserbeamIndicator(chargingUpAttackAnimation.getCenterXCoordinate(), chargingUpAttackAnimation.getCenterYCoordinate(), target.getCenterXCoordinate(), target.getCenterYCoordinate(), angleDegreeIncrement, this);
            laserbeamIndicator.setLength(laserBeamSegments * Laserbeam.bodyWidth);
            MissileManager.getInstance().addLaserbeamIndicator(laserbeamIndicator);
        }

        // als de laserbeam indicator niet null is, update hem (mik naar de speler) met een max increase/decrease in angleDegrees
        if(this.isLiningUp && this.laserbeamIndicator != null){
            updateChargingAttackAnimationCoordination();
            laserbeamIndicator.setStartingXCoordinate(chargingUpAttackAnimation.getCenterXCoordinate());
            laserbeamIndicator.setStartingYCoordinate(chargingUpAttackAnimation.getCenterYCoordinate());
            this.laserbeamIndicator.targetTowardsCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        }

        // Als de chargeUp klaar is met chargen: maak een laserbeam aan
        if (this.isLiningUp && chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
            fireLaserBeam();
            this.laserbeamIndicator.setActive(false);
            this.isLiningUp = false;
        }


        // Als de laserbeam doorzichtig genoeg is, reset de aanval (laserbeam moet invisible worden, en laserbeam/laserbeamindicator moeten op null gezet worden) en zet de lastAttackedTime op de huidige tijd
        if(this.isAttacking && this.laserbeam != null && !this.laserbeam.isVisible()){
            this.laserbeam.setVisible(false);
            this.laserbeam = null;
            this.laserbeamIndicator = null;
            this.isAttacking = false;
            lastAttackTime = currentTime; // Update the last attack time after firing
        }
    }

    @Override
    public boolean isAllowedToMove() {
        if(this.isLiningUp || this.isAttacking){
            return false;
        }
        return allowedToMove;
    }

    private int laserBeamSegments = Math.max(15, Math.round(15 * (DataClass.getInstance().getResolutionFactor() * 0.5f))); //minimum of 15, but increasingly more depending on resolution scale
    private void fireLaserBeam() {
        LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(false, this.getDamage());
        laserbeamConfiguration.setAmountOfLaserbeamSegments(laserBeamSegments);
        laserbeamConfiguration.setOriginPoint(new Point(
                chargingUpAttackAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth,
                chargingUpAttackAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 14
        ));

        AngledLaserBeam laserBeam = new AngledLaserBeam(laserbeamConfiguration);
        laserBeam.setTransparancyAlpha(true, 1, -0.07f);
        laserBeam.setAngleDegrees(this.laserbeamIndicator.getCurrentAngleDegrees());
        laserBeam.setOwner(this);
//        laserBeam.setOriginPoint(new Point(chargingUpAttackAnimation.getCenterXCoordinate() - Laserbeam.getXOffsetForCentering(), chargingUpAttackAnimation.getCenterYCoordinate() - (Laserbeam.getYOffsetForCentering() / 2)));
        laserBeam.update(); //force update it to prevent the jumping
//        laserBeam.setOriginPoint(new Point(chargingUpAttackAnimation.getCenterXCoordinate() - Laserbeam.getXOffsetForCentering(), chargingUpAttackAnimation.getCenterYCoordinate() - (Laserbeam.getYOffsetForCentering() / 2)));
        this.laserbeam = laserBeam;
        MissileManager.getInstance().addLaserBeam(laserBeam);
    }
}
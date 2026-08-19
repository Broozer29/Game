package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FrontShield;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class RoyalGuardShieldbearer extends Enemy {

    /*
        Een frontliner die in de weg zit. Wanneer er een speler in de detectie range is, start een frontShield
        De frontShield vernietigd kogels, doet damage en duwt de speler weg.
        Frontshield gaat weg als er geen speler in de detectie range is
        Beweegt in een rechte lijn, gaat uit het scherm
     */

    private FrontShield frontShield = null;
    private int detectionRange = 0;
    private GameObject target = null;

    public RoyalGuardShieldbearer(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.attackSpeed = 0.1f;
        this.detectionRange = 185 + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 10);
        this.baseArmor += (EnemyManager.getInstance().getEnemyDifficultyModifier() * 5);
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.175f);
        this.knockbackStrength = 8;
    }

    @Override
    public void triggerOnDeathActions(){
        if(this.frontShield != null){
            this.frontShield.startDissipating();
        }
        super.triggerOnDeathActions();
    }

    @Override
    public void fireAction() {
        // Check if the attack cooldown has been reached
        double currentTime = GameState.getInstance().getGameSeconds();
        if (WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire
                && currentTime >= lastAttackTime + this.getAttackSpeed()
                && (frontShield == null || frontShield.isCompletelyDissipated()) //if there is no frontshield or if its dissipated
                && CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), detectionRange)) {
            this.target = PlayerManager.getInstance().getClosestSpaceShip(this);
            initFrontShield();
        }

        //If the shield exists but no players are close, start dissipating it
        if(frontShield != null && frontShield.isVisible()){
            frontShield.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());


            //rotating disabled for now, should only rotate left or right at Direction.RIGHT.toAngleDegrees()
//            if(target != null){
//                frontShield.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), false);
//                if(!target.isVisible() || target.getCurrentHitpoints() <= 0){
//                    this.target = null;
//                    frontShield.startDissipating();
//                    lastAttackTime = currentTime; //set cooldown once it starts dissipating
//                }
//            }

            if(!frontShield.isDissipating() && !CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), Math.round(detectionRange * 1.4f))){ // 25% bonus range zodat hij niet meteen weer stopt
                frontShield.startDissipating();
                lastAttackTime = currentTime; //set cooldown once it starts dissipating
            }
        }

        if(frontShield != null && !frontShield.isVisible()){
            frontShield = null; //reset it
        }
    }


    private void initFrontShield() {
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setxCoordinate(this.getCenterXCoordinate());
        spriteConfiguration1.setyCoordinate(this.getCenterYCoordinate());
        spriteConfiguration1.setScale(0.75f);
        spriteConfiguration1.setImageType(ImageEnums.FrontShield);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, true);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.damage, this.isFriendly(), true, false, true, true, false);
        frontShield = new FrontShield(spriteAnimationConfiguration, specialAttackConfiguration);
        frontShield.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        frontShield.rotateObjectTowardsAngle(this.rotationAngle, false);
//        frontShield.addXOffset(this.movementRotation.equals(Direction.RIGHT) ? 40 : -40); //assume this guy only goes left/right in a straight line, otherwise this offset breaks if he moves diagonally
        frontShield.setOwnerOrCreator(this);
        frontShield.setTransparancyAlpha(true, 0.05f, 0.035f);
        MissileManager.getInstance().addSpecialAttack(frontShield);
    }
}
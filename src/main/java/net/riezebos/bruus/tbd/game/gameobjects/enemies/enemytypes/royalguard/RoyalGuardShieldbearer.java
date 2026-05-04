package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.royalguard;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
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
    private int detectionRange = 165;

    public RoyalGuardShieldbearer(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.damage = 10;
        this.attackSpeed = 4;
        this.knockbackStrength = 8;
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

            initFrontShield();
        }

        //If the shield exists but no players are close, start dissipating it
        if(frontShield != null && frontShield.isVisible()){
            frontShield.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            if(!frontShield.isDissipating() && !CollisionDetector.getInstance().isNearby(this, PlayerManager.getInstance().getClosestSpaceShip(this), Math.round(detectionRange * 1.25f))){ // 25% bonus range zodat hij niet meteen weer stopt
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
        spriteConfiguration1.setScale(1); //todo zet een fatsoenlijk schaal, kan niet testen op werk
        spriteConfiguration1.setImageType(ImageEnums.FrontShield);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, true);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(this.damage, this.isFriendly(), true, false, true, true, false);
        frontShield = new FrontShield(spriteAnimationConfiguration, specialAttackConfiguration);
        frontShield.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        //todo apply een offset zodat het visueel goed eruit ziet, kan niet testen op werk
        frontShield.setOwnerOrCreator(this);
        frontShield.setTransparancyAlpha(true, 0.05f, 0.035f);
        MissileManager.getInstance().addSpecialAttack(frontShield);
    }
}
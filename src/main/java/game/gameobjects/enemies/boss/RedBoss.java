package game.gameobjects.enemies.boss;

import VisualAndAudioData.image.ImageEnums;
import VisualAndAudioData.image.ImageRotator;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.util.OutOfBoundsCalculator;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RedBoss extends Enemy {

    private List<BossActionable> bossBehaviourList = new ArrayList<>();

    public RedBoss (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Explosion2);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 10;
        this.allowedVisualsToRotate = false;

//        BurstMainAttackBossBehaviour bossBehaviour = new BurstMainAttackBossBehaviour();
//        bossBehaviourList.add(bossBehaviour);

        SpawnFourDirectionalDrone bossBehaviour = new SpawnFourDirectionalDrone();
        bossBehaviourList.add(bossBehaviour);

        this.chargingUpAttackAnimation.setAnimationScale(1.5f);
        this.chargingUpAttackAnimation.setFrameDelay(3);
    }



    protected void updateChargingAttackAnimationCoordination () {
        //Overwritten because bosses should only have 1!!! orientation, "LEFT" so we can do this manually and better. The dynamic distance calculation
        //Does not work with this bosses artwork
        if (this.chargingUpAttackAnimation != null) {
            this.chargingUpAttackAnimation.setCenterCoordinates(this.getXCoordinate() - (chargingUpAttackAnimation.getWidth() / 2), this.getCenterYCoordinate());
        }
    }

    @Override
    public void fireAction () {
        if (WithinVisualBoundariesCalculator.isWithinBoundaries(this)) {
            this.allowedToFire = true; //Bosses should ALWAYS be allowed to fire, this check allows it to be immune to CC from the player
        }

        for (BossActionable bossActionable : bossBehaviourList) {
            bossActionable.activateBehaviour(this);
        }

    }

}

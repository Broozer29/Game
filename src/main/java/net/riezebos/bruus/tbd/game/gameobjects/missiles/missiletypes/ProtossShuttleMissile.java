package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ProtossShuttleMissile extends Missile {

    private static float explosionSize = 3f;

    public ProtossShuttleMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.setFrameDelay(0);
        if(missileConfiguration.getDestructionType() != null){
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
            this.destructionAnimation.setAnimationScale(0.75f);
        }
        this.isDamageable = false;
        this.isDestructable = true;
    }

    public void missileAction() {
    }

    public void detonateMissile(){
        createExplosion();
    }

    private void createExplosion(){
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setxCoordinate(this.xCoordinate);
        spriteConfiguration1.setyCoordinate(this.yCoordinate);
        spriteConfiguration1.setScale(explosionSize);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 1, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.ProtossShuttleMissileExplosion);

        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(this.isFriendly(), damage, true, false);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setOwnerOrCreator(this.ownerOrCreator);
        explosion.setScale(explosionSize);
        explosion.setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
        ExplosionManager.getInstance().addExplosion(explosion);

        this.setVisible(false);
    }

}
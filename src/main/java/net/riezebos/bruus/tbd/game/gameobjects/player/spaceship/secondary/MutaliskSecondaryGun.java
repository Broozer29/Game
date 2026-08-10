package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.secondary;

import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SecondaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MutaliskSecondaryGun extends SecondaryPlayerGun {

    private SpriteAnimation chargingUpAnimation;
    public static float healthPercentagePerTick = 0.015f;
    public static float damagePerTickBonus = 0.15f;
    public static float damageModifier = 1.5f;
    public int ticksCounted = 0;


    public MutaliskSecondaryGun() {
        initBlankChargeUpAnim();
    }

    private void initBlankChargeUpAnim() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(-500);
        spriteConfiguration.setyCoordinate(-500);
        spriteConfiguration.setScale(0.75f);
        spriteConfiguration.setImageType(ImageEnums.MutaliskChargeUp);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
        this.chargingUpAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }

        healthPercentagePerTick = 0.012f;
        damagePerTickBonus = 0.175f;

        if (specialAttackCharges > 0) {
            handleMutaliskSpecialAttack(owner);
        }

    }

    @Override
    public void stopFiring(SpaceShip owner){
        if(ticksCounted >= 1){
            createExplosion(owner);
            spendCharge(owner);
        }
    }

    @Override
    public void updateFrameCount(SpaceShip owner) {
        super.updateFrameCount(owner);
        if(this.chargingUpAnimation.isVisible() && ticksCounted >= 1){
            this.chargingUpAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate());
        }
    }

    private void createExplosion(SpaceShip owner) {
        float damage = (owner.getSpecialAttackDamage() * damageModifier) * (1 + damagePerTickBonus * ticksCounted);

        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(owner.isFriendly(), damage, true);
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getCenterYCoordinate());
        spriteConfiguration.setScale(2.75f);
        spriteConfiguration.setImageType(ImageEnums.MutaliskExplosion);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, false);

        //todo verander explosie in een specialattack met intens hoge tickCd zodat het een explosie simuleert maar missiles kans vernietigen
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate());

        chargingUpAnimation.setVisible(false);
        ticksCounted = 0;
        ExplosionManager.getInstance().addExplosion(explosion);
    }

    private void handleMutaliskSpecialAttack(SpaceShip owner) {
        if (!owner.isVisible() || owner.getCurrentHitpoints() <= 0){
            stopFiring(owner);
            return;
        }


        //if the owner would DIE from this tick, release it instead
        if(owner.getCurrentHitpoints() - (owner.getMaxHitPoints() * healthPercentagePerTick) <= 1){
            stopFiring(owner);
            return;
        }

        if(!this.chargingUpAnimation.isVisible() || !this.chargingUpAnimation.isPlaying()){
            chargingUpAnimation.refreshAnimation();
        }

        if(!AnimationManager.getInstance().getUpperAnimations().contains(chargingUpAnimation)){
            AnimationManager.getInstance().addUpperAnimation(chargingUpAnimation);
        }

        chargingUpAnimation.setCenterCoordinates(owner.getCenterXCoordinate(), owner.getCenterYCoordinate());
        owner.setCurrentHitpoints(owner.getCurrentHitpoints() - (owner.getMaxHitPoints() * healthPercentagePerTick));
        ticksCounted += 1;
    }

}

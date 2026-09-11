package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FinalBossTrackingMissileGrenade extends Missile {

    private int amountOfAnimationCyclesBeforeExplosion = 0;

    public FinalBossTrackingMissileGrenade(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, missileConfiguration, movementConfiguration);
        this.animation.setFrameDelay(5);
        this.isDamageable = false;
        this.isDestructable = true;

        if(missileConfiguration.getDestructionType() != null){
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
            this.destructionAnimation.setAnimationScale(0.75f);
        }
    }

    public void missileAction() {
        if(this.animation.getFrameDelay() == 0){
            //Prepare the missile for explosion
            if(this.animation.getCurrentFrame() == this.animation.getTotalFrames()){
                amountOfAnimationCyclesBeforeExplosion++;
                this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getMovementSpeed() * 0.9f);
            }

            //Explode the missile
            if(amountOfAnimationCyclesBeforeExplosion > 4) {
                this.detonateMissile();
            }
        }


        //Speed up the animation after travelling a distance
        if(movementConfiguration.getStepsTaken() % 25 == 0){
            int newFrameDelay = Math.max(0, this.animation.getFrameDelay() - 1);
            //Slow the missile down if the animation isn't at max speed yet
            if(newFrameDelay > 0){
                this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getMovementSpeed() * 0.8f);
            }
            this.animation.setFrameDelay(newFrameDelay);
        }

    }

    public void detonateMissile(){
        createMissiles();
    }

    private void createMissiles(){

    }
}
package game.gameobjects.enemies.boss.behaviour;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.missiles.laserbeams.Laserbeam;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class CrossingLaserbeamsAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 20;
    private int priority = 3;

    private SpriteAnimation upperChargingUpAnimation;
    private SpriteAnimation lowerChargingUpAnimation;
    private Laserbeam upperLaserbeam;
    private Laserbeam lowerLaserbeam;
    private boolean isFiringLaserbeams;
    private float angleStep;
    private float amountOfAnglesIncremented;
    private boolean inwards;

    public CrossingLaserbeamsAttack(boolean inwards){
        //Determines wether the laserbeams go from outside to the center if true, if false the laserbeams should go inwards to outwards
        this.inwards = inwards;
    }

    @Override
    //Return if the behaviour is completed.
    // if returns true; this behaviour is removed and another is selected
    // if false, this behaviour remains and this method keeps getting called
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (upperChargingUpAnimation == null || lowerChargingUpAnimation == null) {
            initSpawnAnimations(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAnimationLocations(enemy);

            if (!lowerChargingUpAnimation.isPlaying()) {
                upperChargingUpAnimation.refreshAnimation();
                lowerChargingUpAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(upperChargingUpAnimation);
                AnimationManager.getInstance().addUpperAnimation(lowerChargingUpAnimation);
            }


            if (lowerChargingUpAnimation.isPlaying() && lowerChargingUpAnimation.getCurrentFrame() == 4) {
                //Create the laserbeams
                //
                isFiringLaserbeams = true;
            }
            return false; //We are charging up the laser or have created them
        }


        /*
        if(isFiringLaserbeams){
            //Adjust the laserbeams angle a little
            laserbeams.setAngleDegrees(getAngleDegrees - angleStep or + angleStep depending on upper or lower)
            amountOfAnglesIncremented += anglestep
            //if(amountOfAnglesIncremented > 100){
                deleteLaserbeams() //so we can remake them
                isFiringLaserbeams = false //reset it for next time
                amountOfAnglesIncremented = 0; //reset it for next time
                enemy.setAttacking(false);
            }
            else {
                return false //because we are still busy firing laserbeams
            }
        }
         */

        return true; //Laserbeams should removed and this attack is finished

    }

    private void deleteLaserbeams(){
        if(upperLaserbeam != null){
            upperLaserbeam.setVisible(false);
        }

        if(lowerLaserbeam != null){
            lowerLaserbeam.setVisible(false);
        }
    }


    private void initSpawnAnimations(Enemy enemy){
        upperChargingUpAnimation = new SpriteAnimation(createChargingAnimationConfig(enemy));
        upperChargingUpAnimation.setAnimationScale(0.4f);
        upperChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate(),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.4f)
        );

        lowerChargingUpAnimation = new SpriteAnimation(createChargingAnimationConfig(enemy));
        lowerChargingUpAnimation.setAnimationScale(0.4f);
        lowerChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate(),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.8f)
        );
    }

    private SpriteAnimationConfiguration createChargingAnimationConfig(Enemy enemy){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.Charging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void updateAnimationLocations (Enemy enemy) {
        upperChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate(),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.4f)
        );

        lowerChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate(),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.8f)
        );

    }

    @Override
    public int getPriority () {
        return priority;
    }
}

package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior;


import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

/*
    This method is intended to provide a template for a boss attack with basic implementation but leaving the actual action empty
 */
public class BossAttackTemplateForDev implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 10;
    private int priority = 2;

    private SpriteAnimation attackingAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (attackingAnimation == null) {
            initAttackAnimation(enemy);
        }


        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAttackAnimationLocation(enemy);
            if (!attackingAnimation.isPlaying()) {
                attackingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(attackingAnimation);
            }


            if (attackingAnimation.isPlaying() && attackingAnimation.getCurrentFrame() == attackingAnimation.getTotalFrames() - 1) {
                doTheThing(enemy);
                lastAttackedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }
            return false; //We still running this behaviour
        }
        return true; //We still running this behaviour

    }


    private void initAttackAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn); //vervangen met de daadwerkelijke anim

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        attackingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        attackingAnimation.setAnimationScale(0.5f);
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        attackingAnimation.addXOffset(Math.round(attackingAnimation.getWidth() * 0.1f));
    }

    private void updateAttackAnimationLocation(Enemy enemy) {
        attackingAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private void doTheThing(Enemy enemy) {

    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == 1
                    && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

}

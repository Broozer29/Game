package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

import java.util.Random;

public class ZergCocoon extends Enemy {

    private EnemyEnums zergToSpawn = null;
    private boolean isSpawning = false;

    public ZergCocoon (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.damage = 0;
        this.attackSpeed = 500;
        this.detonateOnCollision = false;
        this.knockbackStrength = 7;
        this.currentHitpoints = maxHitPoints * 0.1f; //start at 10% hp
        this.showHealthBar = true;
        this.allowedToMove = false;

        this.zergToSpawn = selectZergToSpawn();
    }

    private EnemyEnums selectZergToSpawn () {
        if (this.animation.getImageEnum().equals(ImageEnums.DevourerCocoon)) {
            return EnemyEnums.ZergDevourer;
        }
        random = new Random();
        int randomNumber = random.nextInt(0, 2);
        if (randomNumber == 1) {
            return EnemyEnums.ZergGuardian;
        }
        return EnemyEnums.ZergGuardian;
    }


    @Override
    public void fireAction () {
        if (currentHitpoints < maxHitPoints) {
            currentHitpoints += (maxHitPoints * 0.0025f);
        }


        if (currentHitpoints >= maxHitPoints && !isSpawning) {
            startBirthAnimation();
            this.animation.setFrameDelay(6);
            this.animation.setCurrentFrame(0);
            isSpawning = true;
            this.setBaseArmor(999); //Practically immune, but not entirely
        }

        if (isSpawningAnimationFinished()) {
            spawnZerg();
            this.deleteObject();
        }
    }


    private void spawnZerg () {
        Enemy zerg = EnemyCreator.createEnemy(this.zergToSpawn, this.xCoordinate, this.yCoordinate,
                this.movementRotation, this.scale, this.zergToSpawn.getMovementSpeed(), this.zergToSpawn.getMovementSpeed(),
                MovementPatternSize.SMALL, false);
        zerg.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        EnemyManager.getInstance().addEnemy(zerg);
    }

    private void startBirthAnimation () {
        switch (this.zergToSpawn) {
            case ZergDevourer:
                this.animation.changeImagetype(ImageEnums.DevourerBirth);
                break;
            case ZergGuardian:
                this.animation.changeImagetype(ImageEnums.GuardianBirth);
                break;
            case ZergMutalisk:
                this.animation.changeImagetype(ImageEnums.MutaliskBirth);
                break;
        }
    }

    private boolean isSpawningAnimationFinished () {
        if (this.animation.getImageEnum().equals(ImageEnums.DevourerBirth) || this.animation.getImageEnum().equals(ImageEnums.MutaliskBirth)
                || this.animation.getImageEnum().equals(ImageEnums.GuardianBirth)) {
            return this.animation.getCurrentFrame() >= this.animation.getTotalFrames();
        }
        return false;
    }


}

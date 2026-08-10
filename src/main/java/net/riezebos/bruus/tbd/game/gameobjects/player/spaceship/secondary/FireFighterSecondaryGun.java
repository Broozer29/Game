package net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.secondary;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.FireShield;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerSpecialAttackTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SecondaryPlayerGun;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FireFighterSecondaryGun extends SecondaryPlayerGun {

    @Override
    public void fire(int xCoordinate, int yCoordinate, PlayerSpecialAttackTypes attackType, SpaceShip owner) {
        if (!owner.isAllowedToAttack()) {
            return;
        }
        if (attackOffCooldown(owner)) {
            fireFlameShield(xCoordinate, yCoordinate, owner);
            spendCharge(owner);
        }
    }

    private void fireFlameShield(int xCoordinate, int yCoordinate, SpaceShip owner) {
        float damage = owner.getSpecialAttackDamage();
        SpriteAnimationConfiguration spriteAnimationConfiguration = createConfig(xCoordinate, yCoordinate, ImageEnums.FireFighterFireShieldAppearing, 1, true);
        SpecialAttackConfiguration missileConfiguration = new SpecialAttackConfiguration(damage, true, true, false, true, true, true);
        SpecialAttack specialAttack = new FireShield(spriteAnimationConfiguration, missileConfiguration);
        specialAttack.setCenteredAroundObject(true);
        specialAttack.setScale(1.1f);
        specialAttack.setOwnerOrCreator(owner);
        AudioManager.getInstance().addAudio(AudioEnums.Firewall);
        owner.addFollowingSpecialAttack(specialAttack);
        MissileManager.getInstance().addSpecialAttack(specialAttack);
    }
}

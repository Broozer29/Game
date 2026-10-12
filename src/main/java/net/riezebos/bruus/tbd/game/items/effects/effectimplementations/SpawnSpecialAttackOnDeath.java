package net.riezebos.bruus.tbd.game.items.effects.effectimplementations;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.LingeringAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.specialAttacks.SpecialAttackConfiguration;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.EffectIdentifiers;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.List;
import java.util.Random;

public class SpawnSpecialAttackOnDeath implements EffectInterface {

    private static Random random = new Random();
    private EffectIdentifiers effectIdentifier;
    private EffectActivationTypes effectActivationType;
    private ImageEnums specialAttackImage;
    private boolean hasActivated = false;

    public SpawnSpecialAttackOnDeath(EffectActivationTypes effectActivationType, EffectIdentifiers effectIdentifier,
                                     ImageEnums missileImage) {
        this.effectActivationType = effectActivationType;
        this.effectIdentifier = effectIdentifier;
        this.specialAttackImage = missileImage;
    }

    @Override
    public void activateEffect(GameObject gameObject) {
        if (gameObject.getCurrentHitpoints() <= 0 && !hasActivated) {
            hasActivated = true;
            MissileManager.getInstance().addSpecialAttack(createSpecialAttack(gameObject));
        }
    }

    private SpecialAttack createSpecialAttack(GameObject gameObject) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(gameObject.getXCoordinate());
        spriteConfiguration.setyCoordinate(gameObject.getYCoordinate());
        spriteConfiguration.setScale(0.55f);
        spriteConfiguration.setImageType(this.specialAttackImage);

        //todo een hoop van deze configuratie is hardcoded omdat het alleen gebruikt wordt door de mutalisk atm, moet generieker gemaakt worden als dit hergebruikt gaat worden
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        SpecialAttackConfiguration specialAttackConfiguration = new SpecialAttackConfiguration(gameObject.getDamage(), gameObject.isFriendly(), true, false, true, false, false);

        SpecialAttack specialAttack = new LingeringAttack(spriteAnimationConfiguration, specialAttackConfiguration);
        specialAttack.setCenterCoordinates(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
        specialAttack.setTransparancyAlpha(false, 0.8f, 0.0f);
        return specialAttack;
    }

    @Override
    public boolean shouldBeRemoved(GameObject gameObject) {
        return false;
    }

    @Override
    public List<SpriteAnimation> getAnimations() {
        return null;
    }

    @Override
    public EffectActivationTypes getEffectTypesEnums() {
        return this.effectActivationType;
    }

    @Override
    public void resetDuration() {
        //Not needed
    }

    @Override
    public void increaseEffectStrength(GameObject gameObject) {
//Not needed
    }

    @Override
    public EffectIdentifiers getEffectIdentifier() {
        return this.effectIdentifier;
    }

    @Override
    public void removeEffect(GameObject gameObject) {

    }

    @Override
    public EffectInterface copy() {
        return null;
    }

    public ImageEnums getSpecialAttackImage() {
        return specialAttackImage;
    }

    public void setSpecialAttackImage(ImageEnums specialAttackImage) {
        this.specialAttackImage = specialAttackImage;
    }
}

package net.riezebos.bruus.tbd.game.items.effects;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;

public interface EffectInterface {
    void activateEffect (GameObject gameObject);

    boolean shouldBeRemoved ();

    SpriteAnimation getAnimation ();

    EffectActivationTypes getEffectTypesEnums ();

    void resetDuration ();

    void increaseEffectStrength();
    EffectIdentifiers getEffectIdentifier();

    EffectInterface copy();
}

package net.riezebos.bruus.tbd.game.items.effects;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;

import java.util.List;

public interface EffectInterface {
    void activateEffect (GameObject gameObject);

    boolean shouldBeRemoved (GameObject gameObject);

    List<SpriteAnimation> getAnimations();

    EffectActivationTypes getEffectTypesEnums ();

    void resetDuration ();

    void increaseEffectStrength(GameObject gameObject);
    EffectIdentifiers getEffectIdentifier();
    void removeEffect (GameObject gameObject);

    EffectInterface copy();
}

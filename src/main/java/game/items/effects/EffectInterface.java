package game.items.effects;

import game.gameobjects.GameObject;
import visualobjects.SpriteAnimation;

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

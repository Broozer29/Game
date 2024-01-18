package game.items;

import game.objects.GameObject;

public interface ItemInterface {
    void applyEffectToObject (GameObject gameObject);
    void triggerEffectForOneTimeEffects (GameObject gameObject); //Might not be needed
    void modifyAttackValues (GameObject attack, GameObject target);

}

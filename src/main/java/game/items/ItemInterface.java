package game.items;

import game.objects.GameObject;

public interface ItemInterface {
    void applyEffectToObject (GameObject gameObject);
    void modifyAttackValues (GameObject attack, GameObject target);



}

package game.items;

import game.gameobjects.GameObject;

public interface ItemInterface {
    void applyEffectToObject (GameObject gameObject);
    void modifyAttackValues (GameObject attack, GameObject target);



}

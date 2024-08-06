package game.items;

import game.gameobjects.GameObject;

public interface ItemInterface {
    void applyEffectToObject (GameObject target);
    void applyEffectToObject (GameObject applier, GameObject target);



}

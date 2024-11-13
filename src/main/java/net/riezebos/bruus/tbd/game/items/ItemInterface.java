package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;

public interface ItemInterface {
    void applyEffectToObject (GameObject target);
    void applyEffectToObject(GameObject applier, GameObject target);
    void modifyAttackingObject (GameObject applier, GameObject target);



}

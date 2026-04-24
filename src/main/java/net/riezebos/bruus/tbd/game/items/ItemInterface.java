package net.riezebos.bruus.tbd.game.items;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;

public interface ItemInterface {
    void applyEffectToObject (GameObject target);
    void applyEffectToObject(GameObject applier, GameObject target);
    void applyEffectToObject(GameObject applier, GameObject target, CollisionInfo collisionInfo);
    void modifyAttackingObject (GameObject applier, GameObject target);
    boolean isAvailable();



}

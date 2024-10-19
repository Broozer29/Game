package net.riezebos.bruus.tbd.game.items.effects;

public enum EffectActivationTypes {
    CheckEveryGameTick, //Something that gets checked every gametick wether it should fire
    OnObjectDeath, //Fires AFTER the object has reached <0 hitpoints

}

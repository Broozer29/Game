package game.items.effects;

public enum EffectActivationTypes {
    DamageModification,  //See crowbar or smth
    AfterAHit, //Activates after hitting something, ATG missile from Ror2
    PlayerStatsModificationOnCreation, //Modifies something in the PlayerStats instance
    CheckEveryGameTick, //Something that gets checked every gametick wether it should fire
    OnPlayerHit, // After the player is hit by something (not by taking damage!)
    OnObjectDeath,

}

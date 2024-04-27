package game.items.effects;

public enum EffectActivationTypes {
    DamageOverTime, // A dot, self explanatory
    HealthRegeneration, // Healing every tick
    DormentExplosion, // A single-fire event, possibly rename it
    DamageModification,  //See crowbar or smth
    AfterAHit, //Activates after hitting something, ATG missile from Ror2
    PlayerStatsModification, //Modifies something in the PlayerStats instance
    OutOfCombatArmorBonus,
    CheckEveryGameTick, //Something that gets checked every gametick wether it should fire
    OnDeath, // After a gameobject dies
    OnPlayerHit, // After the player is hit by something (not by taking damage!)
    Debuff,

}

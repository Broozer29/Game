package net.riezebos.bruus.tbd.game.gamestate;

public enum GameMode {
    Default("Default", "The default game mode."),
    ManMode("Man Mode", "Every level is a boss level."), //There are only boss levels
    MonoCultural("Monocultural", "Only 1 enemy type spawns per stage"), //Only 1 enemy type spawns per stage
    Formatted("Formatted", "Enemies spawn and stay in formations."),
    Nightmare("Nightmare", "After the first level, the god run detector becomes maxed out at all times. Bosses are stronger."),
    DoubleTrouble("Double Trouble", "Spawn twice as many enemies. Enemies have their hitpoints reduced by 50% and provide half minerals & experience."); //4 directors active instead of 2, enemies have 50% HP and give -35% XP & cashmoney


    private String name;
    private String description;

    GameMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

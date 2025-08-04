package net.riezebos.bruus.tbd;

public class DevTestSettings {

    public static boolean blockDirectors = true; //if true: prevents ALL enemies from spawning
    public static boolean blockMusic = true; //if true: does NOT activate music during levels, making it impossible to end the level too
    public static boolean enablePlayerMovingPastBoundaries = true; //if true: allow the player to move outside the screen
    public static boolean devTestShortLevelMode = false; //if true: levels end practically immediatly
    public static boolean devTestMuteMode = false; //if true: mutes all audio after starting a level
    public static boolean onlyBossLevels = false; //if true: all levels are boss levels
    public static boolean infiniteMoney = false; //if true: add 9999999 money to the inventory
    public static boolean freeReroll = false; //if true; set reroll discount to 99
    public static boolean spawnTargetDummy = true; //if true, spawns a target dummy at the start of a level
}

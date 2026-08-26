package net.riezebos.bruus.tbd;

public class DevTestSettings {

    public static boolean blockDirectors = false; //if true: prevents ALL enemies from spawning
    public static boolean blockMusic = true; //if true: does NOT activate music during levels, making it impossible to end the level too and breaks localfiles audio
    public static boolean useItunes = true;
    public static boolean enablePlayerMovingPastBoundaries = false; //if true: allow the player to move outside the screen
    public static boolean devTestMuteMode = true; //if true: mutes all audio after starting a level
    public static boolean onlyBossLevels = false; //if true: all levels are boss levels
    public static boolean infiniteMoney = true; //if true: add 9999999 money to the inventory
    public static boolean freeReroll = true; //if true; set reroll discount to 99
    public static boolean spawnTargetDummy = false; //if true, spawns a target dummy at the start of a level
    public static boolean instantlySpawnPortal = false; //spawns a portla at the start of the level
    public static boolean instaKill = true; //multiplies basedamage by 100
    public static boolean playerIsImmune = false; //makes the player invincible by preventing takeDamage being executed
    public static boolean alloweSuicidebutton = false; //Enables the suicide button, causing 9999999 damage to the player(s)
    public static boolean rollFullShop = true; //If true, all 3 rows in the shop are available
    public static boolean exportPerformanceLogs = false;
    public static boolean enableMutalisk = false;
    public static boolean enableDirectShopAccess = true;



    public static boolean blockPlayerRevivers = true; //todo niet een echte test setting, verwijderen wanneer dit getest is
}

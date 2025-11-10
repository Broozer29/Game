package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;

public class EnemyDescriptionRetriever {
    //This class has no uses for now, possibly used for a glossary later on in development

    public static String getDescription(EnemyEnums enemy) {
        switch (enemy) {
            case Scout -> {
                return "Small and flimsy enemy. Moves relatively slow and shoots low damaging projectiles in a straight line in front of it.. " +
                        "Slowly moves towards the left until out of bounds during which it is removed. Grants little XP and money upon death";
            }
            case Needler -> {
                return "Small and flimy enemy. When nearby player, change direction towards the player and accelerates (once!). " +
                        "Keeps moving until colliding with the player upon which it explodes or it reaches the edges of the screen upon which it is removed from the field. Grants little xp and money upon death.";
            }

            case Seeker -> {
                return "Medium size and medium sturdy enemy. Hovers on the right side of the screen and thus never moves out of bounds. Always aims & shoots towards the player with a moderately moving and damaging laserbeam." +
                        "Grants moderate XP and money upon death.";
            }
            case Bomba -> {
                return "Large size and sturdy enemy. Slowly moves in a straight line towards the left, shooting 3 bombs in front of it that travel a short distance before exploding. Deals large amounts of damage." +
                        "Grants moderate XP and money upon death.";
            }
            case Bulldozer -> {
                return "Large size and sturdy enemy. Moves in a straight line towards the left. Has no attack, but upon spawn surrounds itself with bombs that rotate it, functioning as a tank for other enemies." +
                        "Grants moderate XP and money upon death.";
            }
            case Energizer -> {
                return "Medium size and medium sturdy enemy. Hovers on the right side of the screen and never moves out of bounds. Aims in a straight line in front of it. The projectile destroys the players projectiles on contact thus creating a barrier for fellow enemies." +
                        "Grants moderate XP and money upon death.";
            }

            case Tazer -> {
                return "Medium size and medium sturdy enemy. Hovers on the right side of the screen and never moves out of bounds. Aims in a random direction. The projectile can interact with the player and enemies. " +
                        "Upon hitting the player, reduce it's attack speed and damage by 25%. Upon hitting a fellow enemy, increase its attack speed by 25%. The projectile bounces around the screen until it hits 2 enemies or the player once." +
                        "Grants moderate XP and money upon death.";
            }

            case Flamer -> {
                return "Large size and sturdy enemy. Moves in a straight line towards the left. Shoots an EMP in a small radius around it which destroys the players projectiles and deals high damage."
                        + "Grants moderate XP and money upon death.";
            }
            case CashCarrier -> {
                return "Large size and heavy sturdy enemy. Moves in a straight line towards the left. Has no attack but increased toughness. Upon death, spawns 3 coins that bounce around the playing field that the player can retrieve for additional money. Spawns a maximum of 1 every 25-35 seconds" +
                        "Grants large amounts of XP and money upon death.";
            }
            case MotherShipMiniBoss -> {
                return "Mini boss. Moves around the entire playing field. Large size and heavy sturdy. Has no attacks on its own but spawns smaller drones that hover around the mothership. Maximum of 4 drones but replenishes them if killed" +
                    "Grants large amounts of XP and money upon death.";
            }
            case MotherShipDrone -> {
                return "Small summoned enemies that constantly shoot slow-moving projectiles towards the player. Grants no XP or money upon death.";
            }
            case Alien_Bomb -> {
                return "Small summoned enemy that rotates around the Bulldozer. Grants no XP or money upon death.";
            }
            case ShurikenMiniBoss -> {
                return "Mini boss. Has no attack but is exceptionally sturdy. Keeps bounding around the playing field.";
            }
            //Other enemies omitted for now.
        }
        return "";
    }
}

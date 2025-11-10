package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;

public interface BossActionable {
    boolean activateBehaviour(Enemy enemy);
    int getPriority();
    boolean isAvailable(Enemy enemy);

}

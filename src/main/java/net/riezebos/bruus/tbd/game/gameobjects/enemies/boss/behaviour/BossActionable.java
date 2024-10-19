package net.riezebos.bruus.tbd.game.gameobjects.enemies.boss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;

public interface BossActionable {
    public boolean activateBehaviour(Enemy enemy);
    public int getPriority();
    public boolean isAvailable(Enemy enemy);

}

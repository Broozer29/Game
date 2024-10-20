package game.gameobjects.enemies.boss.behaviour;

import game.gameobjects.GameObject;
import game.gameobjects.enemies.Enemy;

public interface BossActionable {
    public boolean activateBehaviour(Enemy enemy);
    public int getPriority();
    public boolean isAvailable(Enemy enemy);

}

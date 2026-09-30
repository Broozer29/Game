package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;

//Used to end the game
public class Portal extends GameObject {
	
	private boolean spawned = false;
	private boolean isFinalBossPortal = false;

	public Portal(SpriteAnimationConfiguration spriteAnimationConfiguration, boolean isFinalBossPortal){
		super(spriteAnimationConfiguration);
        this.isFinalBossPortal = isFinalBossPortal;
		this.animation.setOriginCoordinates(spriteAnimationConfiguration.getSpriteConfiguration().getxCoordinate(),
				spriteAnimationConfiguration.getSpriteConfiguration().getyCoordinate());
		this.animation.cropAnimation();

	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}
	
	public boolean isSpawned () {
		return this.spawned;
	}

    public void activatePortal(){
        LevelManager.getInstance().finishLevel();
        if(isFinalBossPortal){
            GameState.getInstance().setSpawnFinalBoss(true);
            GameState.getInstance().setGameState(GameStatusEnums.Show_Level_Score_Card);
        } else {
            GameState.getInstance().setGameState(GameStatusEnums.Show_Level_Score_Card);
        }
    }

    public void despawnPortal(){
        this.setTransparancyAlpha(true, 1.0f, -0.02f);
        this.setSpawned(false);
    }

}

package net.riezebos.bruus.tbd.game.gamestate;

public enum GameStatusEnums {
	Waiting, //Before the song & level starts
	Playing, //Speaks for itself
	Dying, //The slow-mo dying animation plays
	Dead, //The player is dead, show the game over screen
	Level_Finished, //Spawn the portal
	Level_Completed, //Player has entered portal, show score screen of something
	Zoning_In, // Starting a level, fade in
	Zoning_Out, // Ending a level, fade away
	Show_Level_Score_Card,
	Transition_To_Next_Stage,
	Shopping,
	Paused,
}

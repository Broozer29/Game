package gamedata;

public enum GameStatusEnums {
	Waiting, //Before the song & level starts
	Playing, //Speaks for itself
	Dying, //The slow-mo dying animation plays
	Dead, //The player is dead, show the game over screen
	Song_Finished, //Spawn the portal
	Level_Completed, //Player has entered portal, show score screen of something
	Zoning_In, // Blackness fading away, with a portal where the player spawns from
	Zoning_Out, // Fade to black
}

package VisualAndAudioData.audio.enums;

public enum AudioEnums {
	Player_Laserbeam(false),
	PlayerTakesDamage(false),
	NewPlayerLaserbeam(false),
	StickyGrenadeExplosion(false),
	SilentAudio(false),
	ChargingLaserbeam(false),
	Alien_Bomb_Destroyed(false),
	Alien_Spaceship_Destroyed(false),
	Alien_Bomb_Impact(false),
	Large_Ship_Destroyed(false),
	Default_EMP(false),
	Furi_Wisdowm_Of_Rage(true),
	Furi_My_Only_Chance(true),
	Furi_Make_This_Right(true),
	Ayasa_The_Reason_Why(true),
	Apple_Holder_Remix(true),
	Destroyed_Explosion(false),
	Power_Up_Acquired(false),
	Flamethrower(false),
	Rocket_Launcher(false),
	NONE(false),
	Firewall(false),
	New_Arcades_Solace(true),
	mainmenu(true),
	Viq_Rose(true),
	Five_Seconds_Before_Sunrise(true),
	Downtown_Binary_Astral(true),
	Carpenter_Brut_Enraged(true),
	Carpenter_Brut_Youre_Mine(true),
	Alpha_Room_Come_Back(true),
	Carpenter_Brut_Danger(true),
	Knight_Something_Memorable(true),
	Downtown_Binary_Fantasia(true),
	Downtown_Binary_Light_Cycles(true),
	Marvel_Golden_Dawn(true),
	Tonebox_Memory_Upload(true),
	Forhill_Iris(true),
	Tonebox_Radium_Cloud_Highway(true),
	The_Rain_Formerly_Known_As_Purple(true),
	Blood_On_The_Dancefloor(true),
	Lemmino_Firecracker(true),
	Mydnyte(true),
	Le_Youth_Chills(true),
	Robert_Nickson_Painting_The_Skies(true),
	Cannons_Fire_For_You(true),
	EMBRZ_Rain_On_My_Window(true),
	EMBRZ_Light_Falls(true),
	Deadmau5_Monophobia(true),
	Johny_Theme(true),
	Viq_Girl_From_Nowhere(true),
	Space_Sailors_Cosmos(true),
	New_Arcades_Severed(true),
	Arksun_Arisen(true),
	Ghost_Data_Gods_Of_The_Artificial(true),
	Ghost_Data_Dark_Harvest(true),
	BlackGummy_SuperHuman(true),
	Maduk_Alone(true),
	KingPalmRunway(true),
	ApproachingNirvanaThousandPictures(true),
	ApproachingNirvanaNoStringsAttached(true),
	VendlaSonrisa(true),
	NotEnoughMinerals(false),
	WaveshaperMonster(true),
	TonyLeysSnowdinRemix(true),
	GustyGardenGalaxyRemix(true),
	WePlantsAreHappyPlantsTimeRemix(true),
	BossBattle(true);

	// Boolean attribute to indicate if this should be streamed
	private final boolean shouldStream;

	// Constructor to set the streaming flag
	AudioEnums(boolean shouldStream) {
		this.shouldStream = shouldStream;
	}

	// Method to check if the audio should be streamed
	public boolean shouldBeStreamed() {
		return shouldStream;
	}
}

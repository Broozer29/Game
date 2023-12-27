package menuscreens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.objects.player.PlayerAttackTypes;
import game.objects.player.PlayerSpecialAttackTypes;
import game.objects.player.playerpresets.GunPreset;
import game.objects.player.playerpresets.SpecialGunPreset;
import game.objects.player.PlayerStats;
import gamedata.audio.AudioDatabase;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class MenuObject {

	private List<MenuObjectPart> menuTiles = new ArrayList<MenuObjectPart>();
	private int xCoordinate;
	private int yCoordinate;

	private String text;
	private MenuObjectEnums menuObjectType;
	private MenuFunctionEnums menuFunctionality;
	private ImageEnums imageType;
	private float scale;

	public MenuObject(int xCoordinate, int yCoordinate, float scale, String text, MenuObjectEnums menuObjectType,
			MenuFunctionEnums menuFunctionality) {
		this.setXCoordinate(xCoordinate);
		this.setYCoordinate(yCoordinate);
		this.text = text;
		this.menuObjectType = menuObjectType;
		this.menuFunctionality = menuFunctionality;
		this.scale = scale;
		this.imageType = MenuFunctionImageCoupler.getInstance().getImageByMenuType(menuObjectType);
		initMenuImages();
	}

	private void initMenuImages() {
		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setxCoordinate(xCoordinate);
		spriteConfiguration.setyCoordinate(yCoordinate);
		spriteConfiguration.setScale(scale);
		spriteConfiguration.setImageType(imageType);


		if (!(this.menuObjectType == MenuObjectEnums.Text_Block)) {
			if (this.menuObjectType == MenuObjectEnums.Highlight_Animation) {
				spriteConfiguration.setImageType(ImageEnums.Invisible);
				MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
				newTile.setTileAnimation(imageType);
				this.menuTiles.add(newTile);
			} else {
				MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
				this.menuTiles.add(newTile);
			}
		} else if (this.menuObjectType == MenuObjectEnums.Text_Block) {
			initMenuTextBlock();
		}
	}

	public void menuTileAction() throws UnsupportedAudioFileException, IOException {
		AudioManager audioManager = AudioManager.getInstance();
		AudioDatabase.getInstance().updateGameTick();
		switch (this.menuFunctionality) {
		case Start_Game:
			BoardManager.getInstance().initGame();
			break;
		case Select_Talent_Selection_Board:
			BoardManager.getInstance().initTalentSelectionBoard();
			break;
		case Select_Laserbeam:
			audioManager.addAudio(AudioEnums.Player_Laserbeam);
			PlayerStats.getInstance().setNormalGunPreset(new GunPreset(PlayerAttackTypes.Laserbeam));
			PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Laserbeam);
			break;
		case Select_Flamethrower:
			audioManager.addAudio(AudioEnums.Flamethrower);
			PlayerStats.getInstance().setNormalGunPreset(new GunPreset(PlayerAttackTypes.Flamethrower));
			PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Flamethrower);
			break;
		case Return_To_Main_Menu:
			BoardManager.getInstance().initMainMenu();
			break;
		case NONE:
			break;
		case Select_Rocket_Launcher:
			audioManager.addAudio(AudioEnums.Rocket_Launcher);
			PlayerStats.getInstance().setNormalGunPreset(new GunPreset(PlayerAttackTypes.Rocket));
			PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Rocket);
			break;
		case Select_EMP:
			audioManager.addAudio(AudioEnums.Default_EMP);
			PlayerStats.getInstance().setSpecialGunPreset(new SpecialGunPreset(PlayerSpecialAttackTypes.EMP));
			PlayerStats.getInstance().setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP);
			break;
		case Select_Firewall:
			audioManager.addAudio(AudioEnums.Firewall);
			PlayerStats.getInstance().setSpecialGunPreset(new SpecialGunPreset(PlayerSpecialAttackTypes.Firewall));
			PlayerStats.getInstance().setPlayerSpecialAttackType(PlayerSpecialAttackTypes.Firewall);
			break;
		case Select_Level_Board:
			BoardManager.getInstance().initLevelSelectionBoard();
			break;
			
		default:
			System.out.println("Unimplemented MenuObject behaviour was attempted!");
			break;
		}
	}

	private void initMenuTextBlock() {
		int startingXCoordinate = this.xCoordinate;
		int startingYCoordinate = this.yCoordinate;
		int kernelDistance = (int) Math.ceil(10 * scale);

		for (int i = 0; i < text.length(); i++) {
			char stringChar = text.charAt(i);
			if (stringChar != ' ') {
				ImageEnums imageType = ImageEnums.fromChar(stringChar); // convert char to Letter enum
				SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
				spriteConfiguration.setImageType(imageType);
				spriteConfiguration.setxCoordinate(startingXCoordinate + (kernelDistance * i));
				spriteConfiguration.setyCoordinate(startingYCoordinate);
				spriteConfiguration.setScale(scale);

				MenuObjectPart newTile = new MenuObjectPart(spriteConfiguration);
				menuTiles.add(newTile);
			}
		}
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public List<MenuObjectPart> getMenuImages() {
		return menuTiles;
	}

	public MenuFunctionEnums getMenuFunction() {
		return this.menuFunctionality;
	}

	public MenuObjectEnums getMenuObjectType() {
		return this.menuObjectType;
	}
	
	public void changeImage(ImageEnums newImage) {
		this.imageType = newImage;
		menuTiles = new ArrayList<MenuObjectPart>();
		initMenuImages();
		
	}

}
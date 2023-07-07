package menuscreens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.PlayerStats;
import data.audio.AudioDatabase;
import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.managers.AnimationManager;
import game.managers.AudioManager;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;
import game.playerpresets.GunPreset;
import game.playerpresets.SpecialGunPreset;

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
		if (!(this.menuObjectType == MenuObjectEnums.Text_Block)) {
			if (this.menuObjectType == MenuObjectEnums.Highlight_Animation) {
				MenuObjectPart newTile = new MenuObjectPart(ImageEnums.Invisible, xCoordinate, xCoordinate, scale);
				newTile.setTileAnimation(imageType);
				this.menuTiles.add(newTile);
				AnimationManager.getInstance().addUpperAnimation(newTile.getAnimation());
			} else {
				MenuObjectPart newTile = new MenuObjectPart(imageType, xCoordinate, xCoordinate, scale);
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
		case Select_Setup_Menu:
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
			// Deprecated
			BoardManager.getInstance().userSelectionToMainMenu();
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
		default:
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
				MenuObjectPart newTile = new MenuObjectPart(imageType, startingXCoordinate + (kernelDistance * i),
						startingYCoordinate, scale);

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
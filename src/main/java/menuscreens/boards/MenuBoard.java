package menuscreens.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import data.DataClass;
import data.image.enums.ImageEnums;
import game.managers.BackgroundManager;
import game.objects.BackgroundObject;
import menuscreens.MenuCursor;
import menuscreens.MenuTile;
import visual.objects.Sprite;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;



public class MenuBoard extends JPanel implements ActionListener {
	private DataClass data = DataClass.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuTile> tiles = new ArrayList<MenuTile>();
	private MenuCursor menuCursor;
	private MenuTile startGameTile;
	private MenuTile selectUserTile;
    private Timer timer;

	public MenuBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		initMenuTiles();
		
        timer = new Timer(16, e -> repaint());
        timer.start();
	}

	//Initialize all starter pointers
	private void initMenuTiles() {
		this.startGameTile = new MenuTile(ImageEnums.Start_Game, (boardWidth / 2), (boardHeight / 2), 1);
		this.menuCursor = new MenuCursor((boardWidth / 2 - 50), startGameTile.getYCoordinate(), 1);
		this.menuCursor.setSelectedMenuTile(startGameTile);
		this.selectUserTile = new MenuTile(ImageEnums.Select_User_Menu, (boardWidth / 2), (boardHeight / 2) + 50, 1);
	}
	//Recreate the tilesList that gets drawn by drawComponents	
	private void recreateList() {
		tiles.clear();
		addTileToList(startGameTile);
		addTileToList(selectUserTile);
	}

	private void addTileToList(MenuTile menuTile) {
		tiles.add(menuTile);
	}

	//Activate the functionality of the specific menutile
	private void selectMenuTile() {
		for(MenuTile tile : tiles) {
			if (menuCursor.getSelectedMenuTile().equals(tile)) {
				tile.menuTileAction();
				timer.stop();
			}
		}
	}

	// Go one menu tile upwards
	private void previousMenuTile() {
		for (int i = 0; i < tiles.size(); i++) {
			if ((menuCursor.getSelectedMenuTile().equals(tiles.get(i)))) {
				if ((i > 0)) {
					menuCursor.setY(tiles.get(i - 1).getYCoordinate());
					menuCursor.setX(tiles.get(i - 1).getXCoordinate() - 50);
					menuCursor.setSelectedMenuTile(tiles.get(i - 1));
					break;
				}
			}
		}
	}

	// Go one menu tile downwards
	private void nextMenuTile() {
		for (int i = 0; i < tiles.size(); i++) {
			if ((menuCursor.getSelectedMenuTile().equals(tiles.get(i)))) {
				if ((i + 1) < tiles.size()) {
					menuCursor.setY(tiles.get(i + 1).getYCoordinate());
					menuCursor.setX(tiles.get(i + 1).getXCoordinate() - 50);
					menuCursor.setSelectedMenuTile(tiles.get(i + 1));
					break;
				}
			}
		}
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case (KeyEvent.VK_ENTER):
				selectMenuTile();
				break;
			case (KeyEvent.VK_A):
				break;
			case (KeyEvent.VK_D):
				break;
			case (KeyEvent.VK_W):
				previousMenuTile();
				break;
			case (KeyEvent.VK_S):
				nextMenuTile();
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case (KeyEvent.VK_ENTER):
				break;
			case (KeyEvent.VK_A):
				break;
			case (KeyEvent.VK_D):
				break;
			case (KeyEvent.VK_W):
				break;
			case (KeyEvent.VK_S):
				break;
			}
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draws all background objects
		for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
			drawImage(g, bgObject);
		}
		drawObjects(g);
		backgroundManager.updateGameTick();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawObjects(Graphics g) {
		recreateList();
		g.drawImage(menuCursor.getImage(), menuCursor.getXCoordinate(), menuCursor.getYCoordinate(), this);
		for (MenuTile tile : tiles) {
			g.drawImage(tile.getImage(), tile.getXCoordinate(), tile.getYCoordinate(), this);
		}
	}
	
	private void drawImage(Graphics g, Sprite sprite) {
		if (sprite.getImage() != null) {
			g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}

package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import Main.Data.DataClass;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.Timer;

public class MenuBoard extends JPanel implements ActionListener {
	private DataClass data = DataClass.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuTile> tiles = new ArrayList<MenuTile>();
	private MenuTile pointerTile;
	private MenuTile startGameTile;
	private MenuTile selectUserTile;

	public MenuBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		initMenuTiles();
	}

	//Initialize all starter pointers
	private void initMenuTiles() {
		this.startGameTile = new MenuTile("StartGame", (boardWidth / 2), (boardHeight / 2));
		this.pointerTile = new MenuTile("Pointer", (boardWidth / 2 - 50), startGameTile.getYCoordinate());
		this.selectUserTile = new MenuTile("SelectUser", (boardWidth / 2), (boardHeight / 2) + 50);
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
			if (pointerTile.getYCoordinate() == tile.getYCoordinate()) {
				tile.menuTileAction();
			}
		}
	}

	//Go one menu tile upwards
	private void previousMenuTile() {
		int pointerY = pointerTile.getYCoordinate();
		for (int i = 0; i < tiles.size(); i++) {
			if (pointerY == tiles.get(i).getYCoordinate() && !(i < 1)) {
				pointerTile.setY(tiles.get(i-1).getYCoordinate());
				paintComponent(getGraphics());
				break;
			}
		}
	}

	//Go one menu tile downwards
	private void nextMenuTile() {
		int pointerY = pointerTile.getYCoordinate();
		for (int i = 0; i < tiles.size(); i++) {
			if (pointerY == tiles.get(i).getYCoordinate() && !(i+1 > tiles.size() - 1)) {
				pointerTile.setY(tiles.get(i+1).getYCoordinate());
				paintComponent(getGraphics());
				break;
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
		drawObjects(g);
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void drawObjects(Graphics g) {
		recreateList();
		g.drawImage(pointerTile.getImage(), pointerTile.getXCoordinate(), pointerTile.getYCoordinate(), this);
		for (MenuTile tile : tiles) {
			g.drawImage(tile.getImage(), tile.getXCoordinate(), tile.getYCoordinate(), this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}

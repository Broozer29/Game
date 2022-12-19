package menuscreens.boards;

import javax.swing.JPanel;

import data.DataClass;
import menuscreens.MenuCursor;
import menuscreens.MenuTile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class UserSelectionBoard extends JPanel implements ActionListener {
	private DataClass data = DataClass.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuTile> tiles = new ArrayList<MenuTile>();
	private MenuCursor menuCursor;
	private MenuTile userOneTile;
	private MenuTile userTwoTile;
	private MenuTile userThreeTile;
	private MenuTile returnToMenuTile;

	public UserSelectionBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		initMenuTiles();
	}

	// Initialize all starter tiles
	private void initMenuTiles() {
		this.userOneTile = new MenuTile("UserOne", (boardWidth - (boardWidth - 100)), (boardHeight / 2), 1);
		this.userTwoTile = new MenuTile("UserTwo", (boardWidth - (boardWidth - 300)), (boardHeight / 2), 1);
		this.userThreeTile = new MenuTile("UserThree", (boardWidth - (boardWidth - 500)), (boardHeight / 2), 1);
		this.returnToMenuTile = new MenuTile("userMenuToMainMenu", boardWidth - 150, boardHeight - 100, 1);
		this.menuCursor = new MenuCursor(userOneTile.getXCoordinate() - 50, userOneTile.getYCoordinate(), 1);
		this.menuCursor.setSelectedMenuTile(userOneTile);
	}

	// Recreate the tilesList that gets drawn by drawComponents
	private void recreateList() {
		tiles.clear();
		addTileToList(userOneTile);
		addTileToList(userTwoTile);
		addTileToList(userThreeTile);
		addTileToList(returnToMenuTile);

	}

	private void addTileToList(MenuTile menuTile) {
		tiles.add(menuTile);
	}

	// Activate the functionality of the specific menutile
	private void selectMenuTile() {
		for (MenuTile tile : tiles) {
			if (menuCursor.getSelectedMenuTile().equals(tile)) {
				tile.menuTileAction();
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
					paintComponent(getGraphics());
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
					paintComponent(getGraphics());
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
				previousMenuTile();
				break;
			case (KeyEvent.VK_D):
				nextMenuTile();
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
		g.drawImage(menuCursor.getImage(), menuCursor.getXCoordinate(), menuCursor.getYCoordinate(), this);
		for (MenuTile tile : tiles) {
			g.drawImage(tile.getImage(), tile.getXCoordinate(), tile.getYCoordinate(), this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}

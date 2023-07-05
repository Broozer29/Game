package menuscreens.boards;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.DataClass;
import game.managers.AudioManager;
import game.objects.BackgroundManager;
import game.objects.BackgroundObject;
import menuscreens.MenuCursor;
import menuscreens.MenuFunctionEnums;
import menuscreens.MenuObject;
import menuscreens.MenuObjectEnums;
import menuscreens.MenuObjectPart;
import visual.objects.Sprite;

public class MenuBoard extends JPanel implements ActionListener {
	private DataClass data = DataClass.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuObject> firstRow = new ArrayList<MenuObject>();
	private List<MenuObject> secondRow = new ArrayList<MenuObject>();
	private List<MenuObject> thirdRow = new ArrayList<MenuObject>();
	private List<List<MenuObject>> grid = new ArrayList<>();
	private MenuCursor menuCursor;
	private MenuObject startGameTile;
	private MenuObject selectUserTile;
	private Timer timer;
	private MenuObject titleImage;
	private MenuObject selectUserTile2;
	private MenuObject selectUserTile3;
	private MenuObject wasdExplanation;
	private MenuObject shiftExplanation;
	private MenuObject attackExplanation;

	private int selectedRow = 0;
	private int selectedColumn = 0;

	public MenuBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		initMenuTiles();

		timer = new Timer(16, e -> repaint());
		timer.start();
	}

	// Initialize all starter pointers
	private void initMenuTiles() {
		float imageScale = 1;
		float textScale = (float) 1.3;
		this.startGameTile = new MenuObject((boardWidth / 2), (boardHeight / 2), textScale, "Start Game",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.Start_Game);

		int initCursorX = startGameTile.getXCoordinate();
		int initCursorY = startGameTile.getYCoordinate();
		this.menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
		menuCursor.setXCoordinate(startGameTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));

		this.selectUserTile = new MenuObject((boardWidth / 2), (boardHeight / 2) + 50, textScale, "Laserbeam Setup",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.Select_Laserbeam_Preset);
		this.selectUserTile2 = new MenuObject((boardWidth / 2) + 300, (boardHeight / 2), textScale,
				"Flamethrower Setup", MenuObjectEnums.Text_Block, MenuFunctionEnums.Select_Flamethrower_Preset);
		this.selectUserTile3 = new MenuObject((boardWidth / 2) + 300, (boardHeight / 2) + 50, textScale,
				"Rocket setup missing special attack lol", MenuObjectEnums.Text_Block, MenuFunctionEnums.Select_Rocket_Preset);

		this.titleImage = new MenuObject(200, (boardHeight / 2) / 2, imageScale, null, MenuObjectEnums.Title_Image,
				MenuFunctionEnums.NONE);

		int explanationXCoordinate = boardWidth / 100;
		int explanationYCoordinate = boardHeight / 2 + 300;
		this.wasdExplanation = new MenuObject(explanationXCoordinate, explanationYCoordinate + 0, textScale,
				"Movement can be done with WASD or arrow keys", MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);
		this.shiftExplanation = new MenuObject(explanationXCoordinate, explanationYCoordinate + 20, textScale,
				"Hold left shift for increased movement speed", MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.attackExplanation = new MenuObject(explanationXCoordinate, explanationYCoordinate + 40, textScale,
				"Spacebar for normal attacks. Q or Enter for special attacks", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.NONE);

		this.menuCursor.setSelectedMenuTile(startGameTile);

		grid.add(firstRow);
		grid.add(secondRow);
		grid.add(thirdRow);

	}

	// Recreate the tilesList that gets drawn by drawComponents
	private void recreateList() {
		firstRow.clear();
		secondRow.clear();
		thirdRow.clear();

		addTileToFirstRow(startGameTile);
		addTileToFirstRow(selectUserTile2);
		addTileToSecondRow(selectUserTile);
		addTileToSecondRow(selectUserTile3);

	}

	private void addTileToFirstRow(MenuObject menuTile) {
		firstRow.add(menuTile);
	}

	private void addTileToSecondRow(MenuObject menuTile) {
		secondRow.add(menuTile);
	}

	private void addTileToThirdRow(MenuObject menuTile) {
		thirdRow.add(menuTile);
	}

	/*------------------------Navigation methods--------------------------------*/

	// Activate the functionality of the specific menutile
	private void selectMenuTile() {
		try {
			grid.get(selectedRow).get(selectedColumn).menuTileAction();
			if (grid.get(selectedRow).get(selectedColumn).getMenuFunction() == MenuFunctionEnums.Start_Game) {
				timer.stop();
			}
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}

	}

	private void previousMenuTile() {
		if (isGridEmpty()) {
			return; // Do nothing if the grid is empty
		}

		int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

		do {
			selectedRow--;
			if (selectedRow < 0) {
				selectedRow = grid.size() - 1; // Wrap around to the bottom row
			}
		} while (grid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
																					// found or we've checked all rows

		// Adjust column to be within the new row
		if (!grid.get(selectedRow).isEmpty() && selectedColumn >= grid.get(selectedRow).size()) {
			selectedColumn = grid.get(selectedRow).size() - 1;
		}
		updateCursor();
	}

	// Go one menu tile downwards
	private void nextMenuTile() {
		if (isGridEmpty()) {
			return; // Do nothing if the grid is empty
		}

		int originalRow = selectedRow; // Keep track of the starting row to avoid infinite loop

		do {
			selectedRow++;
			if (selectedRow >= grid.size()) {
				selectedRow = 0; // Wrap around to the top row
			}
		} while (grid.get(selectedRow).isEmpty() && selectedRow != originalRow); // Repeat until a non-empty row is
																					// found or we've checked all rows

		// Adjust column to be within the new row
		if (!grid.get(selectedRow).isEmpty() && selectedColumn >= grid.get(selectedRow).size()) {
			selectedColumn = grid.get(selectedRow).size() - 1;
		}
		updateCursor();
	}

	// Check if the grid is empty
	private boolean isGridEmpty() {
		for (List<MenuObject> row : grid) {
			if (!row.isEmpty()) {
				return false; // Return false as soon as a non-empty row is found
			}
		}
		return true; // If no non-empty rows are found, the grid is empty
	}

	// Go one menu tile to the left
	private void previousMenuColumn() {
		selectedColumn--;
		if (selectedColumn < 0) {
			selectedColumn = grid.get(selectedRow).size() - 1; // Wrap around to the rightmost column
		}
		updateCursor();
	}

	// Go one menu tile to the right
	private void nextMenuColumn() {
		selectedColumn++;
		if (selectedColumn >= grid.get(selectedRow).size()) {
			selectedColumn = 0; // Wrap around to the leftmost column
		}
		updateCursor();
	}

	// Update the cursor's position and selected menu tile
	private void updateCursor() {
		if (grid.get(selectedRow).isEmpty()) { // Check if the selected row is empty
			menuCursor.setSelectedMenuTile(null); // You might need to decide how you want to handle this situation in
													// your application
		} else {
			MenuObject selectedTile = grid.get(selectedRow).get(selectedColumn);
			menuCursor.setSelectedMenuTile(selectedTile);
			menuCursor.setYCoordinate(selectedTile.getYCoordinate());
			menuCursor.setXCoordinate(selectedTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
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
				previousMenuColumn();
				break;
			case (KeyEvent.VK_D):
				nextMenuColumn();
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

	/*-----------------------------End of navigation methods--------------------------*/

	/*---------------------------Drawing methods-------------------------------*/
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
		g.drawImage(menuCursor.getMenuImages().get(0).getImage(), menuCursor.getXCoordinate(),
				menuCursor.getYCoordinate(), this);
		g.drawImage(titleImage.getMenuImages().get(0).getImage(), titleImage.getXCoordinate(),
				titleImage.getYCoordinate(), this);

		for (MenuObjectPart letter : wasdExplanation.getMenuImages()) {
			g.drawImage(letter.getImage(), letter.getXCoordinate(), letter.getYCoordinate(), this);
		}

		for (MenuObjectPart letter : shiftExplanation.getMenuImages()) {
			g.drawImage(letter.getImage(), letter.getXCoordinate(), letter.getYCoordinate(), this);
		}

		for (MenuObjectPart letter : attackExplanation.getMenuImages()) {
			g.drawImage(letter.getImage(), letter.getXCoordinate(), letter.getYCoordinate(), this);
		}

		for (List<MenuObject> list : grid) {
			for (MenuObject object : list) {
				for (MenuObjectPart menuPart : object.getMenuImages()) {
					g.drawImage(menuPart.getImage(), menuPart.getXCoordinate(), menuPart.getYCoordinate(), this);
				}
			}
		}

	}

	private void drawImage(Graphics g, Sprite sprite) {
		if (sprite.getImage() != null) {
			g.drawImage(sprite.getImage(), sprite.getXCoordinate(), sprite.getYCoordinate(), this);
		}
	}

	/*------------------------------End of Drawing methods-------------------------------*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
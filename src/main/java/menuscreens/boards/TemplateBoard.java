package menuscreens.boards;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import controllerInput.ConnectedControllers;
import controllerInput.ControllerInput;
import controllerInput.ControllerInputReader;
import game.managers.AnimationManager;
import game.objects.BackgroundManager;
import game.objects.BackgroundObject;
import gamedata.DataClass;
import menuscreens.MenuCursor;
import menuscreens.MenuFunctionEnums;
import menuscreens.MenuObject;
import menuscreens.MenuObjectEnums;
import menuscreens.MenuObjectPart;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateBoard extends JPanel implements ActionListener {

	private DataClass data = DataClass.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private ConnectedControllers controllers = ConnectedControllers.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuObject> firstColumn = new ArrayList<MenuObject>();
	private List<MenuObject> secondColumn = new ArrayList<MenuObject>();
	private List<MenuObject> thirdColumn = new ArrayList<MenuObject>();
	private List<List<MenuObject>> grid = new ArrayList<>();

	private List<MenuObject> offTheGridObjects = new ArrayList<MenuObject>();

	private Timer timer;
	private int selectedRow = 0;
	private int selectedColumn = 0;
	
	private MenuCursor menuCursor;
	private MenuObject dummyTest;
	private MenuObject returnToMainMenu;
	
	
	private ControllerInputReader controllerInputReader;
	private boolean initializedMenuObjects = false;

	public TemplateBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		initMenuTiles();

		if (controllers.getFirstController() != null) {
			controllerInputReader = controllers.getControllerInputReader(controllers.getFirstController());
		}

		timer = new Timer(16, e -> repaint());
		timer.start();
	}

	private void initMenuTiles() {
		// TODO Auto-generated method stub
		// Create the menu tiles and options and what not

		float imageScale = 1;
		float textScale = 1;
		this.dummyTest = new MenuObject(150, (boardHeight / 2), textScale, "TESTING TESTING",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.Start_Game);
		
		this.returnToMainMenu = new MenuObject(300, 300, textScale, "RETURN TO MAIN MENU", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Return_To_Main_Menu);
		
		int initCursorX = returnToMainMenu.getXCoordinate();
		int initCursorY = returnToMainMenu.getYCoordinate();
		this.menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
		menuCursor.setXCoordinate(returnToMainMenu.getXCoordinate() - (menuCursor.getxDistanceToKeep()));
		
		initializedMenuObjects = true;
	}

	public void recreateWindow() {
		if (initializedMenuObjects) {
			animationManager.resetManager();
			firstColumn.clear();
			secondColumn.clear();
			thirdColumn.clear();
			offTheGridObjects.clear();
			
			
			addTileToFirstColumn(returnToMainMenu);

			offTheGridObjects.add(menuCursor);
			offTheGridObjects.add(dummyTest);
			this.menuCursor.setSelectedMenuTile(returnToMainMenu);
			
			
			grid.add(firstColumn);
			grid.add(secondColumn);
			grid.add(thirdColumn);
		}
	}

	private void recreateList() {
		firstColumn.clear();
		secondColumn.clear();
		thirdColumn.clear();

		// Add the created tiles on the grid somewhere, the order MATTERS for navigation!
		addTileToFirstColumn(returnToMainMenu);

	}

	private void addTileToFirstColumn(MenuObject menuTile) {
		firstColumn.add(menuTile);
	}

	private void addTileToSecondColumn(MenuObject menuTile) {
		secondColumn.add(menuTile);
	}

	private void addTileToThirdColumn(MenuObject menuTile) {
		thirdColumn.add(menuTile);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

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
				previousMenuTile();
				break;
			case (KeyEvent.VK_D):
				nextMenuTile();
				break;
			case (KeyEvent.VK_W):
				previousMenuColumn();
				break;
			case (KeyEvent.VK_S):
				nextMenuColumn();
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

	private long lastMoveTime = 0;
	private static final long MOVE_COOLDOWN = 200; // 500 milliseconds

	public void executeControllerInput() {
		if (controllers.getFirstController() != null) {
			controllerInputReader.pollController();
			long currentTime = System.currentTimeMillis();

			// Left and right navigation
			if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
				if (controllerInputReader.isInputActive(ControllerInput.MOVE_LEFT_SLOW)
						|| controllerInputReader.isInputActive(ControllerInput.MOVE_LEFT_QUICK)) {
					// Menu option to the left
					previousMenuTile();
					System.out.println("Move!");
					lastMoveTime = currentTime;
				} else if (controllerInputReader.isInputActive(ControllerInput.MOVE_RIGHT_SLOW)
						|| controllerInputReader.isInputActive(ControllerInput.MOVE_RIGHT_QUICK)) {
					// Menu option to the right
					nextMenuTile();
					System.out.println("Move!");
					lastMoveTime = currentTime;
				}
			}

			// Up and down navigation
			if (currentTime - lastMoveTime > MOVE_COOLDOWN) {
				if (controllerInputReader.isInputActive(ControllerInput.MOVE_UP_SLOW)
						|| controllerInputReader.isInputActive(ControllerInput.MOVE_UP_QUICK)) {
					// Menu option upwards
					previousMenuColumn();
					System.out.println("Move!");
					lastMoveTime = currentTime;
				} else if (controllerInputReader.isInputActive(ControllerInput.MOVE_DOWN_SLOW)
						|| controllerInputReader.isInputActive(ControllerInput.MOVE_DOWN_QUICK)) {
					// Menu option downwards
					nextMenuColumn();
					System.out.println("Move!");
					lastMoveTime = currentTime;
				}
			}

			// Fire action
			if (controllerInputReader.isInputActive(ControllerInput.FIRE)) {
				// Select menu option
				selectMenuTile();
			}
		}
	}

	/*-----------------------------End of navigation methods--------------------------*/

	/*---------------------------Drawing methods-------------------------------*/
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		// Draws all background objects
		for (BackgroundObject bgObject : backgroundManager.getAllBGO()) {
			drawImage(g2d, bgObject);
		}

		for (SpriteAnimation animation : animationManager.getLowerAnimations()) {
			drawAnimation(g2d, animation);
		}

		drawObjects(g2d);

		for (SpriteAnimation animation : animationManager.getUpperAnimations()) {
			drawAnimation(g2d, animation);
		}
		animationManager.updateGameTick();
		backgroundManager.updateGameTick();
		Toolkit.getDefaultToolkit().sync();

		// Reading controller input
		executeControllerInput();

	}

	private void drawObjects(Graphics2D g) {
		recreateList();
		for (MenuObject object : offTheGridObjects) {
			if (object.getMenuObjectType() == MenuObjectEnums.Text_Block) {
				for (MenuObjectPart letter : object.getMenuImages()) {
					g.drawImage(letter.getImage(), letter.getXCoordinate(), letter.getYCoordinate(), this);
				}
			} else {
				g.drawImage(object.getMenuImages().get(0).getImage(), object.getXCoordinate(), object.getYCoordinate(),
						this);
			}
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

	private void drawAnimation(Graphics2D g, SpriteAnimation animation) {
		if (animation.getCurrentFrameImage() != null) {
			g.drawImage(animation.getCurrentFrameImage(), animation.getXCoordinate(), animation.getYCoordinate(), this);
		}
	}

	/*------------------------------End of Drawing methods-------------------------------*/

}
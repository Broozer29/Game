package menuscreens.boards;

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

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.ConnectedControllers;
import data.DataClass;
import data.PlayerStats;
import data.image.ImageEnums;
import game.managers.AnimationManager;
import game.objects.BackgroundManager;
import game.objects.BackgroundObject;
import menuscreens.MenuCursor;
import menuscreens.MenuFunctionEnums;
import menuscreens.MenuObject;
import menuscreens.MenuObjectEnums;
import menuscreens.MenuObjectPart;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class MenuBoard extends JPanel implements ActionListener {
	private DataClass data = DataClass.getInstance();
	private BackgroundManager backgroundManager = BackgroundManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private ConnectedControllers controllers = ConnectedControllers.getInstance();
	private final int boardWidth = data.getWindowWidth();;
	private final int boardHeight = data.getWindowHeight();;
	private List<MenuObject> firstRow = new ArrayList<MenuObject>();
	private List<MenuObject> secondRow = new ArrayList<MenuObject>();
	private List<MenuObject> thirdRow = new ArrayList<MenuObject>();
	private List<List<MenuObject>> grid = new ArrayList<>();

	private List<MenuObject> offTheGridObjects = new ArrayList<MenuObject>();

	private MenuCursor menuCursor;
	private MenuObject startGameTile;

	private Timer timer;
	private MenuObject titleImage;

	private MenuObject wasdExplanation;
	private MenuObject shiftExplanation;
	private MenuObject attackExplanation;
	private MenuObject specialAttackExplanation;

	private MenuObject startGameCard;
	private MenuObject selectNormalAttackCard;
	private MenuObject selectSpecialAttackCard;
	private MenuObject selectNormalAttackTitle;
	private MenuObject selectNormalAttackIcon;
	private MenuObject selectNormalAttackIconHighlight;
	private MenuObject selectSpecialAttackTitle;
	private MenuObject selectSpecialAttackIcon;
	private MenuObject selectSpecialAttackIconHighlight;

	private MenuObject selectFlamethrower;
	private MenuObject selectRocketLauncher;
	private MenuObject selectLaserbeam;
	private MenuObject selectEMP;
	private MenuObject selectFirewall;
	
	private boolean foundControllerBool = false;
	private MenuObject foundController;

	private int selectedRow = 0;
	private int selectedColumn = 0;

	public MenuBoard() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		initMenuTiles();
		controllers.initController();
		if(controllers.getFirstController() != null) {
			foundControllerBool = true;
		}
		timer = new Timer(16, e -> repaint());
		timer.start();
	}

	// Initialize all starter pointers
	private void initMenuTiles() {

		// With functionality:
		float imageScale = 1;
		float textScale = (float) 2;
		this.startGameTile = new MenuObject(150, (boardHeight / 2), textScale, "Start Game", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Start_Game);

		int initCursorX = startGameTile.getXCoordinate();
		int initCursorY = startGameTile.getYCoordinate();
		this.menuCursor = new MenuCursor(initCursorX, initCursorY, imageScale);
		menuCursor.setXCoordinate(startGameTile.getXCoordinate() - (menuCursor.getxDistanceToKeep()));

		this.selectFlamethrower = new MenuObject((boardWidth / 2) + 300, (boardHeight / 2), textScale,
				"Flamethrower Setup", MenuObjectEnums.Text_Block, MenuFunctionEnums.Select_Flamethrower);
		this.selectRocketLauncher = new MenuObject((boardWidth / 2) + 300, (boardHeight / 2) + 50, textScale,
				"Rocket setup missing special attack lol", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_Rocket_Launcher);

		this.titleImage = new MenuObject((boardWidth / 2) - 357, (boardHeight / 2) - 300, imageScale, null,
				MenuObjectEnums.Title_Image, MenuFunctionEnums.NONE);

		// Without functionality:

		int xCoordinate = boardWidth / 100;
		int yCoordinate = boardHeight / 2 + 300;
		this.wasdExplanation = new MenuObject(xCoordinate, yCoordinate + 0, textScale, "Movement = WASD or arrows",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);
		this.shiftExplanation = new MenuObject(xCoordinate, yCoordinate + 20, textScale, "SHIFT = Speed boost",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.attackExplanation = new MenuObject(xCoordinate, yCoordinate + 40, textScale, "Spacebar = Normal attack",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.specialAttackExplanation = new MenuObject(xCoordinate, yCoordinate + 60, textScale,
				"Q and ENTER = Special attack", MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.startGameCard = new MenuObject(startGameTile.getXCoordinate() - 120, startGameTile.getYCoordinate() - 50,
				1, null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);
		this.startGameCard.getMenuImages().get(0).setImageDimensions(400, 250);

		this.selectNormalAttackCard = new MenuObject(boardWidth / 2 - 100, startGameTile.getYCoordinate() - 50, 1, null,
				MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

		this.selectNormalAttackTitle = new MenuObject(selectNormalAttackCard.getXCoordinate() + 25,
				selectNormalAttackCard.getYCoordinate() + 30, (float) 1.5, "SELECT A NORMAL ATTACK",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.selectLaserbeam = new MenuObject(selectNormalAttackCard.getXCoordinate() + 50,
				selectNormalAttackCard.getYCoordinate() + 75, textScale, "Laserbeam", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_Laserbeam);

		this.selectFlamethrower = new MenuObject(selectNormalAttackCard.getXCoordinate() + 50,
				selectNormalAttackCard.getYCoordinate() + 125, textScale, "Flamethrower", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_Flamethrower);

		this.selectRocketLauncher = new MenuObject(selectNormalAttackCard.getXCoordinate() + 50,
				selectNormalAttackCard.getYCoordinate() + 175, textScale, "Rocket Launcher", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_Rocket_Launcher);

		this.selectNormalAttackIcon = new MenuObject(selectNormalAttackCard.getXCoordinate() + 150,
				selectNormalAttackCard.getYCoordinate() - 75, 1, null, MenuObjectEnums.Laserbeam_Icon,
				MenuFunctionEnums.NONE);

		this.selectNormalAttackIconHighlight = new MenuObject(selectNormalAttackIcon.getXCoordinate(),
				selectNormalAttackIcon.getYCoordinate(), 1, null, MenuObjectEnums.Highlight_Animation,
				MenuFunctionEnums.NONE);

		this.selectSpecialAttackCard = new MenuObject(boardWidth / 2 + 320, startGameTile.getYCoordinate() - 50, 1,
				null, MenuObjectEnums.Square_Card, MenuFunctionEnums.NONE);

		this.selectEMP = new MenuObject(selectSpecialAttackCard.getXCoordinate() + 50,
				selectSpecialAttackCard.getYCoordinate() + 75, textScale, "EMP", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_EMP);

		this.selectFirewall = new MenuObject(selectSpecialAttackCard.getXCoordinate() + 50,
				selectSpecialAttackCard.getYCoordinate() + 125, textScale, "Firewall", MenuObjectEnums.Text_Block,
				MenuFunctionEnums.Select_Firewall);

		this.selectSpecialAttackTitle = new MenuObject(selectSpecialAttackCard.getXCoordinate() + 25,
				selectSpecialAttackCard.getYCoordinate() + 30, (float) 1.5, "SELECT A SPECIAL ATTACK",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);

		this.selectSpecialAttackIcon = new MenuObject(selectSpecialAttackCard.getXCoordinate() + 150,
				selectSpecialAttackCard.getYCoordinate() - 75, 1, null, MenuObjectEnums.EMP_Icon,
				MenuFunctionEnums.NONE);

		this.selectSpecialAttackIconHighlight = new MenuObject(selectSpecialAttackIcon.getXCoordinate(),
				selectSpecialAttackIcon.getYCoordinate(), 1, null, MenuObjectEnums.Highlight_Animation,
				MenuFunctionEnums.NONE);

		selectNormalAttackIconHighlight.getMenuImages().get(0).getAnimation()
				.setX(selectNormalAttackIcon.getXCoordinate() - 2);
		selectNormalAttackIconHighlight.getMenuImages().get(0).getAnimation()
				.setY(selectNormalAttackIcon.getYCoordinate() - 2);

		selectSpecialAttackIconHighlight.getMenuImages().get(0).getAnimation()
				.setX(selectSpecialAttackIcon.getXCoordinate() - 2);
		selectSpecialAttackIconHighlight.getMenuImages().get(0).getAnimation()
				.setY(selectSpecialAttackIcon.getYCoordinate() - 2);

		if(!foundControllerBool) {
		this.foundController = new MenuObject(100,
				100, (float) 1.5, "NO CONTROLLER DETECTED. ONLY XBOX WIRELESS BLUETOOTH HAS BEEN TESTED.",
				MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);
		} else {
			this.foundController = new MenuObject(boardWidth / 2 - 100,
					100, (float) 1.5, "FOUND A CONTROLLER",
					MenuObjectEnums.Text_Block, MenuFunctionEnums.NONE);
		}
		
		offTheGridObjects.add(foundController);
		this.menuCursor.setSelectedMenuTile(startGameTile);

		// IN ORDER OF WHAT NEEDS TO BE SHOWN
		offTheGridObjects.add(wasdExplanation);
		offTheGridObjects.add(shiftExplanation);
		offTheGridObjects.add(attackExplanation);
		offTheGridObjects.add(specialAttackExplanation);
		offTheGridObjects.add(startGameCard);

		offTheGridObjects.add(titleImage);
		offTheGridObjects.add(selectNormalAttackCard);
		offTheGridObjects.add(selectSpecialAttackCard);
		offTheGridObjects.add(selectNormalAttackTitle);
		offTheGridObjects.add(selectSpecialAttackTitle);

		offTheGridObjects.add(selectNormalAttackIcon);
		offTheGridObjects.add(selectSpecialAttackIcon);

		offTheGridObjects.add(menuCursor);
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
		addTileToSecondRow(selectLaserbeam);
		addTileToSecondRow(selectFlamethrower);
		addTileToSecondRow(selectRocketLauncher);
		addTileToThirdRow(selectEMP);
		addTileToThirdRow(selectFirewall);

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

	private void updateActiveAttackIcons() {
		switch (PlayerStats.getInstance().getAttackType()) {
		case Flamethrower:
			selectNormalAttackIcon.changeImage(ImageEnums.Starcraft2_Flame_Turret);
			break;
		case Laserbeam:
			selectNormalAttackIcon.changeImage(ImageEnums.Starcraft2_Pulse_Laser);
			break;
		case Rocket:
			selectNormalAttackIcon.changeImage(ImageEnums.Starcraft2_Dual_Rockets);
			break;
		case Shotgun:
			break;
		}

		switch (PlayerStats.getInstance().getPlayerSpecialAttackType()) {
		case EMP:
			selectSpecialAttackIcon.changeImage(ImageEnums.Starcraft2_Electric_Field);
			break;
		case Firewall:
			selectSpecialAttackIcon.changeImage(ImageEnums.Starcraft2_Firebat_Weapon);
			break;
		case Rocket_Cluster:
			break;
		}
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
			// Shoddy fix, maybe an updateGameTick? Atleast this way it doesnt constantly check for
			// new icons
			updateActiveAttackIcons();
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
		
		
		
		
		readControllerState();

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
		if (animation.getCurrentFrame() != null) {
			g.drawImage(animation.getCurrentFrame(), animation.getXCoordinate(), animation.getYCoordinate(), this);
		}
	}

	/*------------------------------End of Drawing methods-------------------------------*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public Timer getTimer() {
		return this.timer;
	}

	private long lastMoveTime = 0;
	private static final long MOVE_COOLDOWN = 200; // 500 milliseconds

	public void readControllerState() {
	    if (controllers.getFirstController() != null) {
	        controllers.getFirstController().poll();
	        Component[] components = controllers.getFirstController().getComponents();

	        long currentTime = System.currentTimeMillis();
	        
	        for (int i = 0; i < components.length; i++) {
	            Component component = components[i];
	            String componentName = component.getName();
	            float componentValue = component.getPollData();

	            // Handle left joystick horizontal movement
	            if ((componentName.equals("x") || componentName.equals("z")) && (currentTime - lastMoveTime > MOVE_COOLDOWN)) {
	                if (componentValue > 0.5) {
	                    nextMenuTile();
	                    lastMoveTime = currentTime;
	                } else if (componentValue < -0.5) {
	                    previousMenuTile();
	                    lastMoveTime = currentTime;
	                }
	            }

	            // Handle left joystick vertical movement
	            if ((componentName.equals("y") || componentName.equals("rz")) && (currentTime - lastMoveTime > MOVE_COOLDOWN)) {
	                if (componentValue > 0.5) {
	                    nextMenuColumn();
	                    lastMoveTime = currentTime;
	                } else if (componentValue < -0.5) {
	                    previousMenuColumn();
	                    lastMoveTime = currentTime;
	                }
	            }

	            if (componentName.equals("0")) {
	                if (componentValue == 1.0) {
	                    selectMenuTile();
	                	updateActiveAttackIcons();
	                }
	            }
	            
	    		// Component 4 = Y
				// Component 1 = B
				// Component 3 = X
				// Component 0 = A

				// Component 10 & 11 zijn de middelste 2 knoppies, die voor pauzeren

				// Component 6 = linker schouder
				// Component 7 = rechter schouder

	            // Remaining components...
	        }
	    }
	}


}
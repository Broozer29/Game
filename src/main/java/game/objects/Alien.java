package game.objects;

public class Alien extends Enemy {

	public Alien(int x, int y, String enemyType, int currentBoardBlock, String direction) {
		super(x, y, "Alien", currentBoardBlock, direction);
		initAlien();
	}

	private void initAlien() {
		loadImage("Default Alien Spaceship");
		getImageDimensions();
	}

}

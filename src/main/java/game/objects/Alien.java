package game.objects;

public class Alien extends Enemy {

	public Alien(int x, int y, String enemyType, int currentBoardBlock) {
		super(x, y, "Alien", currentBoardBlock);
		initAlien();
	}

	private void initAlien() {
		loadImage("Default Alien Spaceship");
		getImageDimensions();
	}

}

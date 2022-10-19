package gameObjectes;

public class Alien extends Enemy {

	public Alien(int x, int y, String enemyType) {
		super(x, y, "Alien");
		initAlien();
	}

	private void initAlien() {
		loadImage("Alien spaceship");
		getImageDimensions();
	}

}

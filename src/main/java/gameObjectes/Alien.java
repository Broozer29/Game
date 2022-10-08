package gameObjectes;

public class Alien extends Sprite {

	private final int INITIAL_X = 400;

	public Alien(int x, int y) {
		super(x, y);
		initAlien();
	}

	private void initAlien() {
		loadImage("testimage");
		getImageDimensions();
	}

	public void move() {
		if (xCoordinate < 0) {
			xCoordinate = INITIAL_X;
		}
		xCoordinate -= 1;
	}
}

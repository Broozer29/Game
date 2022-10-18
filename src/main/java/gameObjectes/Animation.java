package gameObjectes;

public class Animation extends Sprite{

	float frameCount;
	float currentFrameCount;
	
	public Animation(int x, int y, String imageType) {
		super(x, y);
		this.initAnimation(imageType);
	}
	
	protected void initAnimation(String imageType) {
		loadImage(imageType);
		setFrameCount(imageType);
		getImageDimensions();
	}
	
	//Duration length of the frames
	private void setFrameCount(String imageType) {
		switch(imageType) {
			case("Impact Explosion One"):
				this.frameCount = 25;
				this.centerAnimationFrame();
				return;
		}
	}
	
	//Centers the animation a bit further inwards to the collision spot
	private void centerAnimationFrame() {
		this.setX(xCoordinate + (this.getWidth() / 2));
		this.setY(yCoordinate - (this.getHeight() / 2));
	}
	
	public float getFrameCount() {
		return this.frameCount;
	}
	
	//Updates the current frame of the animation, and sets it invisible if it's fully played out
	public void updateFrameCount() {
		this.currentFrameCount += 1;
		if (this.currentFrameCount >= frameCount) {
			this.setVisible(false);
		}
	}


}

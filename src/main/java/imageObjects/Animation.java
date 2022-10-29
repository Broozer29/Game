package imageObjects;

public class Animation extends Sprite{

	float frameCount;
	float currentFrameCount;
	String animationType;
	
	public Animation(int x, int y, String imageType) {
		super(x, y);
		this.initAnimation(imageType);
		this.animationType = imageType;
	}
	
	protected void initAnimation(String imageType) {
		loadImage(imageType);
		setFrameCount(imageType);
		getImageDimensions();
		centerAnimationFrame();
	}
	
	//Duration length of the frames
	private void setFrameCount(String imageType) {
		switch(imageType) {
			case("Impact Explosion One"):
				this.frameCount = 12;
				return;
			case("Player Engine"):
				this.frameCount = 30;
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
	
	public float getCurrentFrameCount() {
		return this.currentFrameCount;
	}
	
	public String getAnimationType() {
		return this.animationType;
	}
	
	//Updates the current frame of the animation, and sets it invisible if it's fully played out
	public void updateFrameCount() {
		this.currentFrameCount += 1;
		if (this.currentFrameCount >= frameCount) {
			this.setVisible(false);
		}
	}

	public void refreshAnimation(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.currentFrameCount = 0;
		this.setFrameCount(animationType);
		this.setVisible(true);
	}


}

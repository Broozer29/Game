package visualobjects.SpriteConfigurations;

public class SpriteAnimationConfiguration {

    private SpriteConfiguration spriteConfiguration;

    private int frameDelay;
    private boolean infiniteLoop;

    public SpriteAnimationConfiguration (SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop) {
        this.spriteConfiguration = spriteConfiguration;
        this.frameDelay = frameDelay;
        this.infiniteLoop = infiniteLoop;
    }

    public SpriteConfiguration getSpriteConfiguration () {
        return spriteConfiguration;
    }

    public void setSpriteConfiguration (SpriteConfiguration spriteConfiguration) {
        this.spriteConfiguration = spriteConfiguration;
    }

    public int getFrameDelay () {
        return frameDelay;
    }

    public void setFrameDelay (int frameDelay) {
        this.frameDelay = frameDelay;
    }

    public boolean isInfiniteLoop () {
        return infiniteLoop;
    }

    public void setInfiniteLoop (boolean infiniteLoop) {
        this.infiniteLoop = infiniteLoop;
    }
}

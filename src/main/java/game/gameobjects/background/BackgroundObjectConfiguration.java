package game.gameobjects.background;

public class BackgroundObjectConfiguration {

    private int depthLevel;
    private BGOEnums bgoType;

    public BackgroundObjectConfiguration (int depthLevel, BGOEnums bgoType) {
        this.depthLevel = depthLevel;
        this.bgoType = bgoType;
    }

    public int getDepthLevel () {
        return depthLevel;
    }

    public void setDepthLevel (int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public BGOEnums getBgoType () {
        return bgoType;
    }

    public void setBgoType (BGOEnums bgoType) {
        this.bgoType = bgoType;
    }
}

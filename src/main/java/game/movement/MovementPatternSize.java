package game.movement;

public enum MovementPatternSize {
    SMALL(100,100,30,1,70,30),
    MEDIUM(200,200,50,2,120,50),
    LARGE(300,300,80,3,170,70);
    private int diamondWidth;
    private int diamondHeight;
    private int stepsBeforeBounceInOtherDirection;
    private int radiusIncrement;

    private int primaryDirectionStepAmount;
    private int secondaryDirectionStepAmount;

    MovementPatternSize (int diamondWidth, int diamondHeight, int stepsBeforeBounceInOtherDirection, int radiusIncrement, int primaryDirectionStepAmount, int secondaryDirectionStepAmount) {
        this.diamondWidth = diamondWidth;
        this.diamondHeight = diamondHeight;
        this.stepsBeforeBounceInOtherDirection = stepsBeforeBounceInOtherDirection;
        this.radiusIncrement = radiusIncrement;
        this.primaryDirectionStepAmount = primaryDirectionStepAmount;
        this.secondaryDirectionStepAmount = secondaryDirectionStepAmount;
    }

    public int getDiamondWidth () {
        return diamondWidth;
    }

    public int getDiamondHeight () {
        return diamondHeight;
    }

    public int getStepsBeforeBounceInOtherDirection () {
        return stepsBeforeBounceInOtherDirection;
    }

    public int getRadiusIncrement () {
        return radiusIncrement;
    }

    public int getPrimaryDirectionStepAmount () {
        return primaryDirectionStepAmount;
    }

    public int getSecondaryDirectionStepAmount () {
        return secondaryDirectionStepAmount;
    }
}

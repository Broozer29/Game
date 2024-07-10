package game.gameobjects.friendlies;

public enum FriendlyObjectEnums {



    Missile_Drone(false,true);

    private final boolean boxCollision;
    private final boolean permanentObject;

    FriendlyObjectEnums(boolean boxCollision, boolean permanentObject){
        this.boxCollision = boxCollision;
        this.permanentObject = permanentObject;
    }

    public boolean isBoxCollision(){
        return boxCollision;
    }

    public boolean isPermanentObject(){return permanentObject;}
}

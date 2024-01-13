package game.objects.friendlies;

public enum FriendlyObjectEnums {



    Missile_Drone(false);

    private final boolean boxCollision;

    FriendlyObjectEnums(boolean boxCollision){
        this.boxCollision = boxCollision;
    }

    public boolean isBoxCollision(){
        return boxCollision;
    }

}

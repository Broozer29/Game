package game.objects.enemies;

public enum EnemyEnums {
    Alien_Bomb(50,50),
    Seeker(50,50),
    Tazer(50,50),
    Energizer(50,50),
    Bulldozer(50,50),
    Flamer(50,50),
    Bomba(50,50),
    Random(50,50),
    Alien(50,50);

    private final int width;
    private final int height;

    EnemyEnums(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //Returns the distance that enemies should have from each other for spawning formations
    public int getFormationWidthDistance() {
        return width;
    }

    public int getFormationHeightDistance() {
        return height;
    }
}
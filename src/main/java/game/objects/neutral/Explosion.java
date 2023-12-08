package game.objects.neutral;

import java.util.ArrayList;
import java.util.List;

import game.objects.GameObject;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class Explosion extends GameObject {
    private List<Sprite> skipCollision = new ArrayList<Sprite>();

    public Explosion (int x, int y, float scale, SpriteAnimation animation, float damage, boolean friendly) {
        super(x, y, scale);
        this.friendly = friendly;
        this.animation = animation;
        this.damage = damage;
    }


    public void addCollidedSprite (Sprite sprite) {
        if (!this.skipCollision.contains(sprite)) {
            this.skipCollision.add(sprite);
        }
    }

    public List<Sprite> getCollidedSprites () {
        return this.skipCollision;
    }

    //Use this instead of shouldDealDamage
    public void updateAllowedToDealDamage () {
        if (this.animation.getCurrentFrame() < 5 || this.damage == 0) {
            setAllowedToDealDamage(false);
        }
    }

}
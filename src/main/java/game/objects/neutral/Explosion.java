package game.objects.neutral;

import java.util.ArrayList;
import java.util.List;

import game.objects.GameObject;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class Explosion extends GameObject {


    public Explosion (SpriteAnimationConfiguration spriteAnimationConfiguration, ExplosionConfiguration explosionConfiguration) {
        super(spriteAnimationConfiguration);
        this.friendly = explosionConfiguration.isFriendly();
        this.damage = explosionConfiguration.getDamage();
        this.allowedToDealDamage = explosionConfiguration.isAllowedToDealDamage();
        this.setObjectType("Explosion");
    }


    public void addCollidedSprite (GameObject sprite) {
        if (!this.skipCollision.contains(sprite)) {
            this.skipCollision.add(sprite);
        }
    }

    public List<GameObject> getCollidedSprites () {
        return this.skipCollision;
    }

    public void updateAllowedToDealDamage () {
        if (this.animation.getCurrentFrame() > 5 || this.damage == 0) {
            setAllowedToDealDamage(false);
        }
    }

    private List<GameObject> skipCollision = new ArrayList<GameObject>();
    public boolean dealtDamageToTarget(GameObject objectToCheck){
        if(skipCollision.contains(objectToCheck)){
            return true;
        }
        return false;
    }

}
package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;

public class SpaceShipReviver {


    private int reviveRadius = 200;
    private SpaceShip spaceShip;
    private float charge = 0.0f;
    private float maxAlphaTransparancy = 0.8f;
    private float damagePerHeal = 0.05f;
    private float chargeRegenPerTick = 0.005f;
    private boolean isActive = true;

    public SpaceShipReviver(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
        chargeRegenPerTick = Math.max(chargeRegenPerTick * (1 - (PlayerManager.getInstance().getPlayerCount() * 0.15f)), 0.002f ); //-15% heal rate per player
    }

    public void increaseCharge(SpaceShip otherSpaceShip) {
        this.isCurrentlyReviving = true;
        charge += chargeRegenPerTick;
//        otherSpaceShip.takeDamage(damagePerHeal);
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public boolean isWithinRange(SpaceShip otherSpaceShip) {
        //isNearby is similar but does rectangle collision instead of circle collision
        if(!CollisionDetector.getInstance().isNearby(spaceShip, otherSpaceShip, reviveRadius)){
            return false;
        }

        Point centerPointOfCircle = spaceShip.getCurrentCenterLocation();
        Point otherCenter = otherSpaceShip.getCurrentCenterLocation();

        int dx = otherCenter.getX() - centerPointOfCircle.getX();
        int dy = otherCenter.getY() - centerPointOfCircle.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= reviveRadius;
    }

    public int getReviveRadius() {
        return reviveRadius;
    }

    public void setReviveRadius(int reviveRadius) {
        this.reviveRadius = reviveRadius;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public float getMaxAlphaTransparancy() {
        return maxAlphaTransparancy;
    }

    public void setMaxAlphaTransparancy(float maxAlphaTransparancy) {
        this.maxAlphaTransparancy = maxAlphaTransparancy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    private boolean isCurrentlyReviving = false;
    public void attemptRevive() {
        if(isCurrentlyReviving && !isActive && this.charge >= 1.0f){
            spaceShip.reviveSpaceShip();
            PlayerManager.getInstance().reviveSpaceShip(spaceShip);
            this.isActive = false;
        }

        this.charge -= chargeRegenPerTick * 0.75f;
        this.isCurrentlyReviving = false;
    }
}

package net.riezebos.bruus.tbd.game.gameobjects.neutral.interactable;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.SpriteMover;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

import java.util.ArrayList;

public class InteractableManager {

    private static InteractableManager instance = new InteractableManager();
    private ArrayList<Interactable> interactables = new ArrayList<>();
    private PerformanceLogger performanceLogger;
    
    private InteractableManager() {
        this.performanceLogger = new PerformanceLogger("Interactable Manager");
    }

    public static InteractableManager getInstance() {
        return instance;
    }

    public void updateGameTick(){
        for(Interactable interactable : interactables) {
            interactable.move();
            checkCollisionWithPlayer(interactable);
        }
        removeInvisibleInteractables();
    }

    private void removeInvisibleInteractables() {
        interactables.removeIf(interactable -> {
            if (!interactable.isVisible()) {
                interactable.deleteObject();
                return true;
            }
            return false;
        });
    }

    private void checkCollisionWithPlayer(Interactable interactable){
        CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(PlayerManager.getInstance().getSpaceship(), interactable);
        if(collisionInfo != null && collisionInfo.isCollided()){
            interactable.activateObject();
            interactable.setVisible(false);
        }
    }

    public void addInteractable(Interactable interactable) {
        if(!this.interactables.contains(interactable)){
            this.interactables.add(interactable);
            if(interactable.getAnimation() != null){
                AnimationManager.getInstance().addLowerAnimation(interactable.getAnimation());
            }
        }
    }


    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}

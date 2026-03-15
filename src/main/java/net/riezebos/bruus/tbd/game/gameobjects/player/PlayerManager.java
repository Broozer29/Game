package net.riezebos.bruus.tbd.game.gameobjects.player;

import net.riezebos.bruus.tbd.controllerInput.ControllerInputReader;
import net.riezebos.bruus.tbd.controllerInput.ControllerManager;
import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectInterface;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.guiboards.BoardManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PlayerManager {

    private static PlayerManager instance = new PlayerManager();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private GameState gameState = GameState.getInstance();


    private SpaceShip spaceship;  //todo spaceship attribuut verwijderen want dit is residu van singleplayer
    private List<SpaceShip> allSpaceShips = new ArrayList<>();
    private boolean initializedSpaceShips = false;


    private PerformanceLogger performanceLogger;

    private PlayerManager() {
        this.performanceLogger = new PerformanceLogger("Player Manager");
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    // Called when a game instance needs to be deleted and the manager needs to be
    // reset.
    public void resetManager() {
        for (SpaceShip spaceship : allSpaceShips) {
            if (spaceship != null) {
                for (EffectInterface effectInterface : spaceship.getEffects()) {
                    effectInterface.removeEffect(spaceship);
                }
                spaceship.deleteObject();
            }
        }
        this.initializedSpaceShips = false;
        this.allSpaceShips.clear();
        this.spaceship = null;
        hasStartedDyingScene = false;
        performanceLogger.reset();
    }

    public void createSpaceShip() {
        for(int i = 0; i < ControllerManager.getInstance().getControllerInputReaders().size(); i++){
            initSpaceShip(ControllerManager.getInstance().getControllerInputReaders().get(i), i);
        }

        //we found no controllers so create a singular spaceship for keyboard input instead, multiplayer effectively disabled
        if(allSpaceShips.isEmpty()){
            initSpaceShip();
        }

        this.initializedSpaceShips = true;
    }

    public void updateGameTick() {
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Ship Movement", this::updateSpaceShipMovement);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Player Health", this::checkPlayerHealth);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Update SpaceShip", this::updateSpaceShipGameTicks);
    }

    private void updateSpaceShipGameTicks(){
        for(SpaceShip spaceShip : allSpaceShips){
            spaceShip.updateGameTick();
        }
    }


    public SpaceShip getSpaceship() {
        if(allSpaceShips.isEmpty()){
            return null;
        }
        return allSpaceShips.get(0); //dirty hack for backwards compatibility with singleplayer
    }

    public SpaceShip getRandomSpaceShip(){
        return allSpaceShips.get((int)(Math.random()*allSpaceShips.size()));
    }

    public SpaceShip getClosestSpaceShip(int xCoordinate, int yCoordinate){
        //probleem: als de laatste speler sterft moet hij eigenlijk nog bestaan voor de dying animatie, maar hij word verwijderd uit allSpaceShips.
        if(allSpaceShips.size() == 1){
            return allSpaceShips.get(0); //als er 1 speler is, gewoon de speler returnen.
        }

        return allSpaceShips.stream()
                .min(Comparator.comparingInt(s ->
                        Math.abs(s.getCenterXCoordinate() - xCoordinate) +
                                Math.abs(s.getCenterYCoordinate() - yCoordinate)
                ))
                .orElse(null);
    }
    public SpaceShip getClosestSpaceShip(GameObject gameObject){
        return this.getClosestSpaceShip(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
    }

    public SpaceShip getFurthestSpaceShip(int xCoordinate, int yCoordinate){
        if(allSpaceShips.size() == 1){
            return allSpaceShips.get(0); //als er 1 speler is, gewoon de speler returnen.
        }

        return allSpaceShips.stream()
                .max(Comparator.comparingInt(s ->
                        Math.abs(s.getCenterXCoordinate() - xCoordinate) +
                                Math.abs(s.getCenterYCoordinate() - yCoordinate)
                ))
                .orElse(null);
    }
    public SpaceShip getFurthestSpaceShip(GameObject gameObject){
        return this.getFurthestSpaceShip(gameObject.getCenterXCoordinate(), gameObject.getCenterYCoordinate());
    }

    public List<SpaceShip> getAllSpaceShips(){
        return allSpaceShips;
    }

    private void checkPlayerHealth() {
        for(SpaceShip spaceShip : allSpaceShips){
            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairBot) != null) {
                PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairBot).applyEffectToObject(spaceShip);
            }


            if(spaceShip.getCurrentHitpoints() <= 0){

                AnimationManager.getInstance().addUpperAnimation(spaceShip.getDestructionAnimation());

                if(spaceShip.isVisible()) { //to ensure the audio only plays once
                    AudioManager.getInstance().addAudio(AudioEnums.Destroyed_Explosion);
                }

                spaceShip.setVisible(false);
                spaceShip.setImmune(true); //make it immune, shouldn't do anything except prevent weird behaviour

                if(spaceShip.getDestructionAnimation() != null){
                    spaceShip.getDestructionAnimation().setCenterCoordinates(spaceShip.getCenterXCoordinate(), spaceShip.getCenterYCoordinate());

                    if (spaceShip.getExhaustAnimation() != null) {
                        animationManager.getLowerAnimations().remove(spaceShip.getExhaustAnimation());
                    }

                    if (spaceShip.getAnimation() != null) {
                        spaceShip.getAnimation().setVisible(false);
                    }
                }
            }
        }


        //Remove the spaceship and conversely the player if his spaceship is not visible (dead)
        allSpaceShips.removeIf(spaceShip -> !spaceShip.isVisible() && allSpaceShips.size() > 1);


        //if there are no players left, we have created them AND we are currently playing, the player "died"
        if(allSpaceShips.size() == 1 && (allSpaceShips.get(0).getCurrentHitpoints() <= 0 || !allSpaceShips.get(0).isVisible()) && initializedSpaceShips && gameState.getGameState() == GameStatusEnums.Playing){
            //enter the dying animation for the final player
            allSpaceShips.get(0).getDestructionAnimation().setFrameDelay(45);
            gameState.setGameState(GameStatusEnums.Dying);
        }
    }

    private void updateSpaceShipMovement() {
        for(SpaceShip spaceShip : allSpaceShips){
            if(spaceShip.isVisible()) {
                spaceShip.move();
            }
        }

        // Check collisions between all pairs efficiently (avoid duplicate checks)
        for (int i = 0; i < allSpaceShips.size(); i++) {
            SpaceShip spaceShip1 = allSpaceShips.get(i);
            for (int j = i + 1; j < allSpaceShips.size(); j++) {
                SpaceShip spaceShip2 = allSpaceShips.get(j);
                CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(spaceShip1, spaceShip2);
                if (collisionInfo != null) {
                    // Apply knockback to both ships
                    spaceShip1.applyKnockback(collisionInfo, 9);
                    spaceShip1.takeDamage(2.5f * (1 - PlayerStats.getInstance().getCollisionDamageReduction()));
                    spaceShip2.applyKnockback(collisionInfo, 9);
                    spaceShip2.takeDamage(2.5f * (1 - PlayerStats.getInstance().getCollisionDamageReduction()));
                }
            }
        }
    }

    private void initSpaceShip() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 10);
        spriteConfiguration.setyCoordinate(DataClass.getInstance().getWindowHeight() / 2);
        spriteConfiguration.setScale(0.7f * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(ImageEnums.Player_Spaceship_Model_3); //placeholder, gets overwritten anyway
        allSpaceShips.add(new SpaceShip(spriteConfiguration));
    }

    private void initSpaceShip(ControllerInputReader controllerInputReader, int index) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(DataClass.getInstance().getWindowWidth() / 10);

        if(index % 2 == 0){
            spriteConfiguration.setyCoordinate((DataClass.getInstance().getWindowHeight() / 2) - index * 100);
        } else {
            spriteConfiguration.setyCoordinate((DataClass.getInstance().getWindowHeight() / 2) + index * 100);
        }
        spriteConfiguration.setScale(0.7f * DataClass.getInstance().getResolutionFactor());
        spriteConfiguration.setImageType(ImageEnums.Player_Spaceship_Model_3); //placeholder, gets overwritten anyway
        SpaceShip spaceShip = new SpaceShip(spriteConfiguration, controllerInputReader);
        spaceShip.setAttackSpeed(PlayerStats.getInstance().getBaseAttackSpeed());
        this.allSpaceShips.add(spaceShip);
    }


    //Toggle boolean introduced because of multiplayer, since this method is constantly called
    private boolean hasStartedDyingScene = false;
    private double milliSecondsSinceDyingBegan = 0;
    public void startDyingScene() {
        if (gameState.getGameState() == GameStatusEnums.Dying && !hasStartedDyingScene) {
            hasStartedDyingScene = true;

            BoardManager.getInstance().getGameBoard().setDrawnTimerDelay(75); //create the slow motion
            milliSecondsSinceDyingBegan = System.currentTimeMillis();
        }


        if(hasStartedDyingScene && System.currentTimeMillis() >= milliSecondsSinceDyingBegan + 5000){ //5 seconds of slow motion dying
            //todo Stuivie logica, momenteel uitgezet want ik weet niet wat stuivie moet doen wanneer er meerdere spelers zijn
//            if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Stuivie) != null) {
//                StuiversBestFriend stuiversBestFriend = (StuiversBestFriend) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Stuivie);
//                if (stuiversBestFriend != null && !stuiversBestFriend.isHasActivatedThisRound()) {
//                    spaceship.reviveSpaceShip();
//                    stuiversBestFriend.applyEffectToObject(spaceship);
//                    BoardManager.getInstance().getGameBoard().setDrawnTimerDelay(gameState.getDELAY());
//                    gameState.setGameState(GameStatusEnums.Playing);
//                } else { //we already have been revived, we die this time fr fr
//                    gameState.setGameState(GameStatusEnums.Dead);
//                    PlayerInventory.getInstance().resetInventory();
//                }
//            }

            gameState.setGameState(GameStatusEnums.Dead);
            PlayerInventory.getInstance().resetInventory();
        }
    }

    //todo check, dit wordt gebruikt door homingpathfinder maar word hominhpathfinder uberhaupt gebruikt? denk het niet en zo niet, verwijderen
    public LinkedList<Integer> getNearestFriendlyHomingCoordinates() {
        LinkedList<Integer> playerCoordinatesList = new LinkedList<>();
        playerCoordinatesList.add(0, spaceship.getCenterXCoordinate());
        playerCoordinatesList.add(1, spaceship.getCenterYCoordinate());
        return playerCoordinatesList;
    }

    public PerformanceLogger getPerformanceLogger() {
        return this.performanceLogger;
    }

    public int getPlayerCount() {
        if(ControllerManager.getInstance().getControllerInputReaders().isEmpty()){
            return 1; //als er geen controllers zijn, is er maar 1 speler: keyboard
        } else {
            return ControllerManager.getInstance().getControllerInputReaders().size(); //voor elke controller: 1 speler (levend of dood)
        }
    }
}
package game.movement.deprecatedpathfinderconfigs;

import game.movement.MovementConfiguration;
import game.movement.pathfinders.*;
import game.gameobjects.GameObject;

public class PathFinderConfigCreator {

    public static PathFinderConfig createConfig (GameObject sprite, MovementConfiguration moveConfig) {
        PathFinderConfig config = null;
        if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
            config = new OrbitPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof RegularPathFinder) {
            config = new RegularPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof HomingPathFinder) {
            config = new HomingPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof BouncingPathFinder) {
            config = new BouncingPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof DiamondShapePathFinder) {
            config = new DiamondShapePathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof ZigZagPathFinder) {
            config = new ZigZagPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof SpiralPathFinder) {
            config = new SpiralPathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof TrianglePathFinder) {
            config = new TrianglePathFinderConfig(sprite, moveConfig);
        } else if (moveConfig.getPathFinder() instanceof StraightLinePathFinder) {
            config = new StraightLinePathFinderConfig(sprite, moveConfig);
        } else if(moveConfig.getPathFinder() instanceof HoverPathFinder){
            config = new HoverPathFinderConfig(sprite, moveConfig);
        }
        return config;
    }
}

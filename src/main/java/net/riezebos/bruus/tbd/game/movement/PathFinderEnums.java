package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.pathfinders.*;

public enum PathFinderEnums {
    Bouncing {
        public PathFinder createInstance() {
            return new BouncingPathFinder();
        }
    },
    DiamondShape {
        @Override
        public PathFinder createInstance () {
            System.out.println("MAJOR ERROR WILL OCCUR HERE");
            return null;
        }

        public PathFinder createInstance(int loopamount) {
            return new DiamondShapePathFinder(loopamount);
        }
    },
    Homing {
        public PathFinder createInstance() {
            return new HomingPathFinder();
        }
    },
    Hover {
        public PathFinder createInstance() {
            return new HoverPathFinder();
        }
    },
    Orbit {
        public PathFinder createInstance () {
            System.out.println("MAJOR ERROR WILL OCCUR HERE");
            return null;
        }

        public PathFinder createInstance(GameObject target) {
            return new OrbitPathFinder(target, 85, 300, 0);
        }
    },
    Regular {
        public PathFinder createInstance() {
            return new RegularPathFinder();
        }
    },
    Spiral {
        public PathFinder createInstance() {
            return new SpiralPathFinder();
        }
    },
    StraightLine {
        public PathFinder createInstance() {
            return new StraightLinePathFinder();
        }
    },
    Triangle {
        public PathFinder createInstance() {
            return new TrianglePathFinder();
        }
    },
    ZigZag {
        public PathFinder createInstance() {
            return new ZigZagPathFinder();
        }
    };

    public abstract PathFinder createInstance();
}
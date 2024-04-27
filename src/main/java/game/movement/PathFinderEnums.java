package game.movement;

import game.movement.pathfinders.*;
import game.objects.GameObject;

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

        public PathFinder createInstance(GameObject target, int radius, int totalFrames, double offset) {
            return new OrbitPathFinder(target, radius, totalFrames, offset);
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
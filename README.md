# Game
A horizontal shoot-em-up roguelike made in Java.

# How to install
1.) Clone the repository to your intellij IDE
2.) Edit the runtime configuration to use the following arguments
-Djava.library.path=src/main/resources/libraries  -Xms4g -Xmx8g -XX:+UseG1GC

Currently, only integration with apple music on MacOS is implemented. Windows users are out of luck as of now.

# How to use
1.) Enable bluetooth and connect an Xbox360 controller (the only controller that has been tested, others might not have the correct mappings)
2.) Start the application from the IDE

Dependencies:
- Java 21
- Maven is used for smaller dependencies
  - Java Swing/JavaFX
  - GSON
  - JInput

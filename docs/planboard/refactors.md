# Refactors

## Sprite/SpriteAnimation
SpriteAnimation extends Sprite. However, GameObjects extend Sprite AND have a SpriteAnimation.
So now the dependency is this:

Sprite -> SpriteAnimation

Sprite -> GameObject


This is just bad design. As almost all GameObjects have a SpriteAnimation, it means that the Sprite that GameObject extends is almost always overriden with a SpriteAnimation, thus rendering it's existence moot. It's a triangle of depencies.

The reason this is tedious to refactor is because the way the UIObjects are made, they have a Sprite. It means I need to re-order the base resource role of Sprite that it has had in 4+ years.

Ideal solution: GameObject is the parent of a Sprite or SpriteAnimation. Any other UIObject or "child" of a Sprite either become the parent or inherict a shared parent to allow for backwards compatability with the existing codebase.


## All boards: horrible code quality
All boards under src/main/java/net/riezebos/bruus/tbd/guiboards/boards have massive code duplication.
Specifically, the Grid system, navigating the grid, reading and executing controller input, displaying objects/text on screen. The GameBoard is an exception amongst the others since it also has a lot of gameplay logic.
Some boards even have the left/right and up/down navigation methods reversed since the screen/grid is build from a row perspective instead of columns.
It's a mess, not scalable and horribly inefficient. I do like the seperation between board and boardcreator and want to keep this boardcreators as is.

## GameBoard: Drawing text on screen
An unnecessary amount of code in GameBoard.java is used to draw text on screen. It's incredibly verbose, inefficient and horrible to maintain/read.

## SpriteAnimation.getCurrentFrameImage() takes a boolean parameter
There is no reason for this parameter to exist, as it is used for updating the index, this can just be done in a different method

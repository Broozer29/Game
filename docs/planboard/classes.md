# Classes
Captain, Carrier, Firefighter and Mutalisk: their abilities and how each class plays.

## Bugs
- Firefighter: Infernal Pre-Igniter multiplies flamethrower damage again on every game tick, so damage grows the longer fire is held
  - FlameThrower.java `updateSpecialAttack` calls `InfernalPreIgniter.applyEffectToObject` every tick, which does `setDamage(getDamage() * (1 + scalingFactor * quantity))`
  - Open question: should it apply once when the flamethrower starts, or grow over time up to a cap?
- Firefighter: Corrosive Oil never gives back the armor it removes
- Firefighter: the flamethrower keeps hitting enemies after the player dies
  - FlameThrower.java calls `setVisible(false)`, but a game object counts as visible based on its animation, and `setVisible` doesn't change the animation

## Balance

# Bosses
Bosses and the final boss: attacks, phases, difficulty and rewards.

## Bugs
- The white battlecruiser boss sometimes crashes the game
- Tracking laser beams never notice when the object they fire from moves
  - TrackingLaserBeam.java:40-47 stores the new origin position first and then compares it with itself, so the "has the origin moved" check is never true

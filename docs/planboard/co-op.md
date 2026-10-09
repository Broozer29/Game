# Co-op
Playing together: multiple players, reviving and shared progress.

## Features
- Give each co-op player a different colour

## Bugs
- When two ships die in the same tick, both are removed and the game never ends
  - PlayerManager.java:185 checks `allSpaceShips.size() > 1` inside `removeIf`, which still sees both ships while deciding, so both are removed; the dying check needs exactly one ship left, so it never fires
  - Every key release then throws, because `GameBoard`'s key listener does `getAllSpaceShips().get(0)`
  - Fix: count the survivors first, or switch to Dying when the list is empty

# Invariants — Game

## Invariants

| invariant | consequence |
| --- | --- |
| Every `DevTestSettings` switch is `false` in committed code | the `dev test switches off` gate check must pass before any commit that touches `DevTestSettings.java` |
| Every Plan item for Game work names its `docs/planboard/` entry | when either copy changes, the other is updated in the same session; closing the Plan item deletes the planboard entry |

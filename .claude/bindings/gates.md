# Gates — Game

## Gate tiers

| tier | reviewers |
| --- | --- |
| full | `design-review, an independent subagent` |
| light | `one reviewer: design-review` |
<!-- There is no Java standards skill, so no mechanical code reviewer exists; design-review is the only reviewer. -->

## Commands

| purpose | command |
| --- | --- |
| full build | `mvn -q compile` |
| production files | `src/main/java/**/*.java` |
<!-- Scoped test and line counts are absent on purpose: there are no automated tests (a change is checked by playing the affected part of the game) and no line-count script. -->

## Gate checks

| check | command |
| --- | --- |
| dev test switches off | `grep -nE "boolean [A-Za-z]+ = true" src/main/java/net/riezebos/bruus/tbd/DevTestSettings.java` (passes when it prints nothing) |

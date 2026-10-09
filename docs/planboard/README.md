# Planboard

Everything we've thought of for the game: bugs, features, balance tweaks and ideas. One file per
area of the game.

## How to write in it

- Put an entry in the file for its area, under one of these headings: `## Bugs`, `## Features`,
  `## Balance` or `## Ideas`. Add the heading if the file doesn't have it yet.
- An entry is one line starting with `- `. Indented `- ` lines under it are notes.
- Bruus replies to an entry with a note block under it, in Dutch or English:
  ```
    - Bruus insight:
      - **Mening:** <opinion>
      - **Mijn voorstel:** <proposal>
  ```
  Any indented note starting with `- Bruus` or `- bruus` counts as a reply.
- Free text is allowed anywhere: a loose line, a paragraph, or a heading other than the four
  above. It is read as a note at the place it stands, not as an entry, and it does not end the
  section it sits in.
- When an entry is done, or we decide against it, delete it.
- If an entry fits no area, make a new file for it: lowercase-with-dashes name, a `# Title` line,
  and a one-line description of what belongs there.

Example:

```
# Bosses
Bosses and the final boss: attacks, phases, difficulty and rewards.

## Bugs
- Final boss mines don't despawn after it dies

## Ideas
- A boss that copies the player's last bought item
  - Could pick from the last three instead
```

## Reading it

`bash .claude/scripts/planboard.sh <bugs|features|balance|ideas|all> [--unanswered] [area ...]`

For example, `bash .claude/scripts/planboard.sh bugs classes bosses` prints the bugs in Classes and
Bosses. Without areas it reads every file. Free text prints as `note:` lines where it stands.
`--unanswered` leaves out every entry that has a reply from Bruus.

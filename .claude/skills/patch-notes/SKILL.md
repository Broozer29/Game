---
name: patch-notes
description: Writes the player patch notes for a release of the game in Bruus's patch-note style, from every change between the previous release and the new one, and records the release. Use when the user asks for patch notes, release notes or a changelog for players, or says a release is coming or was just made.
---

# Patch Notes

Patch notes tell players what changed between two releases. The work has two parts:

1. **Find the changes.** Build a complete fact list of what differs between the previous release and
   the new one, with every old and new value.
2. **Write the notes.** Turn the fact list into patch notes in Bruus's style.

Release bookkeeping comes before and after: the release log says where the previous release was,
and a new row is added once the release is made.

## Where things live

Read `.claude/bindings/changelog.md`:

| row | holds |
| --- | --- |
| `release-log` | the table of releases: name, date, release commit, patch notes file |
| `patch-notes-folder` | the folder the patch notes files go in |
| `release-branch` | the branch players get their builds from |
| `dev-changelog` | the developers' running changelog |

If the binding or a row is absent, print a one-line notice. For a missing release log, ask the user
for the start commit. For a missing folder, write the notes into the scratchpad and say so.

## Choosing the range

- **Start:** the release commit of the last row in the release log.
- **End:** the commit being released. Run `git fetch` first. The default end is the tip of the
  `release-branch` on `origin`, because that is what players get. Never end on a work branch: work
  there that is not on the release branch yet is not in the release.
- State the range (both commits, both dates) to the user before part 1 starts.

## Part 1: find the changes

1. List the net changes: `git diff -M --stat <start> <end>`. Work only from the net difference
   between the two commits. A change that was made and later undone is absent from it, so it is
   left out. A change made in several steps is described once, by its end result.
2. Use `git log --format='%h %an %ad %s' <start>..<end> -- <file>` and the `dev-changelog` only to
   understand what a change is.
3. Split the changed files into groups by game area, with no file in two groups. Dispatch one agent
   per group with the Agent tool, `model: sonnet`, using the brief in
   [templates/fact-brief.md](templates/fact-brief.md). Each agent writes its facts to its own file
   in the scratchpad and returns only counts and open questions.
4. Merge the fact files into one fact list. The fact list is a working file in the scratchpad, not
   a deliverable.

Each fact holds:

- the area (items, classes, enemies, bosses, controls, performance, soundtracks, general, bug fix,
  developer)
- the subject, by the name the player sees in the game
- the kind: new, changed, rework, removed or fixed
- every changed value as old and new
- for a rework, the in-game description before and after, with its numbers filled in
- the evidence: files and the diff lines that prove it

> **When** a fact goes into the fact list, **run this checklist** — check every item, top to
> bottom, skipping none:
> - [ ] The direction of every value was checked against how the code uses it. A cooldown or a
>   delay that goes up makes things slower, even when the field is called a speed.
> - [ ] The subject uses the in-game display name, not a class or field name.
> - [ ] The thing is enabled at the end commit. An item that is disabled at the end is reported as
>   removed if players had it at the start, and left out if it is new.
> - [ ] Something in play reads the changed code. A value or flag that nothing reads changes
>   nothing for players.
> - [ ] The change is not behind a dev test switch. A switch-only change goes to the developer area
>   or is left out.
>
> **If ANY item fails**, you MUST correct the fact or drop it before it goes in. **Zero exceptions.**

**Suspected bugs** found along the way, such as a description that disagrees with the code or a
value that moves the opposite way from its item's promise, are reported to the user in chat. They
never go into the patch notes.

## Part 2: write the notes

Bruus's own patch notes are the style reference:

- [examples/balance-update.md](examples/balance-update.md)
- [examples/rework-and-fixes.md](examples/rework-and-fixes.md)
- [examples/class-buffs.md](examples/class-buffs.md)

Read all three before writing. [examples/where-names-live.md](examples/where-names-live.md) says
where the in-game names, rarities, classes and prices are found.

The rules of the style:

- **Language:** English everywhere.
- **Opening:** the line `Patch notes`, then one short paragraph with the intent of the update: why
  it exists and what it aims to fix. Only Bruus and Nelis know the intent, so write the placeholder
  `<intent: why this update exists>` unless the user gave it.
- **Sections**, in this order, each only when it has entries: General, Items, Classes, Enemies,
  Bosses, Controls, Performance, Soundtracks, Bug fixes, For developers. A section title is a plain
  line ending in a colon.
- **An item** is one line with its name, then a tag in parentheses with its class (or Generic) and
  rarity: `Explosive Greed (Generic legendary):`. A new item adds `(NEW)` after its name.
- **A changed value** is its own sub-bullet: `Stat: old -> new`. One stat per sub-bullet. Prices,
  rarities and percentages are written as the shop shows them.
- **A rework** gets an `Old:` and a `New:` sub-bullet with the in-game description before and
  after.
- **An explanation** gets its own sub-bullet one level deeper, and only where the change could
  mislead a player, for example when the effect stays the same and only the description changed.
- **A bug fix** names its subject on its own line, then a sub-bullet saying what now works. A light,
  casual tone is fine.
- **Changes without numbers**, such as controls or performance, are short plain bullets saying what
  the player notices.
- **No technical terms** outside For developers: no class names, file names, commit hashes or code
  words.
- **Indentation:** `- ` bullets, four spaces per level, the same depth for the same kind of line
  everywhere.

## Review

Dispatch one reviewer with the Agent tool, `model: sonnet`, using the brief in
[templates/review-brief.md](templates/review-brief.md). It checks every line of the notes against
the code at the end commit and against the fact list, and checks that every fact made it in. Fix
every finding it confirms before handing the notes over.

## Saving and recording the release

1. Save the notes in the `patch-notes-folder` as `<release-date>.md`, the date written as
   `YYYY-MM-DD`.
2. Give the user the notes and the list of suspected bugs, and say they are a draft for Bruus to go
   over.
3. **Add the release log row only when the user says the release was made.** The row holds the
   release name, the date, the short hash of the released commit and the notes file.
   That commit is the start of the next patch notes.
4. The notes file, the release log row and the `dev-changelog` line go in one commit, separate from
   any game code.

---
name: changelog
description: Holds the rules for the developers' running changelog, starting with how to resolve a merge conflict in it. Use when a merge, pull or merge-back of Nelis into a feature branch reports a conflict in the changelog, or when asked how the changelog is kept.
---

# Changelog

The developers' running changelog is the `dev-changelog` row of `.claude/bindings/changelog.md`.
Every committed change adds a line under its own date, as the repo's working rules say.
Two branches that each add lines near the top of the file conflict on every merge, because both sides edit the same place.
Neither side is wrong: the fix is to keep both.

This skill is how to resolve that conflict, never permission to start a merge or to resolve other conflicts.
The user-level commit rule still decides whether a conflicted merge is aborted or resolved.
Resolve only when the user has told you to merge and to resolve the changelog, or has said to fix this conflict. Otherwise abort the merge and report, as the commit rule says.

## Resolving a conflict in the changelog

**When** a merge reports a conflict, **run this checklist** — check every item, top to bottom, skipping none:
- [ ] The list of conflicted files (`git diff --name-only --diff-filter=U`) holds only the changelog.
- [ ] You are resolving by hand: no merge strategy option, no `--ours`, no `--theirs`, no auto-resolve option.
- [ ] Nobody told you to leave this conflict for them.

**If the first item is false, or the second cannot be met**, you MUST abort the merge (`git merge --abort`) and stop, naming the other conflicted files. **Zero exceptions.** Every other conflict needs the user.

Then resolve it with these rules:

1. **Keep every line of both sides.** No line is dropped, reworded, merged with another, or deduplicated. Two entries that look alike are two entries.
2. **A date section only one side has** goes where its date belongs: newest date first, as the file already orders its sections.
3. **A date section both sides have** becomes one section: the date heading once, each subheading (Added, Changed, Fixed) once in the order the file already uses, and under each subheading the lines of your branch first, then the lines of the branch merged in.
4. **Everything else in the file stays as it is**, line endings included.
5. Remove every conflict marker and stage only the changelog.

**Before committing the merge, run this checklist** — check every item, skipping none, and paste each command's output:
- [ ] No conflict marker is left: a search for the three marker lines (`<<<<<<<`, `=======`, `>>>>>>>`) in the changelog finds nothing.
- [ ] Nothing of your branch was lost: `git diff HEAD -- <changelog>` shows only added lines.
- [ ] Nothing of the other branch was lost: `git diff MERGE_HEAD -- <changelog>` shows only added lines.
- [ ] Each date appears as a heading once.

**If ANY item fails**, you MUST fix the file and run the whole checklist again before committing. **Zero exceptions.** Then commit with the default merge message.
`examples/conflict.md` shows a conflict and its resolution.

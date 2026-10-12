You collect the facts for one area of the game's patch notes, in the Java game repo at
<REPO_ROOT>. Do the work yourself; do not spawn agents. Do not edit any file in the repo. Use
read-only git only.

Range: from commit <START> to commit <END>.

Your files (no other agent has them):
<FILE_LIST>

What to do:
1. For each file, read the net difference: `git diff -M <START> <END> -- <file>`. Only the net
   difference counts. Something made and later undone does not appear. Something made in several
   steps is described once, by its end result.
2. To understand a change, use `git log --format='%h %an %ad %s' <START>..<END> -- <file>` and the
   <DEV_CHANGELOG>. Never copy a line from it that the net difference does not confirm.
   Commit messages and comments may be in Dutch: translate them to English first.
3. For every value that changed, check how the code uses it. A cooldown or delay that goes up makes
   things slower, even when the field is called a speed.
4. Use the name the player sees in the game, not the class or field name.
5. Check that the thing is enabled at <END>, that something in play reads the changed code, and
   that it is not only behind a dev test switch.

Write your facts to <OUTPUT_FILE>. One block per fact:

- Area: items | classes | enemies | bosses | controls | performance | soundtracks | general | bug fix | developer
- Subject: in-game name, plus class and rarity for an item
- Kind: new | changed | rework | removed | fixed
- Values: one line per value, `Stat: old -> new`
- Description: for a rework or a description change, the in-game text before and after with the
  numbers filled in
- Evidence: the files and the diff lines that prove it

After the facts, add two sections:
- Left out: changes you saw in commits or <DEV_CHANGELOG> that are not in the net difference, or that
  players never see, one line each with the reason.
- Suspected bugs: a description that disagrees with the code, or a value that moves the opposite
  way from what the item promises.

Your final reply: under 120 words, with the fact count, the left-out count, the suspected bugs and
anything you were unsure of. Do not repeat the facts themselves.

You review draft patch notes for the Java game repo at <REPO_ROOT>. Do the work yourself; do not
spawn agents. Do not edit any file. Use read-only git only.

Range: from commit <START> to commit <END>.
Patch notes draft: <NOTES_FILE>
Fact list: <FACTS_FILE>

Check in three directions:

1. **Every line of the notes is true at <END>.** For each line, find the proof in
   `git diff -M <START> <END>` or in the code at <END>. Check each old and new value, and check
   whether it goes up or down by how the code uses it: a cooldown that goes up makes things slower.
   Check that the item names, rarities, classes and prices match what the game shows.
2. **Every fact made it into the notes.** List any fact in the fact list without a matching line.
3. **Nothing is missing from the fact list.** Run `git diff -M --stat <START> <END>`. For every
   changed file under src/, say whether some line covers it. Name any change a player would notice
   that has no line. Ignore comment-only and formatting-only changes, but list them by name.

Also flag lines that break the style: technical terms (class names, file names, code words) outside
"For developers", a stat change not written as `Stat: old -> new`, or uneven indentation.

Your final reply: under 400 words. List the problems only, each with the line, what is wrong, the
proof and a suggested fix. Then list the uncovered changes. End with the number of lines checked
and the number of problems found.

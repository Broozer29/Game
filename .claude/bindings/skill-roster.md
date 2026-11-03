# Skill Roster — Game

## Routing

| work type | skills |
| --- | --- |
| any Java code (always) | none: there is no Java standards skill; follow the working rules in CLAUDE.md |
| design of a non-trivial change | `design-session`, `design-review` |
| docs under `docs/**` | `docs-authoring` |
| running the autoplay bot, or adding or editing a bot scenario | `bot-run` |
| merging a branch whose merge conflicts in the developers' changelog | `changelog` |

Every row lists ALL governing skills — invoke all of them, never pick one. The base row is the row
whose work type ends in `(always)`; it is listed first and applies to every task. A table with no
`(always)` row has no base row, and a skill that needs one treats it as absent.

## Paths

| glob | skills |
| --- | --- |
| `docs/**/*.md` | `docs-authoring` |
| `README.md` | `docs-authoring` |
| `tools/bot/**` | `bot-run` |
| `docs/patch-notes/**` | `patch-notes` |
| `docs/releases.md` | `patch-notes` |

A glob is relative to the repo root with forward slashes; `**` spans folders, `*` and `?` stay in one
segment. Every row whose glob matches a file applies, and the skills of all matching rows must be invoked
before that file is first written. A file matching no row is unguarded.

## Commands

| pattern | skills |
| --- | --- |
| `bot-run\.sh\|check-jvm-options\.sh` | `bot-run` |

A pattern is an extended regular expression written in backticks, with every `|` inside it written `\|`. It is matched against the Bash command (heredoc bodies removed), the Agent call's `subagent_type` and `description`, or the WebFetch `url`. Every row whose pattern matches applies, and the skills of all matching rows must be invoked before the call runs. A skills cell of `refuse: <reason>` denies the call with that reason and no skill lifts it.

## Content

| glob | pattern | skills |
| --- | --- | --- |
| `CHANGELOG.md` | `<{7}` | `changelog` |

A glob is relative to the repo root as in Paths. A pattern is an extended regular expression, with `\|` standing for a `|` inside it, matched against the new text of an Edit or a Write to a file the glob matches. Every row where both match applies, and the skills of all matching rows must be invoked before the write. A skills cell of `refuse: <reason>` denies the write with that reason and no skill lifts it.

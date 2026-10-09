#!/usr/bin/env bash
# Prints planboard entries (docs/planboard/*.md) filtered by type and area.
# Usage: planboard.sh <bugs|features|balance|ideas|all> [--unanswered] [area ...]
#   --unanswered  only entries without a "- Bruus ..." reply note
# Lines outside an entry (free text, unknown headings) print as "note:" lines where they stand.
set -euo pipefail

board="$(cd "$(dirname "$0")/../.." && pwd)/docs/planboard"

areas() {
  for f in "$board"/*.md; do
    name="$(basename "$f" .md)"
    [ "$name" = README ] || printf '%s ' "$name"
  done
  echo
}

usage() {
  echo "usage: planboard.sh <bugs|features|balance|ideas|all> [--unanswered] [area ...]" >&2
  echo "areas: $(areas)" >&2
  exit 1
}

[ $# -ge 1 ] || usage
case "$1" in
  bugs) want=Bugs ;;
  features) want=Features ;;
  balance) want=Balance ;;
  ideas) want=Ideas ;;
  all) want= ;;
  *) usage ;;
esac
shift

unanswered=0
files=()
for arg in "$@"; do
  if [ "$arg" = --unanswered ]; then
    unanswered=1
    continue
  fi
  f="$board/$arg.md"
  [ -f "$f" ] || { echo "unknown area: $arg" >&2; usage; }
  files+=("$f")
done
if [ ${#files[@]} -eq 0 ]; then
  for f in "$board"/*.md; do
    [ "$(basename "$f")" = README.md ] || files+=("$f")
  done
fi

for f in "${files[@]}"; do
  awk -v want="$want" -v unanswered="$unanswered" '
    function emit(line) {
      if (!shown) { print "== " title " =="; shown = 1 }
      if (want == "" && section != last) { print "[" section "]"; last = section }
      print line
    }
    # Prints the buffered entry and its notes, unless --unanswered and Bruus replied.
    function flush(   i) {
      if (n > 0 && !(unanswered && answered))
        for (i = 1; i <= n; i++) emit(buf[i])
      n = 0; answered = 0
    }
    { sub(/\r$/, "") }
    /^# / && title == "" { title = substr($0, 3); next }
    /^[[:space:]]*$/ { next }
    /^## / {
      name = substr($0, 4)
      sub(/[[:space:]]+$/, "", name)
      if (name ~ /^(Bugs|Features|Balance|Ideas)$/) { flush(); section = name; next }
    }
    section == "" { next }
    want != "" && section != want { next }
    /^- / { flush(); buf[++n] = $0; next }
    /^[[:space:]]/ && n > 0 {
      buf[++n] = $0
      if ($0 ~ /^[[:space:]]+- [Bb]ruus/) answered = 1
      next
    }
    { flush(); line = $0; sub(/^[[:space:]]+/, "", line); emit("  note: " line) }
    END { flush(); if (shown) print "" }
  ' "$f"
done

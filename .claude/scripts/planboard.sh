#!/usr/bin/env bash
# Prints planboard entries (docs/planboard/*.md) filtered by type and area.
# Usage: planboard.sh <bugs|features|balance|ideas|all> [area ...]
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
  echo "usage: planboard.sh <bugs|features|balance|ideas|all> [area ...]" >&2
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

files=()
if [ $# -eq 0 ]; then
  for f in "$board"/*.md; do
    [ "$(basename "$f")" = README.md ] || files+=("$f")
  done
else
  for area in "$@"; do
    f="$board/$area.md"
    [ -f "$f" ] || { echo "unknown area: $area" >&2; usage; }
    files+=("$f")
  done
fi

for f in "${files[@]}"; do
  awk -v want="$want" -v file="$(basename "$f")" '
    { sub(/\r$/, "") }
    /^# / && title == "" { title = substr($0, 3); next }
    /^## / {
      section = substr($0, 4)
      sub(/[[:space:]]+$/, "", section)
      if (section !~ /^(Bugs|Features|Balance|Ideas)$/)
        printf "warning: %s: unknown heading \"%s\"\n", file, section > "/dev/stderr"
      next
    }
    section == "" || /^[[:space:]]*$/ { next }
    want == "" || section == want {
      if (!shown) { print "== " title " =="; shown = 1 }
      if (want == "" && section != last) { print "[" section "]"; last = section }
      print
    }
    END { if (shown) print "" }
  ' "$f"
done

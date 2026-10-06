#!/usr/bin/env bash
# Installs a test save profile (.claude/test-saves/<profile>.json) as savefile.json in the repo root.
# Usage: use-test-save.sh [profile-name]
#   no argument  lists the available profiles
# The current savefile.json (if any) is first copied to target/perf/savefile-backup-<timestamp>.json.
set -euo pipefail

root="$(cd "$(dirname "$0")/../.." && pwd)"
profiles="$root/.claude/test-saves"

list() {
  for f in "$profiles"/*.json; do
    basename "$f" .json
  done
}

if [ $# -eq 0 ]; then
  echo "usage: use-test-save.sh <profile-name>" >&2
  echo "profiles:" >&2
  list >&2
  exit 0
fi

name="${1%.json}"
src="$profiles/$name.json"
if [ ! -f "$src" ]; then
  echo "unknown profile: $name" >&2
  echo "profiles:" >&2
  list >&2
  exit 1
fi

target="$root/savefile.json"
if [ -f "$target" ]; then
  mkdir -p "$root/target/perf"
  backup="$root/target/perf/savefile-backup-$(date +%Y%m%d-%H%M%S).json"
  cp "$target" "$backup"
  echo "backed up savefile.json to ${backup#"$root/"}"
else
  echo "no existing savefile.json, nothing to back up"
fi

cp "$src" "$target"
echo "installed profile '$name' as savefile.json"
python -c "
import json,sys
d=json.load(open(sys.argv[1]))
print('  class:', d['playerclass'], '| level', d['playerLevel'], '| stages completed:', d['stagesCompleted'], '| money:', d['money'], '| state:', d['gameStateEnums'])
print('  items:', ', '.join('%s x%d' % kv for kv in d['items'].items()))
" "$target" 2>/dev/null || echo "  (python not found, item summary skipped)"
echo "start the game, then choose CONTINUE RUN on the main menu"

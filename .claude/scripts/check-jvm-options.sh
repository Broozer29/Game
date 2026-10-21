#!/usr/bin/env bash
# Checks that every place that starts the game uses the game's own memory and collector settings.
# The single source is the jpackage <javaOptions> in pom.xml (its -Xms, -Xmx and -XX: options).
# Places checked: the three vmArgs in .vscode/launch.json, the run command in CLAUDE.md, and .claude/scripts/bot-run.sh.
# Recording-only flags in launch.json (-XX:NativeMemoryTracking=, -XX:StartFlightRecording=, -Xlog:, -D) are allowed.
#
# Usage: check-jvm-options.sh [ROOT]
#   ROOT  folder holding pom.xml, CLAUDE.md, .vscode/launch.json and .claude/scripts/bot-run.sh (default: the repo root)
# Prints one line per place: OK or MISMATCH with the difference. Exit code 1 on any mismatch, 0 otherwise.
set -euo pipefail

root="${1:-$(cd "$(dirname "$0")/../.." && pwd)}"
cd "$root"

# keeps only the -Xms, -Xmx and -XX: tokens, sorted, one per line
core_tokens() {
  tr ' ' '\n' | grep -E '^(-Xms|-Xmx|-XX:)' | grep -vE '^-XX:(NativeMemoryTracking=|StartFlightRecording=)' | sort -u || true
}

pom_opts=$(sed -n 's|^[[:space:]]*<option>\(-X[^<]*\)</option>.*|\1|p' pom.xml | tr '\n' ' ')
pom_opts="${pom_opts% }"
case "$pom_opts" in
  *-Xmx*) ;;
  *) echo "MISMATCH pom.xml: could not read the -Xms/-Xmx/-XX: options (got: '$pom_opts')"; exit 1 ;;
esac
echo "pom.xml: $pom_opts"
pom_set=$(echo "$pom_opts" | core_tokens)

status=0
compare() { # label, token-set
  local label="$1" got="$2"
  if [ "$got" = "$pom_set" ]; then
    echo "OK       $label"
  else
    echo "MISMATCH $label: differs from pom.xml (< pom.xml, > $label):"
    diff <(echo "$pom_set") <(echo "$got") | grep -E '^[<>]' | sed 's/^/           /' || true
    status=1
  fi
}

n=0
while IFS= read -r args; do
  n=$((n + 1))
  compare ".vscode/launch.json vmArgs #$n" "$(echo "$args" | core_tokens)"
done < <(grep -o '"vmArgs": *"[^"]*"' .vscode/launch.json | sed 's/^"vmArgs": *"//; s/"$//')
if [ "$n" -ne 3 ]; then
  echo "MISMATCH .vscode/launch.json: expected 3 vmArgs, found $n"
  status=1
fi

claude_line=$(grep -o 'java [^`]*-jar' CLAUDE.md | head -1 || true)
if [ -z "$claude_line" ]; then
  echo "MISMATCH CLAUDE.md: no run command line (java ... -jar) found"
  status=1
else
  compare "CLAUDE.md run command" "$(echo "$claude_line" | core_tokens)"
fi

bot=.claude/scripts/bot-run.sh
if ! grep -q "<option>" "$bot"; then
  echo "MISMATCH $bot: does not read the options from pom.xml"
  status=1
elif grep -nE -e '-Xmx|-Xms|MaxGCPauseMillis' "$bot" | grep -vE '^[0-9]+:[[:space:]]*#' | grep -vE 'echo|[*]-Xmx[*]' >/dev/null; then
  echo "MISMATCH $bot: has a literal -Xmx/-Xms/MaxGCPauseMillis"
  status=1
else
  echo "OK       $bot reads the options from pom.xml"
fi

exit $status

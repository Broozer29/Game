#!/usr/bin/env bash
# Runs the game with the autoplay bot and reports what the run left behind.
# The bot plays by itself (see src/main/java/net/riezebos/bruus/tbd/bot/BotSettings.java), so nobody has to sit at the PC,
# but the game opens a full-screen window: do not use the PC for other full-screen work while it runs.
#
# Usage: bot-run.sh [--scenario NAME] [options]
#   --scenario NAME  run tools/bot/scenarios/NAME.json (see tools/bot/README.md); the options below then win over the file
#   --minutes N      play for N minutes, then stop (default 10, 0 = until a level or death limit)
#   --class NAME     Captain, FireFighter, Carrier or Mutalisk (default Captain)
#   --levels N       stop after N completed levels (default 0 = no limit)
#   --runs N         stop after N runs ended in death (default 0 = no limit)
#   --shots N        a screenshot every N seconds (default 30, 0 = off)
#   --dev a,b        DevTestSettings switches to turn on, for example playerIsImmune,onlyBossLevels
#   --sound          leave the audio on (by default devTestMuteMode is on)
#   --jfr            also record a Java Flight Recorder file (slower, for deep memory work)
#   --keep-save      play on top of the current savefile.json instead of starting without one
#   --no-build       skip "mvn -q compile"
#
# Everything lands in tools/bot/results/<scenario>-<timestamp>/ (target/perf/bot-<timestamp>/ without a scenario):
# report.md, summary.json, bot.log, load-*.csv, gc.log, stdout.log, screenshots/.
# The exit code is the game's: 0 = finished and every limit met, 6 = finished but a limit of the scenario was missed.
# Your own savefile.json is backed up first and put back afterwards.
# Unplug game controllers first: a real controller on seat 1 takes the menus away from the bot.
# From Claude: start it with run_in_background, it runs for the whole length of the test.
set -euo pipefail

root="$(cd "$(dirname "$0")/../.." && pwd)"
cd "$root"

minutes=""
klass=""
levels=""
runs=""
shots=""
scenario=""
dev=""
sound=0
jfr=0
keep_save=0
build=1

while [ $# -gt 0 ]; do
  case "$1" in
    --scenario) scenario="$2"; shift 2 ;;
    --minutes) minutes="$2"; shift 2 ;;
    --class) klass="$2"; shift 2 ;;
    --levels) levels="$2"; shift 2 ;;
    --runs) runs="$2"; shift 2 ;;
    --shots) shots="$2"; shift 2 ;;
    --dev) dev="$2"; shift 2 ;;
    --sound) sound=1; shift ;;
    --jfr) jfr=1; shift ;;
    --keep-save) keep_save=1; shift ;;
    --no-build) build=0; shift ;;
    -h|--help) sed -n '2,25p' "$0"; exit 0 ;;
    *) echo "unknown option: $1" >&2; exit 1 ;;
  esac
done

if [ "$build" = 1 ]; then
  echo "compiling..."
  mvn -q compile
fi

classpath_file="$root/target/perf/bot-classpath.txt"
if [ ! -f "$classpath_file" ] || [ "$root/pom.xml" -nt "$classpath_file" ]; then
  echo "resolving the dependency classpath..."
  mkdir -p "$root/target/perf"
  mvn -q dependency:build-classpath -Dmdep.outputFile="$classpath_file"
fi

winpath() { if command -v cygpath >/dev/null 2>&1; then cygpath -m "$1"; else echo "$1"; fi; }

# A scenario file: its minutes (for the safety net below) and its start save are read here; the game validates the whole file itself
scenario_file=""
scenario_save=""
file_minutes=""
if [ -n "$scenario" ]; then
  scenario="${scenario%.json}"
  scenario_file="$root/tools/bot/scenarios/$scenario.json"
  if [ ! -f "$scenario_file" ]; then
    echo "unknown scenario: $scenario" >&2
    echo "scenarios:" >&2
    ls "$root/tools/bot/scenarios" 2>/dev/null | sed 's/\.json$//' >&2
    exit 1
  fi
  scenario_save="$(sed -n 's/.*"save"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' "$scenario_file" | head -1)"
  file_minutes="$(sed -n 's/.*"minutes"[[:space:]]*:[[:space:]]*\([0-9.]*\).*/\1/p' "$scenario_file" | head -1)"
fi

stamp="$(date +%Y%m%d-%H%M%S)"
if [ -n "$scenario" ]; then
  dir="$root/tools/bot/results/$scenario-$stamp"
else
  dir="$root/target/perf/bot-$stamp"
fi
mkdir -p "$dir"
wdir="$(winpath "$dir")"

# The game overwrites savefile.json while it runs: keep the user's save safe, start from nothing unless told otherwise
save="$root/savefile.json"
mkdir -p "$root/target/perf"
save_backup="$root/target/perf/savefile-backup-bot-$stamp.json" # outside the run folder, so nothing that cleans the run folder can lose it
had_save=0
if [ -f "$save" ]; then
  had_save=1
  cp "$save" "$save_backup"
  if [ "$keep_save" = 0 ]; then
    rm "$save"
  fi
fi
restore_save() {
  if [ -f "$save" ] && [ -d "$dir" ]; then
    cp "$save" "$dir/savefile-after-run.json"
  fi
  if [ "$had_save" = 1 ]; then
    cp "$save_backup" "$save"
  elif [ -f "$save" ]; then
    rm "$save"
  fi
}
trap restore_save EXIT

if [ -n "$scenario_save" ]; then
  profile="$root/.claude/test-saves/${scenario_save%.json}.json"
  if [ ! -f "$profile" ]; then
    echo "scenario save not found: $profile" >&2
    exit 1
  fi
  cp "$profile" "$save"
  echo "installed test save $scenario_save"
fi

flags=()
[ -n "$scenario_file" ] && flags+=("-Dgame.bot.scenario=$(winpath "$scenario_file")")
[ -n "$klass" ] && flags+=("-Dgame.bot.class=$klass")
[ -n "$minutes" ] && flags+=("-Dgame.bot.minutes=$minutes")
[ -n "$levels" ] && flags+=("-Dgame.bot.levels=$levels")
[ -n "$runs" ] && flags+=("-Dgame.bot.runs=$runs")
[ -n "$shots" ] && flags+=("-Dgame.bot.screenshotSeconds=$shots")
[ "$sound" = 0 ] && flags+=("-Dgame.dev.devTestMuteMode=true")
if [ -n "$dev" ]; then
  IFS=',' read -r -a names <<< "$dev"
  for name in "${names[@]}"; do
    flags+=("-Dgame.dev.$name=true")
  done
fi
if [ "$jfr" = 1 ]; then
  flags+=("-XX:StartFlightRecording=filename=$wdir/run.jfr,settings=profile,dumponexit=true")
fi

echo "bot run in $dir (${scenario:-no scenario})"
java -Xms4g -Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=50 \
  -Dsun.java2d.opengl=true -Dsun.java2d.d3d=true -Dsun.java2d.accthresh=0 \
  "-Xlog:gc:file=$wdir/gc.log:time,uptime" \
  -Dgame.bot=true "-Dgame.bot.dir=$wdir" \
  ${flags[@]+"${flags[@]}"} \
  -cp "target/classes;$(cat "$classpath_file")" net.riezebos.bruus.tbd.Game \
  > "$dir/stdout.log" 2>&1 &
pid=$!

# Safety net: the bot stops itself, but a hung game must not run forever
limit=0
effective_minutes="${minutes:-${file_minutes:-10}}"
if [ "$effective_minutes" != 0 ]; then
  limit=$(awk -v m="$effective_minutes" 'BEGIN { printf "%d", m * 60 + 600 }')
fi
waited=0
while kill -0 "$pid" 2>/dev/null; do
  sleep 5
  waited=$((waited + 5))
  if [ "$limit" -gt 0 ] && [ "$waited" -gt "$limit" ]; then
    echo "the game did not stop within $limit s, killing it"
    kill -9 "$pid" 2>/dev/null || true
    break
  fi
done
exit_code=0
wait "$pid" 2>/dev/null || exit_code=$?

if [ "$exit_code" = 5 ]; then
  echo
  echo "=== the scenario file is not valid, nothing was run ==="
  grep -A40 -e "is not valid" -e "is not a DevTestSettings" "$dir/stdout.log" | head -40 || cat "$dir/stdout.log"
  rm -rf "$dir"
  exit 5
fi

echo
echo "=== game exit code: $exit_code (0 = finished, 1 = game crashed, 2 = uncaught exception, 3 = stuck, 4 = bot error, 5 = bad scenario, 6 = a limit was missed) ==="
if [ -f "$dir/report.md" ]; then
  cat "$dir/report.md"
elif [ -f "$dir/summary.json" ]; then
  cat "$dir/summary.json"
else
  echo "no summary.json was written, the game probably died hard: see stdout.log"
fi

for f in crash.txt error_log.txt; do
  if [ -f "$dir/$f" ]; then
    echo
    echo "=== $f (first 25 lines) ==="
    head -25 "$dir/$f"
  fi
done

csv="$(ls "$dir"/load-*.csv 2>/dev/null | head -1 || true)"
if [ -n "$csv" ]; then
  echo
  echo "=== performance ($(basename "$csv")) ==="
  awk -F, '
    NR == 1 { for (i = 1; i <= NF; i++) col[$i] = i; next }
    {
      rows++
      split("tickAvgMs tickMaxMs gapMaxMs drawAvgMs drawMaxMs usedHeapMb enemies missiles", names, " ")
      for (n in names) {
        name = names[n]
        if (name in col) {
          v = $(col[name]) + 0
          sum[name] += v
          if (v > max[name]) max[name] = v
        }
      }
    }
    END {
      printf "%-12s %10s %10s\n", "column", "average", "max"
      for (n = 1; n <= 8; n++) {
        name = names[n]
        if (rows > 0 && name in col) printf "%-12s %10.2f %10.2f\n", name, sum[name] / rows, max[name]
      }
      printf "intervals: %d\n", rows
    }' "$csv"
fi

if [ -f "$dir/gc.log" ]; then
  echo
  echo "=== garbage collection (gc.log) ==="
  echo "pauses: $(grep -c 'Pause' "$dir/gc.log" || true), full collections: $(grep -c 'Pause Full' "$dir/gc.log" || true)"
  awk '/Pause/ { v = $NF; sub("ms", "", v); if (v + 0 > m) m = v + 0 } END { printf "longest pause: %.1f ms\n", m }' "$dir/gc.log"
fi

echo
echo "screenshots: $(ls "$dir/screenshots" 2>/dev/null | wc -l) in $dir/screenshots"
echo "run folder: $dir"
exit "$exit_code"

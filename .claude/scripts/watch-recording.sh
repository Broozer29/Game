#!/usr/bin/env bash
# Watches a "Run Game (recording memory)" run: confirms the recorders start, reports startup times and live heap
# (after a forced full GC) every 3 minutes, and reports the saved recording on exit. Usage: watch-recording.sh
root="$(cd "$(dirname "$0")/../.." && pwd)"
perf="$root/target/perf"
start=$(date +%s)
echo "watcher armed; waiting for the game to start"
log="$root/startup_log.txt"
inits=$(grep -c "fully initialized" "$log" 2>/dev/null || echo 0)

pid=""
while [ -z "$pid" ]; do
  pid=$(jcmd -l 2>/dev/null | grep -i "tbd.Game" | grep -v JCmd | awk '{print $1}' | head -n 1)
  [ -n "$pid" ] && break
  sleep 2
done
echo "game started (pid $pid)"; gstart=$(date +%s)

sleep 5
gclog=$(ls -t "$perf"/gc-*.log 2>/dev/null | head -n 1)
if [ -n "$gclog" ] && [ "$(stat -c %Y "$gclog")" -ge "$start" ]; then
  echo "gc log is being written: $(basename "$gclog")"
else
  echo "PROBLEM: no new gc log in target/perf; the game was probably not started with 'Run Game (recording memory)'"
fi
if jcmd "$pid" JFR.check 2>&1 | grep -q "running"; then
  echo "flight recorder is running: $(jcmd "$pid" JFR.check 2>&1 | grep -o 'Recording [0-9]*.*' | head -n 1)"
else
  echo "PROBLEM: flight recorder is not running in pid $pid"
fi

# Forces a full collection and prints the heap still in use (the memory the game really keeps).
liveheap() {
  jcmd "$pid" GC.run >/dev/null 2>&1
  jcmd "$pid" GC.heap_info 2>/dev/null | grep -oE 'used [0-9]+K' | head -n 1 | awk '{printf "%.0f MB", $2/1024}'
}
heaplog="$perf/heap-$(date +%Y-%m-%d_%H-%M-%S).txt"
for i in $(seq 1 60); do
  [ "$(grep -c "fully initialized" "$log" 2>/dev/null || echo 0)" -gt "$inits" ] && break
  sleep 1
done
echo "startup (s): $(bash "$(dirname "$0")/startup-times.sh" 1 | tail -n 1)"
bash "$(dirname "$0")/startup-times.sh" 1 | tail -n 1 >> "$heaplog"
sleep 5
h=$(liveheap); echo "$(( $(date +%s) - gstart ))s $h" >> "$heaplog"
echo "live heap after start: $h"

last=$(date +%s)
while jcmd -l 2>/dev/null | awk '{print $1}' | grep -qx "$pid"; do
  now=$(date +%s)
  if [ $((now - last)) -ge 180 ]; then
    h=$(liveheap); echo "$(( now - gstart ))s $h" >> "$heaplog"
    echo "still running, $(( (now - gstart) / 60 )) min; live heap $h"
    last=$now
  fi
  sleep 3
done

echo "game closed after $(( ($(date +%s) - gstart) / 60 )) min $(( ($(date +%s) - gstart) % 60 )) s"
sleep 5
jfr=$(ls -t "$perf"/run-*.jfr 2>/dev/null | head -n 1)
if [ -n "$jfr" ] && [ "$(stat -c %Y "$jfr")" -ge "$start" ]; then
  echo "recording saved: $(basename "$jfr") ($(du -h "$jfr" | cut -f1))"
else
  echo "PROBLEM: no new .jfr file in target/perf after exit (crash or forced stop?)"
fi

#!/usr/bin/env bash
# Watches a "Run Game (recording memory)" run: confirms the recorders start, reports startup times and live heap
# (after a forced full GC) every 3 minutes, and reports the saved recording on exit. Usage: watch-recording.sh
root="$(cd "$(dirname "$0")/../.." && pwd)"
perf="$root/target/perf"
start=$(date +%s)
echo "watcher armed; waiting for the game to start"
log="$root/startup_log.txt"
inits=$(grep -c "fully initialized" "$log" 2>/dev/null || true); inits=${inits:-0}

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
  # The top classes by live bytes show what the memory is made of; one file per reading
  jcmd "$pid" GC.class_histogram 2>/dev/null | head -n 33 > "$perf/histogram-$(date +%Y-%m-%d_%H-%M-%S).txt"
  # Native memory per category (heap, GC, threads, code, other); only when started with -XX:NativeMemoryTracking
  jcmd "$pid" VM.native_memory summary 2>/dev/null > "$perf/nmt-$(date +%Y-%m-%d_%H-%M-%S).txt"
}
# Prints the process's total committed memory from the latest native memory summary, if there is one
nativetotal() {
  local f; f=$(ls -t "$perf"/nmt-*.txt 2>/dev/null | head -n 1)
  [ -n "$f" ] && grep -m1 -oE 'Total: reserved=[0-9]+KB, committed=[0-9]+KB' "$f" | grep -oE 'committed=[0-9]+' | awk -F= '{printf "; process total %.0f MB", $2/1024}'
}
# Prints the game process's private memory and thread count, read through Windows
procstats() {
  powershell -NoProfile -Command "\$p=Get-Process -Id $pid; '{0} {1}' -f \$p.PrivateMemorySize64, \$p.Threads.Count" 2>/dev/null | tr -d '\r' | awk 'NF==2 {printf "; process private %.0f MB, %d threads", $1/1048576, $2}'
}
# Prints the last line of the newest image cache log written during this watch, if there is one
cachestats() {
  local f; f=$(ls -t "$perf"/cache-*.log 2>/dev/null | head -n 1)
  [ -n "$f" ] && [ "$(stat -c %Y "$f")" -ge "$start" ] && tail -n 1 "$f"
}
heaplog="$perf/heap-$(date +%Y-%m-%d_%H-%M-%S).txt"
for i in $(seq 1 60); do
  now_inits=$(grep -c "fully initialized" "$log" 2>/dev/null || true)
  [ "${now_inits:-0}" -gt "$inits" ] && break
  sleep 1
done
echo "startup (s): $(bash "$(dirname "$0")/startup-times.sh" 1 | tail -n 1)"
bash "$(dirname "$0")/startup-times.sh" 1 | tail -n 1 >> "$heaplog"
sleep 5
h=$(liveheap); echo "$(( $(date +%s) - gstart ))s $h$(procstats)" >> "$heaplog"
echo "live heap after start: $h$(nativetotal)$(procstats)"
c=$(cachestats); [ -n "$c" ] && echo "image cache: $c"

last=$(date +%s)
# tasklist is cheap; jcmd -l starts a whole JVM every time, too heavy to poll every few seconds
while tasklist //FI "PID eq $pid" //NH 2>/dev/null | grep -q " $pid "; do
  now=$(date +%s)
  if [ $((now - last)) -ge 180 ]; then
    h=$(liveheap); echo "$(( now - gstart ))s $h$(procstats)" >> "$heaplog"
    echo "still running, $(( (now - gstart) / 60 )) min; live heap $h$(nativetotal)$(procstats)"
    c=$(cachestats); [ -n "$c" ] && echo "image cache: $c"
    last=$now
  fi
  sleep 5
done

echo "game closed after $(( ($(date +%s) - gstart) / 60 )) min $(( ($(date +%s) - gstart) % 60 )) s"
sleep 5
jfr=$(ls -t "$perf"/run-*.jfr 2>/dev/null | head -n 1)
if [ -n "$jfr" ] && [ "$(stat -c %Y "$jfr")" -ge "$start" ]; then
  echo "recording saved: $(basename "$jfr") ($(du -h "$jfr" | cut -f1))"
else
  echo "PROBLEM: no new .jfr file in target/perf after exit (crash or forced stop?)"
fi

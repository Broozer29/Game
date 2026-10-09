#!/usr/bin/env bash
# Prints the startup phase times of every run in startup_log.txt (or only the last N runs).
# Usage: startup-times.sh [N]
awk -v last="${1:-0}" '
  function t(s,  a) { split(substr(s, 13, 15), a, ":"); return a[1]*3600 + a[2]*60 + a[3] }
  /=== Application Starting ===/ { n++; start[n] = t($0); day[n] = substr($0, 2, 19) }
  /Loading assets\.\.\./ { la[n] = t($0) }
  /Preloading assets\.\.\./ { pa[n] = t($0) }
  /Preloading laserbeams\.\.\./ { pl[n] = t($0) }
  /Finishing initialization\.\.\./ { fi[n] = t($0) }
  /=== Application fully initialized ===/ { done[n] = t($0) }
  END {
    printf "%-19s %6s %6s %6s %6s %6s %6s\n", "run", "launch", "assets", "enemy", "laser", "finish", "TOTAL"
    for (i = (last > 0 && n > last ? n - last + 1 : 1); i <= n; i++) {
      if (!done[i]) continue
      printf "%-19s %6.1f %6.1f %6.1f %6.1f %6.1f %6.1f\n", day[i], la[i]-start[i], pa[i]-la[i], pl[i]-pa[i], fi[i]-pl[i], done[i]-fi[i], done[i]-start[i]
    }
  }' "$(cd "$(dirname "$0")/../.." && pwd)/startup_log.txt"

// Runs the packaged game jar (`mvn package` builds it) with the memory settings of the "Run Game" launch
// configuration. With --profile it also records a Java Flight Recorder profile, a native-memory summary and a GC
// log into target/perf, as the "Run Game (recording memory)" launch configuration does.
// Exit codes: the game's own code when it stops; 2 when the jar is missing, java cannot start, or an argument is unknown.
import { spawn } from "node:child_process";
import { existsSync, mkdirSync } from "node:fs";
import { join, resolve } from "node:path";

const REPO_ROOT = resolve(import.meta.dirname, "..", "..");
const JAR = join(REPO_ROOT, "target", "Game-0.0.1-SNAPSHOT.jar");
const PERF_DIR = join(REPO_ROOT, "target", "perf");
const CANNOT_RUN = 2;

const BASE_ARGS = ["-Xms2g", "-Xmx3g", "-XX:+UseG1GC", "-Dsun.java2d.opengl=true", "-Dsun.java2d.d3d=true"];
const PROFILE_ARGS = [
  "-XX:NativeMemoryTracking=summary",
  "-XX:StartFlightRecording=filename=target/perf/run-%t.jfr,settings=profile,dumponexit=true",
  "-Xlog:gc:file=target/perf/gc-%t.log:time,uptime",
  "-Dgame.cacheStatsDir=target/perf",
];

const args = process.argv.slice(2);
const unknown = args.filter((arg) => arg !== "--profile");
if (unknown.length > 0) {
  console.error(`Unknown argument: ${unknown.join(" ")}. The only argument is --profile.`);
  process.exit(CANNOT_RUN);
}
const profile = args.includes("--profile");

if (!existsSync(JAR)) {
  console.error(`No game jar at ${JAR}; build it first with mvn package.`);
  process.exit(CANNOT_RUN);
}
if (profile) {
  mkdirSync(PERF_DIR, { recursive: true });
  console.log(`Recording a profile into ${PERF_DIR} (written when the game exits).`);
}

const child = spawn("java", [...BASE_ARGS, ...(profile ? PROFILE_ARGS : []), "-jar", JAR], { cwd: REPO_ROOT, stdio: "inherit", shell: false });
child.on("error", (error) => {
  console.error(`java could not start: ${error.message}`);
  process.exit(CANNOT_RUN);
});
child.on("exit", (code, signal) => process.exit(code ?? (signal === null ? 0 : 1)));

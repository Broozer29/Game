package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

//Helpers to run and find the outside programs the media players talk to
public final class MediaPlayerProcesses {

    private MediaPlayerProcesses() {
    }

    public record Output(int exitCode, String stdout, String stderr) {
        public boolean succeeded() {
            return exitCode == 0;
        }
    }

    //Runs a command and waits for it to finish. Exit code -1 means it could not start or took longer than the timeout
    public static Output run(long timeoutMillis, String... command) {
        Process process = null;
        try {
            process = new ProcessBuilder(command).start();
            if (!process.waitFor(timeoutMillis, TimeUnit.MILLISECONDS)) {
                return new Output(-1, "", "No answer within " + timeoutMillis + " ms");
            }
            String stdout = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            String stderr = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            return new Output(process.exitValue(), stdout, stderr);
        } catch (IOException e) {
            return new Output(-1, "", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new Output(-1, "", "Interrupted");
        } finally {
            if (process != null) {
                process.destroyForcibly(); //Does nothing when it already finished
            }
        }
    }

    //True when a program runs whose executable path ends with the given text, like "/Spotify.app/Contents/MacOS/Spotify"
    public static boolean isRunning(String executablePathEnd) {
        String end = executablePathEnd.toLowerCase();
        return ProcessHandle.allProcesses()
                .map(process -> process.info().command().orElse(""))
                .anyMatch(command -> command.toLowerCase().endsWith(end));
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isMacOS() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }
}

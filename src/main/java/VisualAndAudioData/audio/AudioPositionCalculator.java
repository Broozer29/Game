package VisualAndAudioData.audio;

import javax.sound.sampled.Clip;

public class AudioPositionCalculator {
	
	private static AudioPositionCalculator instance = new AudioPositionCalculator();
	
	private AudioPositionCalculator() {
		
	}
	
	public static AudioPositionCalculator getInstance() {
		return instance;
	}

	public long getFrameLengthForTime(Clip clip, int seconds) {
	    // Get the frame rate of the audio file
	    float frameRate = clip.getFormat().getFrameRate();

	    // Calculate the number of frames that corresponds to the given number of seconds
	    long frameLengthForTime = (long) (seconds * frameRate);

	    return frameLengthForTime;
	}
	
	
	public float getPlaybackTimeInSeconds(Clip clip, long framePosition) {
	    // Get the frame rate of the audio file
		
	    float frameRate = clip.getFormat().getFrameRate();

	    // Calculate the playback time in seconds
	    float playbackTimeInSeconds = framePosition / frameRate;

	    return playbackTimeInSeconds;
	}
}

package net.riezebos.bruus.tbd.game.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//What the player chose in the settings page, as stored in settings.json
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 100;

    private int musicVolume = MAX_VOLUME; //Whole percentage, 100 is the loudness every sound has in code
    private int effectsVolume = MAX_VOLUME;
    private InputBindings inputBindings = new InputBindings();

    public Settings() {
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = clampVolume(musicVolume);
    }

    public int getEffectsVolume() {
        return effectsVolume;
    }

    public void setEffectsVolume(int effectsVolume) {
        this.effectsVolume = clampVolume(effectsVolume);
    }

    public InputBindings getInputBindings() {
        return inputBindings;
    }

    public void setInputBindings(InputBindings inputBindings) {
        this.inputBindings = inputBindings != null ? inputBindings : new InputBindings();
    }

    //A separate object with the same values, used to remember what was saved
    public Settings copyOf() {
        Settings copy = new Settings();
        copy.musicVolume = this.musicVolume;
        copy.effectsVolume = this.effectsVolume;
        copy.inputBindings = this.inputBindings.copyOf();
        return copy;
    }

    public boolean hasSameValuesAs(Settings other) {
        return other != null
                && this.musicVolume == other.musicVolume
                && this.effectsVolume == other.effectsVolume
                && this.inputBindings.hasSameValuesAs(other.inputBindings);
    }

    private static int clampVolume(int volume) {
        return Math.max(MIN_VOLUME, Math.min(MAX_VOLUME, volume));
    }
}

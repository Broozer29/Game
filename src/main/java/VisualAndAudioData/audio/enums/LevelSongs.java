package VisualAndAudioData.audio.enums;

import game.spawner.enums.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum LevelSongs {

    //Difficulty: Medium
    //Missing: Short
    Furi_Wisdom_Of_Rage(AudioEnums.Furi_Wisdowm_Of_Rage, LevelDifficulty.Medium, LevelLength.Medium),
    Furi_My_Only_Chance(AudioEnums.Furi_My_Only_Chance, LevelDifficulty.Medium, LevelLength.Long),
    Furi_Make_This_Right(AudioEnums.Furi_Make_This_Right, LevelDifficulty.Medium, LevelLength.Long),
    Knight_Something_Memorable(AudioEnums.Knight_Something_Memorable, LevelDifficulty.Medium, LevelLength.Long),
    The_Rain_Formerly_Known_As_Purple(AudioEnums.The_Rain_Formerly_Known_As_Purple, LevelDifficulty.Medium, LevelLength.Long),

    //Difficulty: Easy
    //Missing: Long
    New_Arcades_Solace(AudioEnums.New_Arcades_Solace, LevelDifficulty.Easy, LevelLength.Medium),
    Downtown_Binary_Astral(AudioEnums.Downtown_Binary_Astral, LevelDifficulty.Easy, LevelLength.Short),
    Alpha_Room_Come_Back(AudioEnums.Alpha_Room_Come_Back, LevelDifficulty.Easy, LevelLength.Short),
    Downtown_Binary_Fantasia(AudioEnums.Downtown_Binary_Fantasia, LevelDifficulty.Easy, LevelLength.Medium),
    Marvel_Golden_Dawn(AudioEnums.Marvel_Golden_Dawn, LevelDifficulty.Easy, LevelLength.Medium),
    Tonebox_Memory_Upload(AudioEnums.Tonebox_Memory_Upload, LevelDifficulty.Easy, LevelLength.Medium),
    Forhill_Iris(AudioEnums.Forhill_Iris, LevelDifficulty.Easy, LevelLength.Short),
    Tonebox_Radium_Cloud_Highway(AudioEnums.Tonebox_Radium_Cloud_Highway, LevelDifficulty.Easy, LevelLength.Medium),
    Diq_Rose(AudioEnums.Diq_Rose, LevelDifficulty.Easy, LevelLength.Short),
    Five_Seconds_Before_Sunrise(AudioEnums.Five_Seconds_Before_Sunrise, LevelDifficulty.Easy, LevelLength.Short),


    //Difficulty: Hard
    //Missing: Medium
    Apple_Holder_Remix(AudioEnums.Apple_Holder_Remix, LevelDifficulty.Hard, LevelLength.Short),
    Carpenter_Brut_Enraged(AudioEnums.Carpenter_Brut_Enraged, LevelDifficulty.Hard, LevelLength.Short),
    Carpenter_Brut_Youre_Mine(AudioEnums.Carpenter_Brut_Youre_Mine, LevelDifficulty.Hard, LevelLength.Long),
    Blood_On_The_Dancefloor(AudioEnums.Blood_On_The_Dancefloor, LevelDifficulty.Hard, LevelLength.Long),

    ;

    private AudioEnums audioEnum;
    private LevelDifficulty levelDifficulty;
    private LevelLength levelLength;

    LevelSongs(AudioEnums audioEnum, LevelDifficulty levelDifficulty, LevelLength levelLength){
        this.audioEnum = audioEnum;
        this.levelDifficulty = levelDifficulty;
        this.levelLength = levelLength;
    }

    public AudioEnums getAudioEnum () {
        return audioEnum;
    }

    public LevelDifficulty getLevelDifficulty () {
        return levelDifficulty;
    }

    public LevelLength getLevelLength () {
        return levelLength;
    }

    public static LevelSongs getRandomSong(LevelDifficulty difficulty, LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.levelLength == length)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Diq_Rose; //Default song if something goes wrong
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    public static LevelSongs getRandomSongByLength(LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelLength == length)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return null; // Or throw an exception if preferred
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    // Method 2: Get a random song by LevelDifficulty
    public static LevelSongs getRandomSongByDifficulty(LevelDifficulty difficulty) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return null; // Or throw an exception if preferred
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    // Method 3: Get a random song without any specific criteria
    public static LevelSongs getRandomSong() {
        LevelSongs[] songs = LevelSongs.values();
        return songs[new Random().nextInt(songs.length)];
    }
}

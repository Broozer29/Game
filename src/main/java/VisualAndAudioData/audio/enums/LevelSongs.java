package VisualAndAudioData.audio.enums;

import VisualAndAudioData.image.ImageEnums;
import game.spawner.enums.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum LevelSongs {

    //Difficulty: Medium
    //Missing: Short
    Furi_Wisdom_Of_Rage(AudioEnums.Furi_Wisdowm_Of_Rage, LevelDifficulty.Medium, LevelLength.Medium, true),
    Furi_My_Only_Chance(AudioEnums.Furi_My_Only_Chance, LevelDifficulty.Medium, LevelLength.Long, true),
    Furi_Make_This_Right(AudioEnums.Furi_Make_This_Right, LevelDifficulty.Medium, LevelLength.Long, true),
    Knight_Something_Memorable(AudioEnums.Knight_Something_Memorable, LevelDifficulty.Medium, LevelLength.Long, true),
    The_Rain_Formerly_Known_As_Purple(AudioEnums.The_Rain_Formerly_Known_As_Purple, LevelDifficulty.Medium, LevelLength.Long, true),

    //Difficulty: Easy
    //Missing: Long (temporary chills placed here)
    New_Arcades_Solace(AudioEnums.New_Arcades_Solace, LevelDifficulty.Easy, LevelLength.Medium, true),
    Robert_Nickson_Painting_The_Skies(AudioEnums.Robert_Nickson_Painting_The_Skies, LevelDifficulty.Easy, LevelLength.Short, true),
    Downtown_Binary_Astral(AudioEnums.Downtown_Binary_Astral, LevelDifficulty.Easy, LevelLength.Short, true),
    Alpha_Room_Come_Back(AudioEnums.Alpha_Room_Come_Back, LevelDifficulty.Easy, LevelLength.Short, true),
    Downtown_Binary_Fantasia(AudioEnums.Downtown_Binary_Fantasia, LevelDifficulty.Easy, LevelLength.Medium, true),
    Marvel_Golden_Dawn(AudioEnums.Marvel_Golden_Dawn, LevelDifficulty.Easy, LevelLength.Medium, true),
    Tonebox_Memory_Upload(AudioEnums.Tonebox_Memory_Upload, LevelDifficulty.Easy, LevelLength.Medium, true),
    Forhill_Iris(AudioEnums.Forhill_Iris, LevelDifficulty.Easy, LevelLength.Short, true),
    Tonebox_Radium_Cloud_Highway(AudioEnums.Tonebox_Radium_Cloud_Highway, LevelDifficulty.Easy, LevelLength.Medium, true),
    Diq_Rose(AudioEnums.Diq_Rose, LevelDifficulty.Easy, LevelLength.Short, true),
    Five_Seconds_Before_Sunrise(AudioEnums.Five_Seconds_Before_Sunrise, LevelDifficulty.Easy, LevelLength.Short, true),
    Le_Youth_Chills(AudioEnums.Le_Youth_Chills, LevelDifficulty.Easy, LevelLength.Long, true),


    //Difficulty: Hard
    //Missing: Medium
    Apple_Holder_Remix(AudioEnums.Apple_Holder_Remix, LevelDifficulty.Hard, LevelLength.Short, true),
    Carpenter_Brut_Enraged(AudioEnums.Carpenter_Brut_Enraged, LevelDifficulty.Hard, LevelLength.Short, true),
    Carpenter_Brut_Youre_Mine(AudioEnums.Carpenter_Brut_Youre_Mine, LevelDifficulty.Hard, LevelLength.Long, true),
    Blood_On_The_Dancefloor(AudioEnums.Blood_On_The_Dancefloor, LevelDifficulty.Hard, LevelLength.Long, true),

    ;

    private AudioEnums audioEnum;
    private LevelDifficulty levelDifficulty;
    private LevelLength levelLength;
    private boolean enabled;

    LevelSongs (AudioEnums audioEnum, LevelDifficulty levelDifficulty, LevelLength levelLength, boolean enabled) {
        this.audioEnum = audioEnum;
        this.levelDifficulty = levelDifficulty;
        this.levelLength = levelLength;
        this.enabled = enabled;
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

    public static LevelSongs getRandomSong (LevelDifficulty difficulty, LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.levelLength == length && song.enabled)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Diq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    public static LevelSongs getRandomSongByLength (LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelLength == length && song.enabled)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Diq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    // Method 2: Get a random song by LevelDifficulty
    public static LevelSongs getRandomSongByDifficulty (LevelDifficulty difficulty) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.enabled)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Diq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    // Method 3: Get a random song without any specific criteria
    public static LevelSongs getRandomSong () {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.enabled)
                .collect(Collectors.toList());

        if (filteredSongs.isEmpty()) {
            return Diq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size()));
    }

    public static int getDifficultyImageIndex (LevelDifficulty difficulty, LevelLength length) {
        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = length.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        int combinedScore = difficultyWeight + lengthWeight;

        // Adjusted mapping based on combined score
        if (combinedScore <= 2) return 1; // Image 1
        if (combinedScore == 3) return 2; // Image 2
        if (combinedScore == 4) return 3; // Image 3
        if (combinedScore == 5) return 4; // Image 4
        return 5; // Image 5
    }

    public static int getDifficultyScore(LevelDifficulty difficulty, LevelLength length){
        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = length.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        return difficultyWeight + lengthWeight;
    }

    public static ImageEnums getImageEnumByDifficultyScore (int difficultyScore) {
        if (difficultyScore <= 2) return ImageEnums.PurpleWings1; // Image 1
        if (difficultyScore == 3) return ImageEnums.PurpleWings2; // Image 2
        if (difficultyScore == 4) return ImageEnums.PurpleWings3; // Image 3
        if (difficultyScore == 5) return ImageEnums.PurpleWings4; // Image 4
        return ImageEnums.PurpleWings5; // Image 5
    }
}

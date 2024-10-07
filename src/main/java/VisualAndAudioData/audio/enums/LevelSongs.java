package VisualAndAudioData.audio.enums;

import VisualAndAudioData.image.ImageEnums;
import game.level.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum LevelSongs {

    //Difficulty: Easy
    //--------------Short
    Viq_Rose(AudioEnums.Viq_Rose, LevelDifficulty.Easy, LevelLength.Short, true),
    Downtown_Binary_Astral(AudioEnums.Downtown_Binary_Astral, LevelDifficulty.Easy, LevelLength.Short, true),
    Alpha_Room_Come_Back(AudioEnums.Alpha_Room_Come_Back, LevelDifficulty.Easy, LevelLength.Short, true),

    //--------------Medium
    Viq_Girl_From_Nowhere(AudioEnums.Viq_Girl_From_Nowhere, LevelDifficulty.Easy, LevelLength.Medium, true),
    Downtown_Binary_Fantasia(AudioEnums.Downtown_Binary_Fantasia, LevelDifficulty.Easy, LevelLength.Medium, true),
    Downtown_Light_Cycles(AudioEnums.Downtown_Binary_Light_Cycles, LevelDifficulty.Easy, LevelLength.Medium, true),
    Marvel_Golden_Dawn(AudioEnums.Marvel_Golden_Dawn, LevelDifficulty.Easy, LevelLength.Medium, true),
    Tonebox_Memory_Upload(AudioEnums.Tonebox_Memory_Upload, LevelDifficulty.Easy, LevelLength.Medium, true),
    Forhill_Iris(AudioEnums.Forhill_Iris, LevelDifficulty.Easy, LevelLength.Medium, true),
    Tonebox_Radium_Cloud_Highway(AudioEnums.Tonebox_Radium_Cloud_Highway, LevelDifficulty.Easy, LevelLength.Medium, true),
    Cannons_Fire_For_You(AudioEnums.Cannons_Fire_For_You, LevelDifficulty.Easy, LevelLength.Medium, true),
    EMBRZ_Rain_On_My_Window(AudioEnums.EMBRZ_Rain_On_My_Window, LevelDifficulty.Easy, LevelLength.Medium, false),
    Space_Sailors_Cosmos(AudioEnums.Space_Sailors_Cosmos, LevelDifficulty.Easy, LevelLength.Medium, true),

    //--------------Long
    Ghost_Data_Dark_Harvest(AudioEnums.Ghost_Data_Dark_Harvest, LevelDifficulty.Easy, LevelLength.Long, true),


    //Difficulty: Medium
    //--------------Short
    Robert_Nickson_Painting_The_Skies(AudioEnums.Robert_Nickson_Painting_The_Skies, LevelDifficulty.Medium, LevelLength.Short, true),

    //--------------Medium
    Five_Seconds_Before_Sunrise(AudioEnums.Five_Seconds_Before_Sunrise, LevelDifficulty.Medium, LevelLength.Medium, true),
    New_Arcades_Solace(AudioEnums.New_Arcades_Solace, LevelDifficulty.Medium, LevelLength.Medium, true),
    EMBRZ_Light_Falls(AudioEnums.EMBRZ_Light_Falls, LevelDifficulty.Medium, LevelLength.Medium, true),
    New_Arcades_Severed(AudioEnums.New_Arcades_Severed, LevelDifficulty.Medium, LevelLength.Medium, true),

    //--------------Long
    The_Rain_Formerly_Known_As_Purple(AudioEnums.The_Rain_Formerly_Known_As_Purple, LevelDifficulty.Medium, LevelLength.Long, true),
    Le_Youth_Chills(AudioEnums.Le_Youth_Chills, LevelDifficulty.Medium, LevelLength.Long, true),
    ApproachingNirvanaWorthAThousandPictures(AudioEnums.ApproachingNirvanaThousandPictures, LevelDifficulty.Medium, LevelLength.Long, true),
    ApproachingNirvanaNoStringsAttached(AudioEnums.ApproachingNirvanaNoStringsAttached, LevelDifficulty.Medium, LevelLength.Long, true),


    //Difficulty: Hard
    //--------------Short
    Apple_Holder_Remix(AudioEnums.Apple_Holder_Remix, LevelDifficulty.Hard, LevelLength.Short, true),
    Deadmau5_Monophobia(AudioEnums.Deadmau5_Monophobia, LevelDifficulty.Hard, LevelLength.Short, true),
    Carpenter_Brut_Enraged(AudioEnums.Carpenter_Brut_Enraged, LevelDifficulty.Hard, LevelLength.Short, true), //Starts 1,5 minutes later

    //--------------Medium
    Johny_Theme(AudioEnums.Johny_Theme, LevelDifficulty.Hard, LevelLength.Medium, false),
    Arksun_Arisen(AudioEnums.Arksun_Arisen, LevelDifficulty.Hard, LevelLength.Medium, true),
    Furi_Wisdom_Of_Rage(AudioEnums.Furi_Wisdowm_Of_Rage, LevelDifficulty.Hard, LevelLength.Medium, true),
    blackGummySuperHuman(AudioEnums.BlackGummy_SuperHuman, LevelDifficulty.Hard, LevelLength.Medium, false),
    Maduk_Alone(AudioEnums.Maduk_Alone, LevelDifficulty.Hard, LevelLength.Medium, false),

    //--------------Long
    Furi_My_Only_Chance(AudioEnums.Furi_My_Only_Chance, LevelDifficulty.Hard, LevelLength.Long, false),
    Furi_Make_This_Right(AudioEnums.Furi_Make_This_Right, LevelDifficulty.Hard, LevelLength.Long, true),
    Knight_Something_Memorable(AudioEnums.Knight_Something_Memorable, LevelDifficulty.Hard, LevelLength.Long, true),
    Carpenter_Brut_Youre_Mine(AudioEnums.Carpenter_Brut_Youre_Mine, LevelDifficulty.Hard, LevelLength.Long, true),
    Blood_On_The_Dancefloor(AudioEnums.Blood_On_The_Dancefloor, LevelDifficulty.Hard, LevelLength.Long, true),
    Ghost_Data_Gods_Of_The_Artificial(AudioEnums.Ghost_Data_Gods_Of_The_Artificial, LevelDifficulty.Hard, LevelLength.Long, true),
    ;

    private AudioEnums audioEnum;
    private LevelDifficulty levelDifficulty;
    private LevelLength levelLength;
    //Short: 0 - 3 minutes
    //Medium: 3-5 minutes
    //Long: >5 minutes
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

    public static AudioEnums getRandomSong (LevelDifficulty difficulty, LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.levelLength == length && song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for " + length + " AND " + difficulty + "returning default track");
            return Viq_Rose.getAudioEnum();
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
    }

    public static AudioEnums getRandomSongByLength (LevelLength length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelLength == length && song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for " + length + " returning default track");
            return Viq_Rose.getAudioEnum();
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
    }

    // Method 2: Get a random song by LevelDifficulty
    public static AudioEnums getRandomSongByDifficulty (LevelDifficulty difficulty) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for " + difficulty + " returning default track");
            return Viq_Rose.getAudioEnum();
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
    }

    // Method 3: Get a random song without any specific criteria
    public static AudioEnums getRandomSong () {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for RANDOM returning default track");
            return Viq_Rose.getAudioEnum();
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
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

    public static int getDifficultyScore (LevelDifficulty difficulty, LevelLength length) {
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

    public static AudioEnums getRandomBossSong () {
        List<AudioEnums> bossSongList = new ArrayList<>();
//        bossSongList.add(AudioEnums.BossBattle);
        bossSongList.add(AudioEnums.Blood_On_The_Dancefloor);
//        bossSongList.add(AudioEnums.WaveshaperMonster);

        return bossSongList.get(new Random().nextInt(bossSongList.size()));
    }


}

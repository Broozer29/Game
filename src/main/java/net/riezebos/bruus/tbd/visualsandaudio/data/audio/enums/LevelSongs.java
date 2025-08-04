package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


//DEPRECATED, the whole functionality for LevelSongs is to be rebuild into something else.
public enum LevelSongs {
    ;

    private AudioEnums audioEnum;
    private LevelDifficulty levelDifficulty;
    private MiniBossConfig miniBossConfig;
    private boolean enabled;

    LevelSongs (AudioEnums audioEnum, LevelDifficulty levelDifficulty, MiniBossConfig miniBossConfig, boolean enabled) {
        this.audioEnum = audioEnum;
        this.levelDifficulty = levelDifficulty;
        this.miniBossConfig = miniBossConfig;
        this.enabled = enabled;
    }

    public AudioEnums getAudioEnum () {
        return audioEnum;
    }

    public static AudioEnums getRandomSong (LevelDifficulty difficulty, MiniBossConfig length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.levelDifficulty == difficulty && song.miniBossConfig == length && song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for " + length + " AND " + difficulty + "returning default track");
            return AudioEnums.Viq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
    }

    //DEPRECATED, DO NOT USE
    public static AudioEnums getRandomSongByLength (MiniBossConfig length) {
        List<LevelSongs> filteredSongs = Arrays.stream(LevelSongs.values())
                .filter(song -> song.miniBossConfig == length && song.enabled)
                .toList();

        if (filteredSongs.isEmpty()) {
            System.out.println("Nothing found for " + length + " returning default track");
            return AudioEnums.Viq_Rose;
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
            return AudioEnums.Viq_Rose;
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
            return AudioEnums.Viq_Rose;
        }

        return filteredSongs.get(new Random().nextInt(filteredSongs.size())).getAudioEnum();
    }

    public static int getDifficultyImageIndex (LevelDifficulty difficulty, MiniBossConfig miniBossConfig) {
        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = miniBossConfig.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        int combinedScore = difficultyWeight + lengthWeight;

        // Adjusted mapping based on combined score
        if (combinedScore <= 2) return 1; // Image 1
        if (combinedScore == 3) return 2; // Image 2
        if (combinedScore == 4) return 3; // Image 3
        if (combinedScore == 5) return 4; // Image 4
        return 5; // Image 5
    }

    public static int getDifficultyScore (LevelDifficulty difficulty, MiniBossConfig miniBossConfig) {
        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = miniBossConfig.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        return difficultyWeight + lengthWeight;
    }

    public static int getDifficultyScoreByDifficultyOnly(LevelDifficulty difficulty){
        return (difficulty.ordinal() + 1) * 2; // Assuming Enum order is EASY, MEDIUM, HARD
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
        bossSongList.add(AudioEnums.BossBattle);
        bossSongList.add(AudioEnums.Blood_On_The_Dancefloor);
        bossSongList.add(AudioEnums.WaveshaperMonster);
        bossSongList.add(AudioEnums.RiskOfDanger);

        return bossSongList.get(new Random().nextInt(bossSongList.size()));
    }

    public static AudioEnums getBossTheme (EnemyEnums enemyEnums) {
        switch (enemyEnums) {
            case SpaceStationBoss:
                return AudioEnums.Furi_Wisdowm_Of_Rage;
            case RedBoss:
                return AudioEnums.Blood_On_The_Dancefloor;
            case CarrierBoss:
                return AudioEnums.nomad;
            case YellowBoss:
                return AudioEnums.keygen;
            default:
                return getRandomBossSong();
        }
    }

}

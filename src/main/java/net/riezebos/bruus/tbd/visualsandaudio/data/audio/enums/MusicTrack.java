package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

//Every song of the game: where its file is, what it is called, where it plays and which boss it belongs to.
//Titles and artists are derived from the file names until they are filled in by hand.
public enum MusicTrack {
    FIVE_SECONDS("/audio/music/custom/5 seconds.mp3", "5 seconds", "", MusicPlace.LEVEL),
    AFTERGLOW("/audio/music/custom/Afterglow.mp3", "Afterglow", "", MusicPlace.LEVEL),
    ASTRAL("/audio/music/custom/Astral.mp3", "Astral", "", MusicPlace.LEVEL),
    COSMOS("/audio/music/custom/Cosmos.mp3", "Cosmos", "", MusicPlace.LEVEL),
    DEPTH("/audio/music/custom/Depth.mp3", "Depth", "", MusicPlace.LEVEL),
    DOWNTOWN_BINARY_8MB("/audio/music/custom/Downtown Binary - 8MB.mp3", "8MB", "Downtown Binary", MusicPlace.LEVEL),
    DOWNTOWN_BINARY_FANTASIA("/audio/music/custom/Downtown Binary - Fantasia.mp3", "Fantasia", "Downtown Binary", MusicPlace.LEVEL),
    DOWNTOWN_BINARY_LIGHT_CYCLES("/audio/music/custom/Downtown Binary - Light Cycles.mp3", "Light Cycles", "Downtown Binary", MusicPlace.LEVEL),
    DOWNTOWN_BINARY_LOST("/audio/music/custom/Downtown Binary - Lost.mp3", "Lost", "Downtown Binary", MusicPlace.LEVEL),
    DOWNTOWN_BINARY_OORT("/audio/music/custom/Downtown Binary - Oort.mp3", "Oort", "Downtown Binary", MusicPlace.LEVEL),
    ECLIPSE("/audio/music/custom/Eclipse.mp3", "Eclipse", "", MusicPlace.LEVEL),
    FLUX("/audio/music/custom/Flux.mp3", "Flux", "", MusicPlace.LEVEL),
    FORHILL_IRIS("/audio/music/custom/Forhill-Iris.mp3", "Forhill-Iris", "", MusicPlace.LEVEL),
    KISS_OF_THE_CREATURE("/audio/music/custom/Kiss of the Creature.mp3", "Kiss of the Creature", "", MusicPlace.LEVEL),
    MEMORY_UPLOADED("/audio/music/custom/Memory-Uploaded.mp3", "Memory-Uploaded", "", MusicPlace.LEVEL),
    NEW_ARCADES_SOLACE("/audio/music/custom/New Arcades - Solace.mp3", "Solace", "New Arcades", MusicPlace.LEVEL),
    NOVA("/audio/music/custom/Nova.mp3", "Nova", "", MusicPlace.LEVEL),
    PAINTING_THE_SKIES("/audio/music/custom/Painting the skies.mp3", "Painting the skies", "", MusicPlace.LEVEL),
    RADIUM("/audio/music/custom/Radium.mp3", "Radium", "", MusicPlace.LEVEL),
    RECOMPILE("/audio/music/custom/Recompile.mp3", "Recompile", "", MusicPlace.LEVEL),
    ROSE("/audio/music/custom/Rose.mp3", "Rose", "", MusicPlace.LEVEL),
    SEVERED("/audio/music/custom/Severed.mp3", "Severed", "", MusicPlace.LEVEL),
    TURISMO("/audio/music/custom/Turismo.mp3", "Turismo", "", MusicPlace.LEVEL),
    VIQ_GIRL_FROM_NOWHERE("/audio/music/custom/VIQ - Girl from Nowhere.mp3", "Girl from Nowhere", "VIQ", MusicPlace.LEVEL),
    RUNNER_TWO("/audio/music/custom/runner two.mp3", "runner two", "", MusicPlace.LEVEL),
    VIQ_ALISON("/audio/music/custom/viq alison.mp3", "viq alison", "", MusicPlace.LEVEL),
    WAVESHAPER_WISDOM_OF_RAGE("/audio/music/Waveshaper wisdom of rage.mp3", "Waveshaper wisdom of rage", "", MusicPlace.BOSS, EnemyEnums.SpaceStationBoss, 0, true),
    RM_CHASE("/audio/music/RM Chase.mp3", "RM Chase", "", MusicPlace.BOSS, EnemyEnums.RedBoss, 0, false),
    NOMAD("/audio/music/nomad.mp3", "nomad", "", MusicPlace.BOSS, EnemyEnums.CarrierBoss, 0, true),
    FILTH("/audio/music/Filth.mp3", "Filth", "", MusicPlace.BOSS, EnemyEnums.YellowBoss, 0, false),
    ARISEN("/audio/music/Arisen.mp3", "Arisen", "", MusicPlace.BOSS, EnemyEnums.StrikerBoss, 0, false),
    MAUSOLEUM_MASH("/audio/music/Mausoleum mash.mp3", "Mausoleum mash", "", MusicPlace.BOSS, EnemyEnums.BlueBoss, 0, false),
    ENRAGED("/audio/music/enraged.mp3", "enraged", "", MusicPlace.BOSS, EnemyEnums.TwinBoss, 0, false),
    FINAL_BOSS_PHASE_1("/audio/music/phase1.mp3", "phase1", "", MusicPlace.FINAL_BOSS, EnemyEnums.FinalBoss, 1, false),
    FINAL_BOSS_PHASE_2("/audio/music/phase2.mp3", "phase2", "", MusicPlace.FINAL_BOSS, EnemyEnums.FinalBoss, 2, false),
    FINAL_BOSS_PHASE_3("/audio/music/phase3.mp3", "phase3", "", MusicPlace.FINAL_BOSS, EnemyEnums.FinalBoss, 3, false),
    MAIN_MENU("/audio/music/mainmenu.mp3", "mainmenu", "", MusicPlace.MAIN_MENU, null, 0, false),
    SHOP("/audio/music/lemmino Firecracker.mp3", "lemmino Firecracker", "", MusicPlace.SHOP, null, 0, false),
    GAME_OVER("/audio/music/Vendla - Sonrisa (Royalty Free Music).mp3", "Sonrisa (Royalty Free Music)", "Vendla", MusicPlace.GAME_OVER, null, 0, false),
    BLOOD_ON_THE_DANCEFLOOR("/audio/music/Blood_On_The_Dancefloor.mp3", "Blood On The Dancefloor", "", MusicPlace.FALLBACK, null, 0, true),
    WAVESHAPER_MONSTER("/audio/music/Waveshaper - Monster.mp3", "Monster", "Waveshaper", MusicPlace.FALLBACK, null, 0, true);

    private static final int LEVEL_HISTORY_SIZE = 10; //A level song never repeats one of the last picks
    private static final LinkedList<MusicTrack> recentLevelTracks = new LinkedList<>();
    private static final Random random = new Random();

    private final String path;
    private final String title;
    private final String artist;
    private final MusicPlace place;
    private final EnemyEnums boss; //Only for BOSS and FINAL_BOSS songs
    private final int phase; //Only for FINAL_BOSS songs, 1 to 3
    private final boolean inFallbackPool;

    MusicTrack(String path, String title, String artist, MusicPlace place) {
        this(path, title, artist, place, null, 0, false);
    }

    MusicTrack(String path, String title, String artist, MusicPlace place, EnemyEnums boss, int phase, boolean inFallbackPool) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.place = place;
        this.boss = boss;
        this.phase = phase;
        this.inFallbackPool = inFallbackPool;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public MusicPlace getPlace() {
        return place;
    }

    public EnemyEnums getBoss() {
        return boss;
    }

    public int getPhase() {
        return phase;
    }

    public boolean isInFallbackPool() {
        return inFallbackPool;
    }

    public static MusicTrack getBossTheme(EnemyEnums enemyEnums) {
        for (MusicTrack track : values()) {
            if (track.boss == enemyEnums && (track.place == MusicPlace.BOSS || (track.place == MusicPlace.FINAL_BOSS && track.phase == 1))) {
                return track;
            }
        }
        return getRandomFallback();
    }

    public static MusicTrack getFinalBossPhase(int phase) {
        for (MusicTrack track : values()) {
            if (track.place == MusicPlace.FINAL_BOSS && track.phase == phase) {
                return track;
            }
        }
        return null;
    }

    public static MusicTrack getRandomFallback() {
        List<MusicTrack> fallbackTracks = new ArrayList<>();
        for (MusicTrack track : values()) {
            if (track.inFallbackPool) {
                fallbackTracks.add(track);
            }
        }
        return fallbackTracks.get(random.nextInt(fallbackTracks.size()));
    }

    //Picks a LEVEL song that is not one of the last 10 picks
    public static synchronized MusicTrack getRandomLevelTrack() {
        List<MusicTrack> candidates = new ArrayList<>();
        for (MusicTrack track : values()) {
            if (track.place == MusicPlace.LEVEL && !recentLevelTracks.contains(track)) {
                candidates.add(track);
            }
        }
        MusicTrack picked = candidates.get(random.nextInt(candidates.size()));
        recentLevelTracks.add(picked);
        if (recentLevelTracks.size() > LEVEL_HISTORY_SIZE) {
            recentLevelTracks.removeFirst();
        }
        return picked;
    }
}

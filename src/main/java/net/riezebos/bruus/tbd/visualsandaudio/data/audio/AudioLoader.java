package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AudioLoader {

    private static AudioLoader instance = new AudioLoader();
    private final LinkedList<String> recentTracks = new LinkedList<>();
    private static final int MAX_HISTORY_SIZE = 10;
    private static final int MAX_RETRIES = 10;

    private AudioLoader () {

    }

    public static AudioLoader getInstance () {
        return instance;
    }

    // Load the audio file into memory using MediaPlayer for small audio files
    public MediaPlayer getSoundfile(AudioEnums audioFile) {
        try {
            URL url = getClass().getResource(convertAudioToFileString(audioFile));
            if (url != null) {
                Media media = new Media(url.toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(false); // Prevent auto-playing the sound
                return mediaPlayer;
            }
            System.out.println("Failed to load audio file: " + audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Helper method to get a random custom music file.
     * Falls back to JAR resources if external folder is empty/missing.
     * Prevents repeating recently played tracks by maintaining a history of the last 10 tracks.
     * @return The path/URL to a random custom music file or fallback boss song
     */
    private String getRandomCustomMusicFile() {
        List<Object> availableTracks = new ArrayList<>(); // Can hold File or String (resource path)
        boolean isExternalFolder = false;

        // If no external tracks found, load from JAR resources
        if (availableTracks.isEmpty()) {
            try {
                InputStream tracksListStream = getClass().getResourceAsStream("/audio/music/custom/tracks.txt");
                if (tracksListStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(tracksListStream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (!line.isEmpty() && line.toLowerCase().endsWith(".wav")) {
                                availableTracks.add("/audio/music/custom/" + line);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error accessing JAR custom music tracks: " + e.getMessage());
            }
        }

        // Select a random track with history tracking
        if (!availableTracks.isEmpty()) {
            Random random = new Random();
            Object selectedTrack = null;
            String trackIdentifier = null;
            int attempts = 0;

            // Try to find a track not in recent history
            while (attempts < MAX_RETRIES) {
                selectedTrack = availableTracks.get(random.nextInt(availableTracks.size()));
                trackIdentifier = selectedTrack instanceof java.io.File
                    ? ((java.io.File) selectedTrack).getName()
                    : (String) selectedTrack;

                if (!recentTracks.contains(trackIdentifier)) {
                    break;
                }
                attempts++;
            }

            // If we couldn't find a non-recent track after MAX_RETRIES, clear history
            if (attempts >= MAX_RETRIES) {
                recentTracks.clear();
                selectedTrack = availableTracks.get(random.nextInt(availableTracks.size()));
                trackIdentifier = selectedTrack instanceof java.io.File
                    ? ((java.io.File) selectedTrack).getName()
                    : (String) selectedTrack;
            }

            // Add to history
            recentTracks.add(trackIdentifier);

            // Maintain max history size (FIFO - remove oldest if exceeds limit)
            if (recentTracks.size() > MAX_HISTORY_SIZE) {
                recentTracks.removeFirst();
            }

            // Return appropriate path/URL
            if (isExternalFolder && selectedTrack instanceof java.io.File) {
                return ((java.io.File) selectedTrack).toURI().toString();
            } else {
                return (String) selectedTrack;
            }
        }

        // Fallback to random boss song if no custom tracks available
        return convertAudioToFileString(AudioEnums.getRandomBossSong());
    }

    public String convertAudioToFileString (AudioEnums audioFile) {
        if(audioFile == AudioEnums.CustomMusicFile){
            return getRandomCustomMusicFile();
        }


        switch (audioFile) {
            case CaptainMisc0: return "/audio/selectclass/captain/misc0.wav";
            case CaptainMisc1: return "/audio/selectclass/captain/misc1.wav";
            case CaptainRdy0: return "/audio/selectclass/captain/rdy0.wav";
            case CaptainWhat0: return "/audio/selectclass/captain/what0.wav";
            case CaptainWhat1: return "/audio/selectclass/captain/what1.wav";
            case CaptainWhat2: return "/audio/selectclass/captain/what2.wav";
            case CaptainWhat3: return "/audio/selectclass/captain/what3.wav";
            case CaptainYes0: return "/audio/selectclass/captain/yes0.wav";
            case CaptainYes1: return "/audio/selectclass/captain/yes1.wav";
            case CaptainYes2: return "/audio/selectclass/captain/yes2.wav";

            case CarrierRdy0: return "/audio/selectclass/carrier/rdy0.wav";
            case CarrierWhat0: return "/audio/selectclass/carrier/what0.wav";
            case CarrierWhat1: return "/audio/selectclass/carrier/what1.wav";
            case CarrierYes0: return "/audio/selectclass/carrier/yes0.wav";
            case CarrierYes1: return "/audio/selectclass/carrier/yes1.wav";
            case CarrierYes2: return "/audio/selectclass/carrier/yes2.wav";
            case CarrierYes3: return "/audio/selectclass/carrier/yes3.wav";

            case FireFighterMisc0: return "/audio/selectclass/firefighter/misc0.wav";
            case FireFighterMisc1: return "/audio/selectclass/firefighter/misc1.wav";
            case FireFighterYes0: return "/audio/selectclass/firefighter/yes0.wav";
            case FireFighterYes1: return "/audio/selectclass/firefighter/yes1.wav";
            case FireFighterYes2: return "/audio/selectclass/firefighter/yes2.wav";
            case FireFighterYes3: return "/audio/selectclass/firefighter/yes3.wav";


            case ClassCarrierSlowingDown: return "/audio/carrierslow.wav";
            case ClassCarrierSpeedingUp: return "/audio/carrierfast.wav";
            case ProtossShipDeath: return "/audio/ProtossShipDeath.wav";
            case QueenDeath: return "/audio/Zerg/QueenDeath.wav";
            case MutaliskBirth: return "/audio/Zerg/MutaliskBirth.wav";
            case MutaliskDeath: return "/audio/Zerg/MutaliskDeath.wav";
            case DevourerBirth: return "/audio/Zerg/DevourerBirth.wav";
            case DevourerDeath: return "/audio/Zerg/DevourerDeath.wav";
            case DevourerHit: return "/audio/Zerg/DevourerHit.wav";
            case ScourgeCollision: return "/audio/Zerg/ScourgeCollision.wav";
            case ScourgeDeath: return "/audio/Zerg/ScourgeDeath.wav";
            case ScourgeNoticed: return "/audio/Zerg/ScourgeNoticed.wav";
            case GuardianBirth: return "/audio/Zerg/GuardianBirth.wav";
            case GuardianDeath: return "/audio/Zerg/GuardianDeath.wav";
            case BroodlingAttached: return "/audio/Zerg/BroodlingAttached.wav";
            case OverlordDeath: return "/audio/Zerg/OverlordDeath.wav";
            case FinalBossMock1: return "/audio/finalbossbarks/mock1.wav";
            case FinalBossMock2: return "/audio/finalbossbarks/mock2.wav";
            case FinalBossMock3: return "/audio/finalbossbarks/mock3.wav";
            case FinalBossMock4: return "/audio/finalbossbarks/mock4.wav";
            case FinalBossMock5: return "/audio/finalbossbarks/mock5.wav";
            case FinalBossMock6: return "/audio/finalbossbarks/mock6.wav";
            case FinalBossMock7: return "/audio/finalbossbarks/mock7.wav";
            case FinalBossMock8: return "/audio/finalbossbarks/mock8.wav";
            case FinalBossGreeting: return "/audio/finalbossbarks/greeting.wav";
            case FinalBossTaunt1: return "/audio/finalbossbarks/taunt1.wav";
            case FinalBossTaunt2: return "/audio/finalbossbarks/taunt2.wav";
            case FinalBossTaunt3: return "/audio/finalbossbarks/taunt3.wav";
            case FinalBossTaunt4WhenLowHp: return "/audio/finalbossbarks/taunt4.wav";
            case FinalBossPhaseTransition1: return "/audio/finalbossbarks/phasetransition1.wav";
            case FinalBossPhaseTransition2: return "/audio/finalbossbarks/phasetransition2.wav";
            case FinalBossPhaseTransition3: return "/audio/finalbossbarks/phasetransition3.wav";
            case FinalBossPhaseTransition4: return "/audio/finalbossbarks/phasetransition4.wav";
            case FinalBossPhaseTransition5: return "/audio/finalbossbarks/phasetransition5.wav";
            case FinalBossOh: return "/audio/finalbossbarks/Oh.wav";
            case FinalBossManyBulletsTaunt1: return "/audio/finalbossbarks/manybulletstaunt1.wav";
            case FinalBossManyBulletsTaunt2: return "/audio/finalbossbarks/manybulletstaunt2.wav";
            case FinalBossManyBulletsTaunt3: return "/audio/finalbossbarks/manybulletstaunt3.wav";
            case FinalBossFemaleGreeting: return "/audio/finalbossbarksfemale/greeting1.mp3";
            case FinalBossFemaleIdk1: return "/audio/finalbossbarksfemale/idk.mp3";
            case FinalBossFemaleIdk2: return "/audio/finalbossbarksfemale/idk2.mp3";
            case FinalBossFemaleMock1: return "/audio/finalbossbarksfemale/mock1.mp3";
            case FinalBossFemaleMock2: return "/audio/finalbossbarksfemale/mock2.mp3";
            case FinalBossFemaleMock3: return "/audio/finalbossbarksfemale/mock3.mp3";
            case FinalBossFemaleMock4: return "/audio/finalbossbarksfemale/mock4.mp3";
            case FinalBossFemaleMock5: return "/audio/finalbossbarksfemale/mock5.mp3";
            case FinalBossFemalePhaseTransition1: return "/audio/finalbossbarksfemale/phasetransition1.mp3";
            case FinalBossFemalePhaseTransition2: return "/audio/finalbossbarksfemale/phasetransition2.mp3";
            case FinalBossFemalePhaseTransition3: return "/audio/finalbossbarksfemale/phasetransition3.mp3";
            case FinalBossFemalePhaseTransition4: return "/audio/finalbossbarksfemale/phasetransition4.mp3";
            case FinalBossFemalePhaseTransition5: return "/audio/finalbossbarksfemale/phasetransition5.mp3";
            case FinalBossFemaleTaunt1: return "/audio/finalbossbarksfemale/taunt1.mp3";
            case FinalBossFemaleTaunt2: return "/audio/finalbossbarksfemale/taunt2.mp3";
            case FinalBossFemaleTaunt3: return "/audio/finalbossbarksfemale/taunt3.mp3";
            case FinalBossFemaleTaunt4: return "/audio/finalbossbarksfemale/taunt4.mp3";
            case FinalBossFemaleTaunt5: return "/audio/finalbossbarksfemale/taunt5.mp3";
            case FinalBossFemaleTaunt6: return "/audio/finalbossbarksfemale/taunt6.mp3";
            case FinalBossFemaleTaunt7: return "/audio/finalbossbarksfemale/taunt7.mp3";
            case AlarakBossDeath1: return "/audio/alarak/boss death/bossdeath1.mp3";
            case AlarakBossDeath2: return "/audio/alarak/boss death/bossdeath2.mp3";
            case AlarakBossDeath3: return "/audio/alarak/boss death/bossdeath3.mp3";
            case AlarakBossDeath4: return "/audio/alarak/boss death/bossdeath4.mp3";
            case AlarakBossDeath5: return "/audio/alarak/boss death/bossdeath5.mp3";
            case AlarakBossDeath6: return "/audio/alarak/boss death/bossdeath6.mp3";
            case AlarakBossDeath7: return "/audio/alarak/boss death/bossdeath7.mp3";
            case AlarakBossDeath8: return "/audio/alarak/boss death/bossdeath8.mp3";
            case AlarakBossDeath9: return "/audio/alarak/boss death/bossdeath9.mp3";
            case AlarakBossDeath10: return "/audio/alarak/boss death/bossdeath10.mp3";
            case AlarakBossDeath11: return "/audio/alarak/boss death/bossdeath11.mp3";
            case AlarakBossDeath12: return "/audio/alarak/boss death/bossdeath12.mp3";
            case AlarakBossDeath13: return "/audio/alarak/boss death/bossdeath13.mp3";
            case AlarakBossDeath14: return "/audio/alarak/boss death/bossdeath14.mp3";
            case AlarakBossDeath15: return "/audio/alarak/boss death/bossdeath15.mp3";
            case AlarakBossSpawn1: return "/audio/alarak/boss spawn/bossspawn1.mp3";
            case AlarakBossSpawn2: return "/audio/alarak/boss spawn/bossspawn2.mp3";
            case AlarakBossSpawn3: return "/audio/alarak/boss spawn/bossspawn3.mp3";
            case AlarakBossSpawn4: return "/audio/alarak/boss spawn/bossspawn4.mp3";
            case AlarakBossSpawn5: return "/audio/alarak/boss spawn/bossspawn5.mp3";
            case AlarakBossSpawn6: return "/audio/alarak/boss spawn/bossspawn6.mp3";
            case AlarakBossSpawn7: return "/audio/alarak/boss spawn/bossspawn7.mp3";
            case AlarakBossSpawn8: return "/audio/alarak/boss spawn/bossspawn8.mp3";
            case AlarakBossSpawn9: return "/audio/alarak/boss spawn/bossspawn9.mp3";
            case AlarakBossSpawn10: return "/audio/alarak/boss spawn/bossspawn10.mp3";
            case AlarakClassSelect1: return "/audio/alarak/class selection/classselect1.mp3";
            case AlarakClassSelect2: return "/audio/alarak/class selection/classselect2.mp3";
            case AlarakClassSelect3: return "/audio/alarak/class selection/classselect3.mp3";
            case AlarakClassSelect4: return "/audio/alarak/class selection/classselect4.mp3";
            case AlarakClassSelect5: return "/audio/alarak/class selection/classselect5.mp3";
            case AlarakClassSelect6: return "/audio/alarak/class selection/classselect6.mp3";
            case AlarakClassSelect7: return "/audio/alarak/class selection/classselect7.mp3";
            case AlarakClassSelectCaptain: return "/audio/alarak/class selection/classselectcaptain.mp3";
            case AlarakClassSelectMutalisk: return "/audio/alarak/class selection/select mutalisk.mp3";
            case AlarakLevelFinished1: return "/audio/alarak/level finished/levelfinished1.mp3";
            case AlarakLevelFinished2: return "/audio/alarak/level finished/levelfinished2.mp3";
            case AlarakLevelFinished3: return "/audio/alarak/level finished/levelfinished3.mp3";
            case AlarakLevelFinished4: return "/audio/alarak/level finished/levelfinished4.mp3";
            case AlarakLevelFinished5: return "/audio/alarak/level finished/levelfinished5.mp3";
            case AlarakLevelFinished6: return "/audio/alarak/level finished/levelfinished6.mp3";
            case AlarakLevelFinished7: return "/audio/alarak/level finished/levelfinished7.mp3";
            case AlarakLevelFinished8: return "/audio/alarak/level finished/levelfinished8.mp3";
            case AlarakLevelFinished9: return "/audio/alarak/level finished/levelfinished9.mp3";
            case AlarakLevelFinished10: return "/audio/alarak/level finished/levelfinished10.mp3";
            case AlarakLevelFinished11: return "/audio/alarak/level finished/levelfinished11.mp3";
            case AlarakLevelFinishedZerg: return "/audio/alarak/level finished/levelfinishedzerg.mp3";
            case AlarakLevelStart1: return "/audio/alarak/level start/levelstart1.mp3";
            case AlarakLevelStart2: return "/audio/alarak/level start/levelstart2.mp3";
            case AlarakLevelStart3: return "/audio/alarak/level start/levelstart3.mp3";
            case AlarakLevelStart4: return "/audio/alarak/level start/levelstart4.mp3";
            case AlarakLevelStart5: return "/audio/alarak/level start/levelstart5.mp3";
            case AlarakLevelStart6: return "/audio/alarak/level start/levelstart6.mp3";
            case AlarakLevelStart7: return "/audio/alarak/level start/levelstart7.mp3";
            case AlarakLevelStart8: return "/audio/alarak/level start/levelstart8.mp3";
            case AlarakLevelStart9: return "/audio/alarak/level start/levelstart9.mp3";
            case AlarakLevelStart10: return "/audio/alarak/level start/levelstart10.mp3";
            case AlarakLevelStart11: return "/audio/alarak/level start/levelstart11.mp3";
            case AlarakLevelStart12: return "/audio/alarak/level start/levelstart12.mp3";
            case AlarakLevelStart13: return "/audio/alarak/level start/levelstart13.mp3";
            case AlarakLevelStart14: return "/audio/alarak/level start/levelstart14.mp3";
            case AlarakLevelStartZerg: return "/audio/alarak/level start/levelstartzerg.mp3";
            case AlarakPlayerDeath1: return "/audio/alarak/playerdeath/playerdeath1.mp3";
            case AlarakPlayerDeath2: return "/audio/alarak/playerdeath/playerdeath2.mp3";
            case AlarakPlayerDeath3: return "/audio/alarak/playerdeath/playerdeath3.mp3";
            case AlarakPlayerDeath4: return "/audio/alarak/playerdeath/playerdeath4.mp3";
            case AlarakPlayerDeath5: return "/audio/alarak/playerdeath/playerdeath5.mp3";
            case AlarakPlayerDeath6: return "/audio/alarak/playerdeath/playerdeath6.mp3";
            case AlarakPlayerDeath7: return "/audio/alarak/playerdeath/playerdeath7.mp3";
            case AlarakPlayerDeath8: return "/audio/alarak/playerdeath/playerdeath8.mp3";
            case AlarakPlayerDeath9: return "/audio/alarak/playerdeath/playerdeath9.mp3";
            case AlarakPlayerDeath10: return "/audio/alarak/playerdeath/playerdeath10.mp3";
            case AlarakPlayerDeath11: return "/audio/alarak/playerdeath/playerdeath11.mp3";
            case AlarakPlayerDeath12: return "/audio/alarak/playerdeath/playerdeath12.mp3";
            case AlarakPlayerDeath13: return "/audio/alarak/playerdeath/playerdeath13.mp3";
            case AlarakPlayerDeath14: return "/audio/alarak/playerdeath/playerdeath14.mp3";
            case AlarakPlayerDeath15: return "/audio/alarak/playerdeath/playerdeath15.mp3";
            case AlarakPlayerDeath16: return "/audio/alarak/playerdeath/playerdeath16.mp3";
            case AlarakPlayerDeath17: return "/audio/alarak/playerdeath/playerdeath17.mp3";
            case AlarakPlayerDeath18: return "/audio/alarak/playerdeath/playerdeath18.mp3";
            case AlarakPlayerDeath19: return "/audio/alarak/playerdeath/playerdeath19.mp3";
            case AlarakPlayerDeath20: return "/audio/alarak/playerdeath/playerdeath20.mp3";
            case AlarakPlayerDeath21: return "/audio/alarak/playerdeath/playerdeath21.mp3";
            case AlarakPlayerDeath22: return "/audio/alarak/playerdeath/playerdeath22.mp3";
            case AlarakPlayerDeath23: return "/audio/alarak/playerdeath/playerdeath23.mp3";
            case AlarakPlayerDeath24: return "/audio/alarak/playerdeath/playerdeath24.mp3";
            case AlarakPlayerDeath25: return "/audio/alarak/playerdeath/playerdeath25.mp3";
            case AlarakRelicSelect1: return "/audio/alarak/relic selection/relicselect1.mp3";
            case AlarakRelicSelect2: return "/audio/alarak/relic selection/relicselect2.mp3";
            case AlarakRelicSelect3: return "/audio/alarak/relic selection/relicselect3.mp3";
            case AlarakRelicSelect4: return "/audio/alarak/relic selection/relicselect4.mp3";
            case AlarakRelicSelect5: return "/audio/alarak/relic selection/relicselect5.mp3";
            case AlarakRelicSelect6: return "/audio/alarak/relic selection/relicselect6.mp3";
            case AlarakRelicSelect7: return "/audio/alarak/relic selection/relicselect7.mp3";
            case AlarakRelicSelect8: return "/audio/alarak/relic selection/relicselect8.mp3";
            case AlarakEnterShop1: return "/audio/alarak/shop/enter shop/entershop1.mp3";
            case AlarakEnterShop2: return "/audio/alarak/shop/enter shop/entershop2.mp3";
            case AlarakEnterShop3: return "/audio/alarak/shop/enter shop/entershop3.mp3";
            case AlarakEnterShop4: return "/audio/alarak/shop/enter shop/entershop4.mp3";
            case AlarakEnterShop5: return "/audio/alarak/shop/enter shop/entershop5.mp3";
            case AlarakEnterShop6: return "/audio/alarak/shop/enter shop/entershop6.mp3";
            case AlarakIdleInShop1: return "/audio/alarak/shop/idle in shop/idleinshop1.mp3";
            case AlarakIdleInShop2: return "/audio/alarak/shop/idle in shop/idleinshop2.mp3";
            case AlarakIdleInShop3: return "/audio/alarak/shop/idle in shop/idleinshop3.mp3";
            case AlarakIdleInShop4: return "/audio/alarak/shop/idle in shop/idleinshop4.mp3";
            case AlarakIdleInShop5: return "/audio/alarak/shop/idle in shop/idleinshop5.mp3";
            case AlarakIdleInShop6: return "/audio/alarak/shop/idle in shop/idleinshop6.mp3";
            case AlarakItemPurchased1: return "/audio/alarak/shop/item purchased/itempurchased1.mp3";
            case AlarakItemPurchased2: return "/audio/alarak/shop/item purchased/itempurchased2.mp3";
            case AlarakItemPurchased3: return "/audio/alarak/shop/item purchased/itempurchased3.mp3";
            case AlarakItemPurchased4: return "/audio/alarak/shop/item purchased/itempurchased4.mp3";
            case AlarakItemPurchased5: return "/audio/alarak/shop/item purchased/itempurchased5.mp3";
            case AlarakNotEnoughMinerals1: return "/audio/alarak/shop/not enough minerals/notenoughminerals1.mp3";
            case AlarakNotEnoughMinerals2: return "/audio/alarak/shop/not enough minerals/notenoughminerals2.mp3";
            case AlarakNotEnoughMinerals3: return "/audio/alarak/shop/not enough minerals/notenoughminerals3.mp3";
            case AlarakNotEnoughMinerals4: return "/audio/alarak/shop/not enough minerals/notenoughminerals4.mp3";
            case AlarakRandomMenu1: return "/audio/alarak/random menu/AlarakA_HeroSelectPlayer01.mp3";
            case AlarakRandomMenu2: return "/audio/alarak/random menu/AlarakA_OutpostLost01.mp3";
            case AlarakRandomMenu3: return "/audio/alarak/random menu/AlarakA_OutpostLost03.mp3";
            case AlarakRandomMenu4: return "/audio/alarak/random menu/AlarakA_PlayerAbandon02.mp3";
            case AlarakRandomMenu5: return "/audio/alarak/random menu/AlarakA_PlayerRejoin01.mp3";
            case AlarakRandomMenu6: return "/audio/alarak/random menu/AlarakA_PlayerRejoin02.mp3";
            case AlarakRandomMenu7: return "/audio/alarak/random menu/AlarakBase_IntroQuestion_Negative00.mp3";
            case AlarakRandomMenu8: return "/audio/alarak/random menu/AlarakBase_IntroRespond01.mp3";
            case AlarakRandomMenu9: return "/audio/alarak/random menu/AlarakBase_IntroResponse_Firebat00.mp3";
            case AlarakRandomMenu10: return "/audio/alarak/random menu/AlarakBase_IntroResponse_Samuro00.mp3";
            case AlarakRandomMenu11: return "/audio/alarak/random menu/AlarakBase_IntroResponse_Terran00.mp3";
            case AlarakRandomMenu12: return "/audio/alarak/random menu/AlarakBase_Ping_DefendStructure00.mp3";
            case AlarakRandomMenu13: return "/audio/alarak/random menu/AlarakBase_Ping_DefendThing00.mp3";
            case AlarakRandomMenu14: return "/audio/alarak/random menu/AlarakBase_Pissed09.mp3";
            case AlarakRandomMenuNeedsEdit1: return "/audio/alarak/random menu/needs edit - AlarakBase_IntroQuestion_Tracer00.mp3";
            case AlarakRandomMenuNeedsEdit2: return "/audio/alarak/random menu/needs edit -AlarakA_KeepLost02.mp3";
            case AlarakRandomMenuNeedsEdit3: return "/audio/alarak/random menu/needs edit-AlarakBase_Ping_DefendThing01.mp3";
            case AlarakIdk: return "/audio/alarak/idk.mp3";
            case AchievementUnlocked: return "/audio/achievement.wav";
            case CoinCollected: return "/audio/coin.wav";
            case GenericError: return "/audio/genericerror.wav";
            case DistressCall: return "/audio/distressbeacon.wav";
            case Player_Laserbeam:
                return "/audio/laserbeam1.wav";
            case GodRunDetected:
                return "/audio/godrundetected.wav";
            case StickyGrenadeExplosion:
                return "/audio/StickyGrenadeExplosion.wav";
            case ChargingBigIronLaserbeam:
                return "/audio/ChargingBigIronLaserbeam.wav";
            case NewPlayerLaserbeam:
                return  "/audio/NewPlayerLaserbeam.wav";
            case PlayerTakesDamage:
                return "/audio/PlayerTakesDamage.wav";
            case Destroyed_Explosion:
                return "/audio/Destroyed Explosion.wav";
            case Alien_Spaceship_Destroyed:
                return "/audio/Alien Spaceship Destroyed.wav";
            case Furi_Wisdowm_Of_Rage:
                return "/audio/music/Waveshaper wisdom of rage.wav";
            case Alien_Bomb_Impact:
                return "/audio/Alien Bomb Impact.wav";
            case Large_Ship_Destroyed:
                return "/audio/Large Ship Destroyed.wav";
            case Default_EMP:
                return "/audio/Default EMP.wav";
            case Alien_Bomb_Destroyed:
                // DUPLICATE
                return "/audio/Destroyed Explosion.wav";
            case GenericSelect:
                return "/audio/PowerUpAcquired.wav";
            case Rocket_Launcher:
                return "/audio/Rocket Launcher.wav";
            case Flamethrower:
                return "/audio/Flamethrower.wav";
            case Firewall:
                return "/audio/Firewall.wav";
            case mainmenu:
                return "/audio/music/mainmenu.wav";
            case Filth:
                return "/audio/music/Filth.wav";
            case Blood_On_The_Dancefloor:
                return "/audio/music/Blood_On_The_Dancefloor.wav";
            case FinalBossPhase1:
                return "/audio/music/phase1.wav";
            case FinalBossPhase2:
                return "/audio/music/phase2.wav";
            case FinalBossPhase3:
                return "/audio/music/phase3.wav";
            case MausoleumMash:
                return "/audio/music/Mausoleum mash.wav";
            case Arisen:
                return "/audio/music/Arisen.wav";
            case Enraged:
                return "/audio/music/enraged.wav";
            case Lemmino_Firecracker:
                return "/audio/music/lemmino Firecracker.wav";
            case NotEnoughMinerals:
                return "/audio/Not Enough Minerals.wav";
            case ChargingLaserbeam:
                return "/audio/Laserbeam Charging1.wav";
            case SpaceStationBlastingOff:
                return "/audio/SpaceStationBlastingOff.wav";
            case SpaceStationChargingUpMovement:
                return "/audio/SpaceStationChargingUpMovement.wav";
            case SpecialAttackFinishedCharging:
                return "/audio/ElectroShredFinishedCharging.wav";
            case ScarabExplosion:
                return "/audio/scarabexplosion.wav";
            case DeathMusic:return "/audio/music/deathmusic.mp3";
            case nomad:return "/audio/music/nomad.wav";
            case WaveshaperMonster: return "/audio/music/Waveshaper - Monster.wav";
            case RMChase: return "/audio/music/RM Chase.wav";
        }
        return null;
    }
}

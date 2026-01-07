package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AudioEnums {
    GenericError,
    Player_Laserbeam,
    SpecialAttackFinishedCharging,
    PlayerTakesDamage,
    NewPlayerLaserbeam,
    StickyGrenadeExplosion,
    SilentAudio,
    ChargingLaserbeam,
    SpaceStationChargingUpMovement,
    SpaceStationBlastingOff,
    Alien_Bomb_Destroyed,
    Alien_Spaceship_Destroyed,
    Alien_Bomb_Impact,
    Large_Ship_Destroyed,
    Default_EMP,
    Furi_Wisdowm_Of_Rage,
    Destroyed_Explosion,
    GenericSelect,
    Flamethrower,
    Rocket_Launcher,
    NONE,
    Firewall,
    mainmenu,
    Viq_Rose,
    Blood_On_The_Dancefloor,
    Lemmino_Firecracker,
    VendlaSonrisa,
    keygen,
    nomad,
    NotEnoughMinerals,
    BroodlingAttached,
    AchievementUnlocked,
    OverlordDeath,
    DevourerBirth,
    DevourerDeath,
    DevourerHit,
    GuardianBirth,
    GuardianDeath,
    MutaliskBirth,
    MutaliskDeath,
    QueenDeath,
    ScourgeCollision,
    ScourgeDeath,
    ScourgeNoticed,
    WaveshaperMonster,
    RiskOfDanger,
    BossBattle,
    ClassCarrierSpeedingUp,
    ClassCarrierSlowingDown,
    CaptainMisc0,
    CaptainMisc1,
    CaptainRdy0,
    CaptainWhat0,
    CaptainWhat1,
    CaptainWhat2,
    CaptainWhat3,
    CaptainYes0,
    CaptainYes1,
    CaptainYes2,
    CarrierRdy0,
    CarrierWhat0,
    CarrierWhat1,
    CarrierYes0,
    CarrierYes1,
    CarrierYes2,
    CarrierYes3,
    FireFighterMisc0,
    FireFighterMisc1,
    FireFighterYes0,
    FireFighterYes1,
    FireFighterYes2,
    FireFighterYes3,
    ProtossShipDeath,
    CoinCollected,
    ScarabExplosion,
    GodRunDetected,
    MausoleumMash,
    Arisen,
    DistressCall;

    public static AudioEnums getSelectClassAudioByClass(PlayerClass playerClass) {
        List<AudioEnums> availableSounds = new ArrayList<>();
        Random random = new Random();

        for (AudioEnums audioEnum : AudioEnums.values()) {
            String enumName = audioEnum.name();
            switch (playerClass) {
                case Carrier:
                    if (enumName.startsWith("Carrier")) {
                        availableSounds.add(audioEnum);
                    }
                    break;
                case Captain:
                    if (enumName.startsWith("Captain")) {
                        availableSounds.add(audioEnum);
                    }
                    break;
                case FireFighter:
                    if (enumName.startsWith("FireFighter")) {
                        availableSounds.add(audioEnum);
                    }
                    break;
            }
        }

        if (availableSounds.isEmpty()) {
            return null; // Or throw an exception if no sounds are found
        }

        int randomIndex = random.nextInt(availableSounds.size());
        return availableSounds.get(randomIndex);
    }

    public static AudioEnums getBossTheme(EnemyEnums enemyEnums) {
        switch (enemyEnums) {
            case SpaceStationBoss:
                return AudioEnums.Furi_Wisdowm_Of_Rage;
            case RedBoss:
                return AudioEnums.Blood_On_The_Dancefloor;
            case CarrierBoss:
                return AudioEnums.nomad;
            case YellowBoss:
                return AudioEnums.WaveshaperMonster;
            case StrikerBoss:
                return AudioEnums.Arisen;
            case BlueBoss:
                return AudioEnums.MausoleumMash;
            default:
                return getRandomBossSong();
        }
    }

    public static AudioEnums getRandomBossSong() {
        List<AudioEnums> bossSongList = new ArrayList<>();
        bossSongList.add(AudioEnums.Furi_Wisdowm_Of_Rage);
        bossSongList.add(AudioEnums.Blood_On_The_Dancefloor);
        bossSongList.add(AudioEnums.nomad);
        bossSongList.add(AudioEnums.WaveshaperMonster);

        return bossSongList.get(new Random().nextInt(bossSongList.size()));
    }
}

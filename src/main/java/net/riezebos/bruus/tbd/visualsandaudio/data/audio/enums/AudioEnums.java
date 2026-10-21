package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

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
    ChargingLaserbeam,
    ChargingBigIronLaserbeam,
    SpaceStationChargingUpMovement,
    SpaceStationBlastingOff,
    Alien_Bomb_Destroyed,
    Alien_Spaceship_Destroyed,
    Alien_Bomb_Impact,
    Large_Ship_Destroyed,
    Default_EMP,
    Destroyed_Explosion,
    GenericSelect,
    Flamethrower,
    Rocket_Launcher,
    Firewall,
    NotEnoughMinerals,
    BroodlingAttached, //unused but the idea behind this audio effect is still interesting/cool
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
    FinalBossTaunt1,
    FinalBossTaunt2,
    FinalBossTaunt3,
    FinalBossTaunt4WhenLowHp,
    FinalBossPhaseTransition1,
    FinalBossPhaseTransition2,
    FinalBossPhaseTransition3,
    FinalBossPhaseTransition4,
    FinalBossPhaseTransition5,
    FinalBossOh,
    FinalBossMock1,
    FinalBossMock2,
    FinalBossMock3,
    FinalBossMock4,
    FinalBossMock5,
    FinalBossMock6,
    FinalBossMock7,
    FinalBossMock8,
    FinalBossGreeting,
    FinalBossManyBulletsTaunt1,
    FinalBossManyBulletsTaunt2,
    FinalBossManyBulletsTaunt3,
    ProtossShipDeath,
    CoinCollected,
    ScarabExplosion,
    GodRunDetected,
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
}

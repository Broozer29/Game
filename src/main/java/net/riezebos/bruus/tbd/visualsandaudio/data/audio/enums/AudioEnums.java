package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AudioEnums {
    CustomMusicFile,
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
    Furi_Wisdowm_Of_Rage,
    RMChase,
    Destroyed_Explosion,
    GenericSelect,
    Flamethrower,
    Rocket_Launcher,
    Firewall,
    mainmenu,
    Blood_On_The_Dancefloor,
    Lemmino_Firecracker,
    DeathMusic,
    nomad,
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
    WaveshaperMonster,
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
    FinalBossFemaleGreeting,
    FinalBossFemaleIdk1,
    FinalBossFemaleIdk2,
    FinalBossFemaleMock1,
    FinalBossFemaleMock2,
    FinalBossFemaleMock3,
    FinalBossFemaleMock4,
    FinalBossFemaleMock5,
    FinalBossFemalePhaseTransition1,
    FinalBossFemalePhaseTransition2,
    FinalBossFemalePhaseTransition3,
    FinalBossFemalePhaseTransition4,
    FinalBossFemalePhaseTransition5,
    FinalBossFemaleTaunt1,
    FinalBossFemaleTaunt2,
    FinalBossFemaleTaunt3,
    FinalBossFemaleTaunt4,
    FinalBossFemaleTaunt5,
    FinalBossFemaleTaunt6,
    FinalBossFemaleTaunt7,
    AlarakBossDeath1,
    AlarakBossDeath2,
    AlarakBossDeath3,
    AlarakBossDeath4,
    AlarakBossDeath5,
    AlarakBossDeath6,
    AlarakBossDeath7,
    AlarakBossDeath8,
    AlarakBossDeath9,
    AlarakBossDeath10,
    AlarakBossDeath11,
    AlarakBossDeath12,
    AlarakBossDeath13,
    AlarakBossDeath14,
    AlarakBossDeath15,
    AlarakBossSpawn1,
    AlarakBossSpawn2,
    AlarakBossSpawn3,
    AlarakBossSpawn4,
    AlarakBossSpawn5,
    AlarakBossSpawn6,
    AlarakBossSpawn7,
    AlarakBossSpawn8,
    AlarakBossSpawn9,
    AlarakBossSpawn10,
    AlarakClassSelect1,
    AlarakClassSelect2,
    AlarakClassSelect3,
    AlarakClassSelect4,
    AlarakClassSelect5,
    AlarakClassSelect6,
    AlarakClassSelect7,
    AlarakClassSelectCaptain,
    AlarakClassSelectMutalisk,
    AlarakLevelFinished1,
    AlarakLevelFinished2,
    AlarakLevelFinished3,
    AlarakLevelFinished4,
    AlarakLevelFinished5,
    AlarakLevelFinished6,
    AlarakLevelFinished7,
    AlarakLevelFinished8,
    AlarakLevelFinished9,
    AlarakLevelFinished10,
    AlarakLevelFinished11,
    AlarakLevelFinishedZerg,
    AlarakLevelStart1,
    AlarakLevelStart2,
    AlarakLevelStart3,
    AlarakLevelStart4,
    AlarakLevelStart5,
    AlarakLevelStart6,
    AlarakLevelStart7,
    AlarakLevelStart8,
    AlarakLevelStart9,
    AlarakLevelStart10,
    AlarakLevelStart11,
    AlarakLevelStart12,
    AlarakLevelStart13,
    AlarakLevelStart14,
    AlarakLevelStartZerg,
    AlarakPlayerDeath1,
    AlarakPlayerDeath2,
    AlarakPlayerDeath3,
    AlarakPlayerDeath4,
    AlarakPlayerDeath5,
    AlarakPlayerDeath6,
    AlarakPlayerDeath7,
    AlarakPlayerDeath8,
    AlarakPlayerDeath9,
    AlarakPlayerDeath10,
    AlarakPlayerDeath11,
    AlarakPlayerDeath12,
    AlarakPlayerDeath13,
    AlarakPlayerDeath14,
    AlarakPlayerDeath15,
    AlarakPlayerDeath16,
    AlarakPlayerDeath17,
    AlarakPlayerDeath18,
    AlarakPlayerDeath19,
    AlarakPlayerDeath20,
    AlarakPlayerDeath21,
    AlarakPlayerDeath22,
    AlarakPlayerDeath23,
    AlarakPlayerDeath24,
    AlarakPlayerDeath25,
    AlarakRelicSelect1,
    AlarakRelicSelect2,
    AlarakRelicSelect3,
    AlarakRelicSelect4,
    AlarakRelicSelect5,
    AlarakRelicSelect6,
    AlarakRelicSelect7,
    AlarakRelicSelect8,
    AlarakEnterShop1,
    AlarakEnterShop2,
    AlarakEnterShop3,
    AlarakEnterShop4,
    AlarakEnterShop5,
    AlarakEnterShop6,
    AlarakIdleInShop1,
    AlarakIdleInShop2,
    AlarakIdleInShop3,
    AlarakIdleInShop4,
    AlarakIdleInShop5,
    AlarakIdleInShop6,
    AlarakItemPurchased1,
    AlarakItemPurchased2,
    AlarakItemPurchased3,
    AlarakItemPurchased4,
    AlarakItemPurchased5,
    AlarakNotEnoughMinerals1,
    AlarakNotEnoughMinerals2,
    AlarakNotEnoughMinerals3,
    AlarakNotEnoughMinerals4,
    AlarakRandomMenu1,
    AlarakRandomMenu2,
    AlarakRandomMenu3,
    AlarakRandomMenu4,
    AlarakRandomMenu5,
    AlarakRandomMenu6,
    AlarakRandomMenu7,
    AlarakRandomMenu8,
    AlarakRandomMenu9,
    AlarakRandomMenu10,
    AlarakRandomMenu11,
    AlarakRandomMenu12,
    AlarakRandomMenu13,
    AlarakRandomMenu14,
    AlarakRandomMenuNeedsEdit1,
    AlarakRandomMenuNeedsEdit2,
    AlarakRandomMenuNeedsEdit3,
    AlarakIdk,
    ProtossShipDeath,
    CoinCollected,
    ScarabExplosion,
    GodRunDetected,
    MausoleumMash,
    Arisen,
    Enraged,
    DistressCall,
    Filth,
    FinalBossPhase1,
    FinalBossPhase2,
    FinalBossPhase3;

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
                return AudioEnums.RMChase;
            case CarrierBoss:
                return AudioEnums.nomad;
            case YellowBoss:
                return AudioEnums.Filth;
            case StrikerBoss:
                return AudioEnums.Arisen;
            case BlueBoss:
                return AudioEnums.MausoleumMash;
            case TwinBoss:
                return AudioEnums.Enraged;
            case FinalBoss:
                return AudioEnums.FinalBossPhase1;
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

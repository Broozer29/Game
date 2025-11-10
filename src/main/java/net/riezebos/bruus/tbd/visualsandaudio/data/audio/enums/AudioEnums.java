package net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum AudioEnums {
    GenericError(false),
    Player_Laserbeam(false),
    SpecialAttackFinishedCharging(false),
    PlayerTakesDamage(false),
    NewPlayerLaserbeam(false),
    StickyGrenadeExplosion(false),
    SilentAudio(false),
    ChargingLaserbeam(false),
    SpaceStationChargingUpMovement(false),
    SpaceStationBlastingOff(false),
    Alien_Bomb_Destroyed(false),
    Alien_Spaceship_Destroyed(false),
    Alien_Bomb_Impact(false),
    Large_Ship_Destroyed(false),
    Default_EMP(false),
    Furi_Wisdowm_Of_Rage(false),
    Destroyed_Explosion(false),
    GenericSelect(false),
    Flamethrower(false),
    Rocket_Launcher(false),
    NONE(false),
    Firewall(false),
    mainmenu(false),
    Viq_Rose(true),
    Blood_On_The_Dancefloor(false),
    Lemmino_Firecracker(false),
    VendlaSonrisa(false),
    keygen(false),
    nomad(false),
    NotEnoughMinerals(false),
    BroodlingAttached(false),
    AchievementUnlocked(false),
    OverlordDeath(false),
    DevourerBirth(false),
    DevourerDeath(false),
    DevourerHit(false),
    GuardianBirth(false),
    GuardianDeath(false),
    MutaliskBirth(false),
    MutaliskDeath(false),
    QueenDeath(false),
    ScourgeCollision(false),
    ScourgeDeath(false),
    ScourgeNoticed(false),
    WaveshaperMonster(false),
    RiskOfDanger(false),
    BossBattle(false),
    ClassCarrierSpeedingUp(false),
    ClassCarrierSlowingDown(false),
    CaptainMisc0(false),
    CaptainMisc1(false),
    CaptainRdy0(false),
    CaptainWhat0(false),
    CaptainWhat1(false),
    CaptainWhat2(false),
    CaptainWhat3(false),
    CaptainYes0(false),
    CaptainYes1(false),
    CaptainYes2(false),
    CarrierRdy0(false),
    CarrierWhat0(false),
    CarrierWhat1(false),
    CarrierYes0(false),
    CarrierYes1(false),
    CarrierYes2(false),
    CarrierYes3(false),
    FireFighterMisc0(false),
    FireFighterMisc1(false),
    FireFighterYes0(false),
    FireFighterYes1(false),
    FireFighterYes2(false),
    FireFighterYes3(false),
    ProtossShipDeath(false),
    CoinCollected(false),
    ScarabExplosion(false), GodRunDetected(false);

    // Boolean attribute to indicate if this should be streamed
    private final boolean shouldStream;

    // Constructor to set the streaming flag
    AudioEnums(boolean shouldStream) {
        this.shouldStream = shouldStream;
    }

    // Method to check if the audio should be streamed
    public boolean shouldBeStreamed() {
        return shouldStream;
    }

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

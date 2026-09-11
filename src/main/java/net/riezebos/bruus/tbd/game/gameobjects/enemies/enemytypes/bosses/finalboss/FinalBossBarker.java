package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.*;

public class FinalBossBarker {

    private Set<AudioEnums> allBarkEnums;

    private List<AudioEnums> greetingBarks = new ArrayList<>();
    private List<AudioEnums> mockingBarks = new ArrayList<>();
    private List<AudioEnums> tauntingBarks = new ArrayList<>();
    private List<AudioEnums> phaseTransitionBarks = new ArrayList<>();
    private List<AudioEnums> manyBulletBarks = new ArrayList<>();
    private List<AudioEnums> lowHealthBarks = new ArrayList<>();

    private static int BULLETS_FOR_MANY_BULLET_BARKS = 40;
    private Random random = new Random();

    public FinalBossBarker() {
        loadBarkLists();
    }

    private void loadBarkLists() {
        greetingBarks.add(AudioEnums.FinalBossGreeting);

        mockingBarks.add(AudioEnums.FinalBossMock1);
        mockingBarks.add(AudioEnums.FinalBossMock2);
        mockingBarks.add(AudioEnums.FinalBossMock3);
        mockingBarks.add(AudioEnums.FinalBossMock4);
        mockingBarks.add(AudioEnums.FinalBossMock5);
        mockingBarks.add(AudioEnums.FinalBossMock6);
        mockingBarks.add(AudioEnums.FinalBossMock7);
        mockingBarks.add(AudioEnums.FinalBossMock8);

        tauntingBarks.add(AudioEnums.FinalBossTaunt1);
        tauntingBarks.add(AudioEnums.FinalBossTaunt2);
        tauntingBarks.add(AudioEnums.FinalBossTaunt3);

        phaseTransitionBarks.add(AudioEnums.FinalBossPhaseTransition1);
        phaseTransitionBarks.add(AudioEnums.FinalBossPhaseTransition2);
        phaseTransitionBarks.add(AudioEnums.FinalBossPhaseTransition3);
        phaseTransitionBarks.add(AudioEnums.FinalBossPhaseTransition4);
        phaseTransitionBarks.add(AudioEnums.FinalBossPhaseTransition5);

        manyBulletBarks.add(AudioEnums.FinalBossManyBulletsTaunt1);
        manyBulletBarks.add(AudioEnums.FinalBossManyBulletsTaunt2);
        manyBulletBarks.add(AudioEnums.FinalBossManyBulletsTaunt3);


        lowHealthBarks.add(AudioEnums.FinalBossOh);
        lowHealthBarks.add(AudioEnums.FinalBossTaunt4WhenLowHp);

        allBarkEnums = new HashSet<>();
        allBarkEnums.addAll(greetingBarks);
        allBarkEnums.addAll(mockingBarks);
        allBarkEnums.addAll(tauntingBarks);
        allBarkEnums.addAll(phaseTransitionBarks);
        allBarkEnums.addAll(manyBulletBarks);
        allBarkEnums.addAll(lowHealthBarks);
    }

    public AudioEnums getRandomGreetingBark() {
        return greetingBarks.get(random.nextInt(greetingBarks.size()));
    }

    public AudioEnums getRandomTauntBark() {
        int amountOfBulletsThatAreReflected = Math.toIntExact(MissileManager.getInstance().getMissiles().stream().filter(missile -> missile.getTimesReflected() >= 1).count());

        if (amountOfBulletsThatAreReflected > BULLETS_FOR_MANY_BULLET_BARKS) {
            return manyBulletBarks.get(random.nextInt(manyBulletBarks.size()));
        }

        return tauntingBarks.get(random.nextInt(tauntingBarks.size()));
    }

    public AudioEnums getRandomMockBark() {
        return mockingBarks.get(random.nextInt(mockingBarks.size()));
    }

    public AudioEnums getRandomPhaseTransitionBark() {
        return phaseTransitionBarks.get(random.nextInt(phaseTransitionBarks.size()));
    }

    public AudioEnums getLowHealthBark() {
        return lowHealthBarks.get(random.nextInt(lowHealthBarks.size()));
    }

    public Set<AudioEnums> getAllBarkEnums() {
        return allBarkEnums;
    }
}

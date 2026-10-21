package net.riezebos.bruus.tbd.visualsandaudio.data.audio;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;

import java.util.*;

public class AlarakBarker {

    private Set<AudioEnums> allBarkEnums;

    private List<AudioEnums> bossSpawnBarks = new ArrayList<>();
    private List<AudioEnums> bossDeathBarks = new ArrayList<>();
    private List<AudioEnums> classSelectBarks = new ArrayList<>();
    private List<AudioEnums> captainClassSelectBarks = new ArrayList<>();
    private List<AudioEnums> mutaliskClassSelectBarks = new ArrayList<>();
    private List<AudioEnums> levelStartBarks = new ArrayList<>();
    private List<AudioEnums> zergLevelStartBarks = new ArrayList<>();
    private List<AudioEnums> levelFinishedBarks = new ArrayList<>();
    private List<AudioEnums> zergLevelFinishedBarks = new ArrayList<>();
    private List<AudioEnums> playerDeathBarks = new ArrayList<>();
    private List<AudioEnums> relicSelectBarks = new ArrayList<>();
    private List<AudioEnums> enterShopBarks = new ArrayList<>();
    private List<AudioEnums> idleInShopBarks = new ArrayList<>();
    private List<AudioEnums> itemPurchasedBarks = new ArrayList<>();
    private List<AudioEnums> notEnoughMineralsBarks = new ArrayList<>();
    private List<AudioEnums> randomMenuBarks = new ArrayList<>();

    private Random random = new Random();

    public AlarakBarker() {
        loadBarkLists();
    }

    private void loadBarkLists() {
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn1);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn2);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn3);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn4);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn5);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn6);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn7);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn8);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn9);
        bossSpawnBarks.add(AudioEnums.AlarakBossSpawn10);

        bossDeathBarks.add(AudioEnums.AlarakBossDeath1);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath2);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath3);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath4);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath5);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath6);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath7);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath8);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath9);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath10);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath11);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath12);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath13);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath14);
        bossDeathBarks.add(AudioEnums.AlarakBossDeath15);

        classSelectBarks.add(AudioEnums.AlarakClassSelect1);
        classSelectBarks.add(AudioEnums.AlarakClassSelect2);
        classSelectBarks.add(AudioEnums.AlarakClassSelect3);
        classSelectBarks.add(AudioEnums.AlarakClassSelect4);
        classSelectBarks.add(AudioEnums.AlarakClassSelect5);
        classSelectBarks.add(AudioEnums.AlarakClassSelect6);
        classSelectBarks.add(AudioEnums.AlarakClassSelect7);

        captainClassSelectBarks.add(AudioEnums.AlarakClassSelectCaptain);

        mutaliskClassSelectBarks.add(AudioEnums.AlarakClassSelectMutalisk);

        levelStartBarks.add(AudioEnums.AlarakLevelStart1);
        levelStartBarks.add(AudioEnums.AlarakLevelStart2);
        levelStartBarks.add(AudioEnums.AlarakLevelStart3);
        levelStartBarks.add(AudioEnums.AlarakLevelStart4);
        levelStartBarks.add(AudioEnums.AlarakLevelStart5);
        levelStartBarks.add(AudioEnums.AlarakLevelStart6);
        levelStartBarks.add(AudioEnums.AlarakLevelStart7);
        levelStartBarks.add(AudioEnums.AlarakLevelStart8);
        levelStartBarks.add(AudioEnums.AlarakLevelStart9);
        levelStartBarks.add(AudioEnums.AlarakLevelStart10);
        levelStartBarks.add(AudioEnums.AlarakLevelStart11);
        levelStartBarks.add(AudioEnums.AlarakLevelStart12);
        levelStartBarks.add(AudioEnums.AlarakLevelStart13);
        levelStartBarks.add(AudioEnums.AlarakLevelStart14);

        zergLevelStartBarks.add(AudioEnums.AlarakLevelStartZerg);

        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished1);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished2);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished3);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished4);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished5);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished6);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished7);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished8);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished9);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished10);
        levelFinishedBarks.add(AudioEnums.AlarakLevelFinished11);

        zergLevelFinishedBarks.add(AudioEnums.AlarakLevelFinishedZerg);

        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath1);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath2);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath3);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath4);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath5);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath6);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath7);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath8);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath9);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath10);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath11);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath12);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath13);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath14);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath15);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath16);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath17);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath18);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath19);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath20);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath21);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath22);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath23);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath24);
        playerDeathBarks.add(AudioEnums.AlarakPlayerDeath25);

        relicSelectBarks.add(AudioEnums.AlarakRelicSelect1);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect2);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect3);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect4);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect5);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect6);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect7);
        relicSelectBarks.add(AudioEnums.AlarakRelicSelect8);

        enterShopBarks.add(AudioEnums.AlarakEnterShop1);
        enterShopBarks.add(AudioEnums.AlarakEnterShop2);
        enterShopBarks.add(AudioEnums.AlarakEnterShop3);
        enterShopBarks.add(AudioEnums.AlarakEnterShop4);
        enterShopBarks.add(AudioEnums.AlarakEnterShop5);
        enterShopBarks.add(AudioEnums.AlarakEnterShop6);

        idleInShopBarks.add(AudioEnums.AlarakIdleInShop1);
        idleInShopBarks.add(AudioEnums.AlarakIdleInShop2);
        idleInShopBarks.add(AudioEnums.AlarakIdleInShop3);
        idleInShopBarks.add(AudioEnums.AlarakIdleInShop4);
        idleInShopBarks.add(AudioEnums.AlarakIdleInShop5);
        idleInShopBarks.add(AudioEnums.AlarakIdleInShop6);

        itemPurchasedBarks.add(AudioEnums.AlarakItemPurchased1);
        itemPurchasedBarks.add(AudioEnums.AlarakItemPurchased2);
        itemPurchasedBarks.add(AudioEnums.AlarakItemPurchased3);
        itemPurchasedBarks.add(AudioEnums.AlarakItemPurchased4);
        itemPurchasedBarks.add(AudioEnums.AlarakItemPurchased5);

        notEnoughMineralsBarks.add(AudioEnums.AlarakNotEnoughMinerals1);
        notEnoughMineralsBarks.add(AudioEnums.AlarakNotEnoughMinerals2);
        notEnoughMineralsBarks.add(AudioEnums.AlarakNotEnoughMinerals3);
        notEnoughMineralsBarks.add(AudioEnums.AlarakNotEnoughMinerals4);

        randomMenuBarks.add(AudioEnums.AlarakRandomMenu1);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu2);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu3);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu4);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu5);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu6);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu7);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu8);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu9);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu10);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu11);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu12);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu13);
        randomMenuBarks.add(AudioEnums.AlarakRandomMenu14);
        //These still need editing before they can be used
//        randomMenuBarks.add(AudioEnums.AlarakRandomMenuNeedsEdit1);
//        randomMenuBarks.add(AudioEnums.AlarakRandomMenuNeedsEdit2);
//        randomMenuBarks.add(AudioEnums.AlarakRandomMenuNeedsEdit3);

        allBarkEnums = new HashSet<>();
        allBarkEnums.addAll(bossSpawnBarks);
        allBarkEnums.addAll(bossDeathBarks);
        allBarkEnums.addAll(classSelectBarks);
        allBarkEnums.addAll(captainClassSelectBarks);
        allBarkEnums.addAll(mutaliskClassSelectBarks);
        allBarkEnums.addAll(levelStartBarks);
        allBarkEnums.addAll(zergLevelStartBarks);
        allBarkEnums.addAll(levelFinishedBarks);
        allBarkEnums.addAll(zergLevelFinishedBarks);
        allBarkEnums.addAll(playerDeathBarks);
        allBarkEnums.addAll(relicSelectBarks);
        allBarkEnums.addAll(enterShopBarks);
        allBarkEnums.addAll(idleInShopBarks);
        allBarkEnums.addAll(itemPurchasedBarks);
        allBarkEnums.addAll(notEnoughMineralsBarks);
        allBarkEnums.addAll(randomMenuBarks);
    }

    private AudioEnums getRandomBark(List<AudioEnums> barks) {
        if (barks.isEmpty()) {
            return null;
        }
        return barks.get(random.nextInt(barks.size()));
    }

    public AudioEnums getRandomBossSpawnBark() {
        return getRandomBark(bossSpawnBarks);
    }

    public AudioEnums getRandomBossDeathBark() {
        return getRandomBark(bossDeathBarks);
    }

    //Picks from the generic class select barks plus the ones that belong to the chosen class
    public AudioEnums getRandomClassSelectBark(PlayerClass playerClass) {
        List<AudioEnums> barks = new ArrayList<>(classSelectBarks);
        if (playerClass == PlayerClass.Captain) {
            barks.addAll(captainClassSelectBarks);
        } else if (playerClass == PlayerClass.Mutalisk) {
            barks.addAll(mutaliskClassSelectBarks);
        }
        return getRandomBark(barks);
    }

    public AudioEnums getRandomLevelStartBark(boolean zergLevel) {
        List<AudioEnums> barks = new ArrayList<>(levelStartBarks);
        if (zergLevel) {
            barks.addAll(zergLevelStartBarks);
        }
        return getRandomBark(barks);
    }

    public AudioEnums getRandomLevelFinishedBark(boolean zergLevel) {
        List<AudioEnums> barks = new ArrayList<>(levelFinishedBarks);
        if (zergLevel) {
            barks.addAll(zergLevelFinishedBarks);
        }
        return getRandomBark(barks);
    }


    private double lastSecondsBarked = 0;
    private static final double menuBarkCooldown = 10000;
    private static final double shopBarkCooldown = 30000;

    public void playPlayerDeathBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomPlayerDeathBark());
        }
    }


    public void playRelicSelectBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomRelicSelectBark());
        }
    }

    public void playEnterShopBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomEnterShopBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playIdleInShopBark() {
        if (System.currentTimeMillis() - lastSecondsBarked > shopBarkCooldown && !isBarking()) {
            AudioManager.getInstance().addAudio(getRandomIdleInShopBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playItemPurchasedBark() {
        if (System.currentTimeMillis() - lastSecondsBarked > 5000 && !isBarking()) {
            AudioManager.getInstance().addAudio(getRandomItemPurchasedBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playNotEnoughMineralsBark() {
        if (System.currentTimeMillis() - lastSecondsBarked > 2000 && !isBarking()) {
            AudioManager.getInstance().addAudio(getRandomNotEnoughMineralsBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playMenuBark() {
        if (System.currentTimeMillis() - lastSecondsBarked > menuBarkCooldown && !isBarking()) {
            AudioManager.getInstance().addAudio(getRandomMenuBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playLevelStartBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomLevelStartBark(LevelManager.getInstance().getCurrentEnemyTribe().equals(EnemyTribes.Zerg)));
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playLevelFinishedBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomLevelFinishedBark(LevelManager.getInstance().getCurrentEnemyTribe().equals(EnemyTribes.Zerg)));
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playBossSpawnBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomBossSpawnBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }

    public void playBossRandomDeathBark() {
        if (!isBarking()) {
            AudioManager.getInstance().addAudio(getRandomBossDeathBark());
            lastSecondsBarked = System.currentTimeMillis();
        }
    }


    public AudioEnums getRandomPlayerDeathBark() {
        return getRandomBark(playerDeathBarks);
    }

    public AudioEnums getRandomRelicSelectBark() {
        return getRandomBark(relicSelectBarks);
    }

    public AudioEnums getRandomEnterShopBark() {
        return getRandomBark(enterShopBarks);
    }

    public AudioEnums getRandomIdleInShopBark() {
        return getRandomBark(idleInShopBarks);
    }

    public AudioEnums getRandomItemPurchasedBark() {
        return getRandomBark(itemPurchasedBarks);
    }

    public AudioEnums getRandomNotEnoughMineralsBark() {
        return getRandomBark(notEnoughMineralsBarks);
    }

    public AudioEnums getRandomMenuBark() {
        return getRandomBark(randomMenuBarks);
    }

    public Set<AudioEnums> getAllBarkEnums() {
        return allBarkEnums;
    }

    public boolean isBarking() {
        if (!DevTestSettings.enableAlarakBarker) {
            return true;
        }


        return AudioDatabase.getInstance().getAllActiveClips().stream()
                .anyMatch(clip -> this.getAllBarkEnums().contains(clip.getAudioType()));
    }

}

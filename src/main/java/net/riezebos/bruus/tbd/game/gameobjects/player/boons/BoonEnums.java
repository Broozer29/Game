package net.riezebos.bruus.tbd.game.gameobjects.player.boons;

import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public enum BoonEnums {
    NEPOTISM(ImageEnums.Test_Image),
    CLUB_ACCESS(ImageEnums.ClubAcessUnlock),
    COMPOUND_WEALTH(ImageEnums.CompoundWealthUnlock),
    BOUNTY_HUNTER(ImageEnums.BountyHunterUnlock),
    TREASURE_HUNTER(ImageEnums.TreasureHunterUnlock);

    private ImageEnums unlockImage;

    BoonEnums(ImageEnums unlockImage) {
        this.unlockImage = unlockImage;
    }

    public ImageEnums getUnlockImage() {
        return unlockImage;
    }
}

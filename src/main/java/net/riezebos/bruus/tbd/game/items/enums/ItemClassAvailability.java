package net.riezebos.bruus.tbd.game.items.enums;

import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerClass;

import java.util.List;

public enum ItemClassAvailability {
    All(List.of(PlayerClass.Captain, PlayerClass.FireFighter, PlayerClass.Carrier)),
    Captain(List.of(PlayerClass.Captain)),
    FireFighter(List.of(PlayerClass.FireFighter)),
    Carrier(List.of(PlayerClass.Carrier)),
    CaptainFireFighter(List.of(PlayerClass.Captain, PlayerClass.FireFighter)),
    CaptainCarrier(List.of(PlayerClass.Captain, PlayerClass.Carrier)),
    Mutalisk(List.of(PlayerClass.Mutalisk));

    private final List<PlayerClass> playerClasses;

    ItemClassAvailability(List<PlayerClass> playerClasses) {
        this.playerClasses = playerClasses;
    }

    public List<PlayerClass> getPlayerClasses() {
        return playerClasses;
    }
}

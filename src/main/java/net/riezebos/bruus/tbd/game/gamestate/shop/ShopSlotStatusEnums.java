package net.riezebos.bruus.tbd.game.gamestate.shop;

public enum ShopSlotStatusEnums {
    Available,
    Bought, //Emptied by a purchase; stays empty in the next shop when the shop is frozen
    Locked //In a row that is not unlocked yet
}

package net.riezebos.bruus.tbd.game.items.enums;

public enum ItemApplicationEnum {
    AfterCollision, //When a GameObject collides with a GameObject and has to apply an effect
    BeforeCollision, //When a GameObject collides with a GameObject and something has to be modified before it takes effect
    ApplyOnCreation, //For effects that need to be applied on object creation
    UponAcquiring, //Directly after acquiring the item
    CustomActivation; //The item itself does not have a linked event, it's activated based on conditions in another class
}

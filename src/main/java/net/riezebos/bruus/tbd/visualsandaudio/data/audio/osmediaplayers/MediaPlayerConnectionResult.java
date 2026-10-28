package net.riezebos.bruus.tbd.visualsandaudio.data.audio.osmediaplayers;

//Outcome of a "test connection": whether the music app answered and can be used, and a message to show the player
//todo een record als dit vind ik slordige implementatie, waarom een aparte class aanmaken hiervoor?
public record MediaPlayerConnectionResult(boolean connected, String message) {
}

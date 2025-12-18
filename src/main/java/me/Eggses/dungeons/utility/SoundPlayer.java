package me.Eggses.dungeons.utility;

import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SoundPlayer {

    // accepts a String like "entity.evoker.prepare_wololo"
    public Sound createSound(String soundToPlay) {
        return createSound(soundToPlay, 1.0f, 1.0f);
    }

    public Sound createSound(String soundToPlay, float volume, float pitch) {
        org.bukkit.Sound soundValue = getSound(soundToPlay);

        return Sound.sound(soundValue, Sound.Source.PLAYER, volume, pitch);
    }

    private org.bukkit.Sound getSound(String soundToPlay) {
        try {
            return Registry.SOUNDS.getOrThrow(NamespacedKey.minecraft(soundToPlay));
        } catch (Exception e) {
            return org.bukkit.Sound.ENTITY_GOAT_HORN_BREAK;
        }
    }

    public void playSound(Sound sound, Collection<? extends Player> playersToPlaySoundToo) {
        playersToPlaySoundToo.forEach(player -> player.playSound(sound, Sound.Emitter.self()));
    }
}
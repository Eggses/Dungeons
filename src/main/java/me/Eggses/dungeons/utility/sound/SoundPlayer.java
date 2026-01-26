package me.Eggses.dungeons.utility.sound;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SoundPlayer {

    private static final String DEFAULT_SOUND = "minecraft:item.goat_horn.break";

    @SuppressWarnings("All") // soundToPlay always gets a warning even though it's fine!
    public Sound createSound(String soundToPlay, float volume, float pitch) {

        Key key = Key.parseable(soundToPlay) ? Key.key(soundToPlay) : Key.key(DEFAULT_SOUND);
        Sound sound = Sound.sound(key, Sound.Source.PLAYER, volume, pitch);

        return sound;
    }

    public Sound createSound(String soundToPlay) {
        return createSound(soundToPlay, 1.0f, 1.0f);
    }

    public void playSound(Sound sound, Collection<? extends Player> players) {
        players.forEach(player -> player.playSound(sound, Sound.Emitter.self()));
    }

    public void playSound(Sound sound, Player player) {
       player.playSound(sound, Sound.Emitter.self());
    }
}
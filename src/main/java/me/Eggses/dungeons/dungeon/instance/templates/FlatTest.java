package me.Eggses.dungeons.dungeon.instance.templates;

import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class FlatTest extends DungeonTemplate {

    private static final String CONFIG_FILE_NAME = "flat_test.yml";

    public FlatTest(JavaPlugin plugin, MessageCreator messageCreator, SoundPlayer soundPlayer) {
        super(plugin, CONFIG_FILE_NAME, messageCreator, soundPlayer);
    }
}
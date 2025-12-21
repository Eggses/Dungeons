package me.Eggses.dungeons.dungeon.instance.templates;

import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class MalignantMarsh extends DungeonTemplate {

    private static final String CONFIG_FILE_NAME = "malignant_marsh.yml";

    public MalignantMarsh(JavaPlugin plugin, MessageCreator messageCreator, SoundPlayer soundPlayer) {
        super(plugin, CONFIG_FILE_NAME, messageCreator, soundPlayer);
    }
}
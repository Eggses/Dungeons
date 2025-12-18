package me.Eggses.dungeons.dungeon.instance.configurations;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.files.DungeonFileReader;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class FlatTest implements DungeonTemplate {

    private static final String CONFIG_FILE_NAME = "flat_test.yml";
    private final DungeonFileReader dungeonFileReader;

    public FlatTest(JavaPlugin plugin, MessageCreator messageCreator, SoundPlayer soundPlayer) {
        this.dungeonFileReader = new DungeonFileReader(plugin, CONFIG_FILE_NAME, messageCreator, soundPlayer);
    }

    @Override
    public String getTemplateFolderName() {
        return dungeonFileReader.readTemplateFileName();
    }

    @Override
    public DungeonPortal getDungeonPortal() {
        return dungeonFileReader.readDungeonPortal();
    }

    @Override
    public AreaControllerBuilder getAreaControllerBuilder() {
        return dungeonFileReader.readDungeonAreas();
    }
}
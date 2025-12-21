package me.Eggses.dungeons.dungeon.instance.templates;

import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.files.DungeonFileReader;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public abstract class DungeonTemplate {

    private static final Consumer<World> EMPTY_CONSUMER = (world) -> {};
    private final DungeonFileReader dungeonFileReader;

    protected DungeonTemplate(JavaPlugin plugin,
                              String configFileName,
                              MessageCreator messageCreator,
                              SoundPlayer soundPlayer) {

        this.dungeonFileReader = new DungeonFileReader(plugin, configFileName, messageCreator, soundPlayer);
    }

    public String getTemplateFolderName() {
        return dungeonFileReader.readTemplateFileName();
    }

    // TOODO FIx this to read a file.
    public Position getDefaultGraveyardPosition() {
        return new Position(1, 2, 3);
    }

    public DungeonPortal getDungeonPortal() {
        return dungeonFileReader.readDungeonPortal();
    }

    public AreaControllerBuilder getAreaControllerBuilder() {
        return dungeonFileReader.readDungeonAreas();
    }

    public Consumer<World> getDungeonRules() {
        return EMPTY_CONSUMER;
    }
}
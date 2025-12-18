package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.areas.utility.AreaControllerBuilder;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class DungeonFileReader {

    private final DungeonHeadingReader dungeonHeadingReader;
    private final DungeonAreaReader dungeonAreaReader;

    public DungeonFileReader(JavaPlugin plugin,
                             String fileName,
                             MessageCreator messageCreator,
                             SoundPlayer soundPlayer) {

        var configurationFile = new ConfigurationFile(plugin, fileName);
        this.dungeonHeadingReader = new DungeonHeadingReader(configurationFile, this, messageCreator, soundPlayer);
        this.dungeonAreaReader = new DungeonAreaReader(plugin, configurationFile, this);
    }

    public AreaControllerBuilder readDungeonAreas() {
        return dungeonAreaReader.readDungeonAreas();
    }

    public String readTemplateFolderName() {
        return dungeonHeadingReader.readTemplateFileName();
    }

    public DungeonPortal readDungeonPortal() {
        return dungeonHeadingReader.readDungeonPortal();
    }

    public Map<String, String> createValueMap(String command) {

        String[] arguments = command.split("\\s+");

        Map<String, String> valuesMap = new HashMap<>();

        for (String argument : arguments) {

            int indexOfEquals = argument.indexOf('=');
            if (indexOfEquals == -1) continue;

            String key = argument.substring(0, indexOfEquals);
            String value = argument.substring(indexOfEquals + 1);

            valuesMap.put(key, value);
        }
        return valuesMap;
    }

    public Region stringToRegion(String entryBounds) {

        Map<String, String> valuesMap = createValueMap(entryBounds);

        Position pos1 = stringToPosition(valuesMap.get("pos1"));
        Position pos2 = stringToPosition(valuesMap.get("pos2"));
        if (pos1 == null || pos2 == null) return null;

        return new Region(pos1, pos2);
    }

    public Position stringToPosition(String position) {
        if (position == null) return null;

        String[] coordinates = position.split(",");
        if (coordinates.length != 3) return null;

        try {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int z = Integer.parseInt(coordinates[2]);

            return new Position(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
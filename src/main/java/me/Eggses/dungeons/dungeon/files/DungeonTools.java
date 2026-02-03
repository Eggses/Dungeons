package me.Eggses.dungeons.dungeon.files;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.items.DungeonItems;
import me.Eggses.dungeons.dungeon.items.management.DungeonTool;
import me.Eggses.dungeons.items.ItemTemplate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DungeonTools {

    private static final String FILE_NAME = "dungeon_tools.yml";

    private final ConfigurationFile configurationFile;
    private final DungeonItems<DungeonTool> dungeonTools;

    public DungeonTools(JavaPlugin javaPlugin,
                        DungeonItems<DungeonTool> dungeonTools) {

        this.dungeonTools = dungeonTools;
        this.configurationFile = new ConfigurationFile(javaPlugin, FILE_NAME);

        readIntoDungeonTools();
    }

    private void readIntoDungeonTools() {

        for (DungeonTool dungeonTool : DungeonTool.values()) {

            ConfigurationSection itemSection = configurationFile.getCustomFile()
                    .getConfigurationSection(dungeonTool.getConfigurationSectionName());

            if (itemSection == null) continue;
            String name = itemSection.getString("name");
            String material = itemSection.getString("material");
            List<String> lore = itemSection.getStringList("lore");

            if (material == null || name == null) continue;

            dungeonTools.addItem(dungeonTool, new ItemTemplate(name, material, lore));
        }
    }

    public void reload() {
        configurationFile.reloadCustomFile();
        dungeonTools.clear();
        readIntoDungeonTools();
    }
}

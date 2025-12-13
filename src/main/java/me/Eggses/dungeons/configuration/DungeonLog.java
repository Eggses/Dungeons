package me.Eggses.dungeons.configuration;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DungeonLog {

    private static final String FILE_NAME = "log.yml";
    private static final String LIST_PATH = "errors";

    private final ConfigurationFile configurationFile;

    public DungeonLog(JavaPlugin plugin) {
        this.configurationFile = new ConfigurationFile(plugin, FILE_NAME);
    }

    public void addEntryList(List<String> entryList) {
        addEntryAndSaveList(entryList);
    }

    public void addEntry(String entry) {
        addEntryList(List.of(entry));
    }

    public void addEntryAndSaveList(List<String> entryList) {
        List<String> errorList = configurationFile.getCustomFile().getStringList(LIST_PATH);
        errorList.addAll(entryList);
        configurationFile.getCustomFile().set(LIST_PATH, errorList);

        configurationFile.saveCustomFile();
    }
}
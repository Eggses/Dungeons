package me.Eggses.dungeons.configuration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DungeonLog extends ConfigurationFile {

    private static final String FILE_NAME = "log.yml";
    private static final String LIST_PATH = "errors";

    private final JavaPlugin plugin;

    public DungeonLog(JavaPlugin plugin) {
        super(plugin, FILE_NAME);
        this.plugin = plugin;
    }

    public void addEntryList(List<String> entryList) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> addEntryAndSaveList(entryList));
    }

    public void addEntry(String entry) {
        addEntryList(List.of(entry));
    }

    public synchronized void addEntryAndSaveList(List<String> entryList) {

        List<String> errorList = this.getCustomFile().getStringList(LIST_PATH);
        errorList.addAll(entryList);
        this.getCustomFile().set(LIST_PATH, errorList);

        this.saveCustomFile();
    }
}
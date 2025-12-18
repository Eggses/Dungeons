package me.Eggses.dungeons.dungeon.files;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DungeonLog {

    private static final String FILE_NAME = "log.yml";
    private static final String ERROR_LIST_PATH = "errors";
    private static final String ACTIVE_NAME_LIST_PATH = "active_names";

    private final JavaPlugin plugin;
    private final ConfigurationFile configurationFile;
    private final List<String> activeNames;

    public DungeonLog(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configurationFile = new ConfigurationFile(plugin, FILE_NAME);
        this.activeNames = new ArrayList<>(configurationFile.getCustomFile().getStringList(ACTIVE_NAME_LIST_PATH));
    }

    public List<String> getActiveNameList() {
        synchronized (this) {
            return List.copyOf(activeNames);
        }
    }

    public void addActiveName(String name) {
        synchronized (this) {
            activeNames.add(name);
        }
        saveActiveNamesAsync();
    }

    public void removeActiveName(String name) {
        synchronized (this) {
            activeNames.remove(name);
        }
        saveActiveNamesAsync();
    }

    public void saveActiveNamesAsync() {

        List<String> snapshot;
        synchronized (this) {
            snapshot = List.copyOf(activeNames);
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (this) {
                configurationFile.getCustomFile().set(ACTIVE_NAME_LIST_PATH, snapshot);
                configurationFile.saveCustomFile();
            }
        });
    }

    public void addError(String entry) {
        addErrorList(List.of(entry));
    }

    public void addErrorList(List<String> errorsList) {
        addErrorListAsync(errorsList);
    }

    public void addErrorListAsync(List<String> errorsList) {
        List<String> snapshot = List.copyOf(errorsList);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            synchronized (this) {
                List<String> errorList = configurationFile.getCustomFile().getStringList(ERROR_LIST_PATH);
                errorList.addAll(snapshot);
                configurationFile.getCustomFile().set(ERROR_LIST_PATH, errorList);
                configurationFile.saveCustomFile();
            }
        });
    }
}
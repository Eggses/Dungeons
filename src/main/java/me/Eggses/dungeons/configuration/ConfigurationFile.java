package me.Eggses.dungeons.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationFile {

    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration customFile;

    public ConfigurationFile(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        createCustomFile(fileName);
    }

    // Creates File if it does not already exist.
    private void createCustomFile(String fileName) {

        file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    // Reloads the File (Changes made in Config updated to Server)
    public void reloadCustomFile() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    // Save the File (Changes made in Server saved to SSD)
    public void saveCustomFile() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save File: " + customFile + " to " + file);
        }
    }

    // Returns the Config file.
    public FileConfiguration getCustomFile() {
        return customFile;
    }

    store player stats in a config somewhere that would be cool!
}

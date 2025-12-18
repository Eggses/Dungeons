package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import me.Eggses.dungeons.dungeon.portals.DungeonPortal;
import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.utility.MessageCreator;
import me.Eggses.dungeons.utility.SoundPlayer;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DungeonHeadingReader {

    private final ConfigurationFile configurationFile;
    private final DungeonFileReader dungeonFileReader;
    private final MessageCreator messageCreator;
    private final SoundPlayer soundPlayer;

    public DungeonHeadingReader(ConfigurationFile configurationFile,
                                DungeonFileReader dungeonFileReader,
                                MessageCreator messageCreator,
                                SoundPlayer soundPlayer) {

        this.configurationFile = configurationFile;
        this.dungeonFileReader = dungeonFileReader;
        this.messageCreator = messageCreator;
        this.soundPlayer = soundPlayer;
    }

    public String readTemplateFileName() {
        String templateName = configurationFile.getCustomFile().getString("dungeon_template_name");
        if (templateName == null) throw new IllegalArgumentException("Template File not Defined");
        return templateName;
    }

    public DungeonPortal readDungeonPortal() {

        ConfigurationSection portalConfig =
                configurationFile.getCustomFile().getConfigurationSection("dungeon_portal");
        if (portalConfig == null) return null;

        String worldWithPortal = portalConfig.getString("world_with_portal");
        if (worldWithPortal == null) return null;

        Region entryPortalRegion = dungeonFileReader.stringToRegion(portalConfig.getString("entry_portal_region"));
        if (entryPortalRegion == null) return null;

        Region exitPortalRegion = dungeonFileReader.stringToRegion(portalConfig.getString("exit_portal_region"));
        if (exitPortalRegion == null) return null;

        Position exitPosition = unformattedStringToPosition(portalConfig.getString("world_exit_location"));
        if (exitPosition
                == null) return null;
        Position dungeonSpawnPosition = unformattedStringToPosition(portalConfig.getString("dungeon_spawn_position"));
        if (dungeonSpawnPosition == null) return null;

        int openDurationSeconds = portalConfig.getInt("open_duration_seconds");

        Runnable onOpen = resolveCommandList(portalConfig.getStringList("on_open"));
        Runnable onClose = resolveCommandList(portalConfig.getStringList("on_close"));


        return new DungeonPortal(
                worldWithPortal,
                entryPortalRegion,
                dungeonSpawnPosition,
                exitPortalRegion,
                exitPosition,
                openDurationSeconds,
                onOpen,
                onClose);
    }

    private Runnable resolveCommandList(List<String> commands) {

        List<Runnable> runnables = new ArrayList<>();
        if (commands == null) return compressRunnableList(runnables);

        for (String command : commands) {

            if (command == null) continue;
            command = command.trim();
            if (command.isBlank()) continue;

            String[] arguments = command.split("\\s+", 2);
            String commandName = arguments[0];
            command = (arguments.length == 2) ? arguments[1] : "";

            switch (commandName.toUpperCase()) {

                case "MESSAGE" -> {
                    var runnable = resolveMessageCommand(command);
                    if (runnable != null) runnables.add(runnable);
                }
                case "SOUND" -> {
                    var runnable = resolvePlaySoundCommand(command);
                    if (runnable != null) runnables.add(runnable);
                }
            }

        }
        return compressRunnableList(runnables);
    }

    private Runnable resolveMessageCommand(String text) {

        if (text == null) return null;
        Map<String, String> valuesMap = dungeonFileReader.createValueMap(text);
        Component message = messageCreator.createMessage(valuesMap.get("message"));

        return () -> Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    private Runnable resolvePlaySoundCommand(String soundToPlay) {

        if (soundToPlay == null) return null;
        Map<String, String> valuesMap = dungeonFileReader.createValueMap(soundToPlay);
        Sound sound = soundPlayer.createSound(valuesMap.get("sound"));

        return () -> soundPlayer.playSound(sound, Bukkit.getOnlinePlayers());
    }

    private Position unformattedStringToPosition(String position) {
        if (position == null) return null;
        Map<String, String> valuesMap = dungeonFileReader.createValueMap(position);
        return dungeonFileReader.stringToPosition(valuesMap.get("pos"));
    }

    private Runnable compressRunnableList(List<Runnable> runnables) {
        if (runnables == null) return () -> {};
        List<Runnable> copy = List.copyOf(runnables);
        return () -> copy.forEach(Runnable::run);
    }
}
package me.Eggses.dungeons.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private static final List<String> FILES_TO_DELETE = List.of("session.lock", "uid.dat");

    private final Set<BukkitTask> tasks = new HashSet<>();

    private final JavaPlugin plugin;
    private World dungeonWorld = null;

    public DungeonInstance(JavaPlugin plugin, String dungeonTemplateName) {
        this.plugin = plugin;

        File serverFolder = Bukkit.getWorldContainer();

        createWorld(serverFolder, dungeonTemplateName);

    }

    private void createWorld(File serverFolder, String dungeonTemplateName) {

        BukkitTask makeWorldTask = new BukkitRunnable() {

            @Override
            public void run() {
                createWorldFromTemplate(serverFolder, dungeonTemplateName);
            }

        }.runTaskAsynchronously(plugin);

        BukkitTask waitForWorldTask = new BukkitRunnable() {

            final long startTime = System.currentTimeMillis();
            final long maxAllowedTime = 120000;

            @Override
            public void run() {

                if (dungeonWorld != null) {
                    endDungeonCreationTasks();

                    teleport players into the dungeon?
                            or enable the portal ?
                            ?
                            ?
                            ?
                            yeah probably do that?

                    return;
                }

                if (System.currentTimeMillis() > startTime + maxAllowedTime && dungeonWorld == null) {
                    endDungeonCreationTasks();
                }
            }

        }.runTaskTimer(plugin, 20 * 10, 20);

        tasks.add(makeWorldTask);
        tasks.add(waitForWorldTask);
    }

    private void endDungeonCreationTasks() {
        for (BukkitTask task : tasks) {
            if (task.isCancelled()) continue;
            task.cancel();
        }
        tasks.clear();
    }

    private void createWorldFromTemplate(File serverFolder, String dungeonTemplateName) {

        File sourceDungeonTemplate = new File(serverFolder, dungeonTemplateName);

        if (!(sourceDungeonTemplate.exists() && sourceDungeonTemplate.isDirectory())) {
            plugin.getLogger().severe("Could not copy Dungeon Template: " + dungeonTemplateName + ".");
            return;
        }

        File destinationOfDungeonInstance = new File(serverFolder, produceInstanceName());

        try {
            copyFolderBFS(sourceDungeonTemplate.toPath(), destinationOfDungeonInstance.toPath());
            deleteFilesBFS(destinationOfDungeonInstance.toPath());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not copy Dungeon Template: " + dungeonTemplateName, e);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator worldCreator = new WorldCreator(destinationOfDungeonInstance.getName());
            World world = worldCreator.createWorld();

            if (world == null) {
                plugin.getLogger().severe("Could not load World with Dungeon Template: " + dungeonTemplateName + ".");
                return;
            }

            this.dungeonWorld = world;
        });
    }

    private void copyFolderBFS(Path sourceFolder, Path destinationFolder) throws IOException {

        Queue<File> queueOfFolders = new LinkedList<>();
        Files.createDirectories(destinationFolder); // Makes a Folder WITH that path, NOT in that path!
        queueOfFolders.add(sourceFolder.toFile());

        while (!queueOfFolders.isEmpty()) {

            File folder = queueOfFolders.poll();
            File[] files = folder.listFiles();
            if (files == null) continue;

            for (File file : files) {

                Path childPath = file.toPath();
                Path relativePathInFolder = sourceFolder.relativize(childPath);
                Path specificDestinationPath = destinationFolder.resolve(relativePathInFolder);

                if (file.isDirectory()) {
                    Files.createDirectories(specificDestinationPath);
                    queueOfFolders.offer(file);
                } else {
                    Files.copy(childPath, specificDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private void deleteFilesBFS(Path folderToCheck) throws IOException {

        Set<String> filesToDelete = new HashSet<>(DungeonInstance.FILES_TO_DELETE);
        if (filesToDelete.isEmpty()) return;

        Queue<File> queueOfFolders = new LinkedList<>();
        queueOfFolders.add(folderToCheck.toFile());

        while (!queueOfFolders.isEmpty()) {

            File folder = queueOfFolders.poll();
            File[] files = folder.listFiles();
            if (files == null) continue;

            for (File file : files) {

                if (file.isDirectory()) {
                    queueOfFolders.add(file);
                    continue;
                }
                if (filesToDelete.contains(file.getName())) {
                    filesToDelete.remove(file.getName());
                    Files.delete(file.toPath());
                    if (filesToDelete.isEmpty()) return;
                }
            }
        }
    }

    public abstract String produceInstanceName();

    public Optional<World> getDungeonWorld() {
        return Optional.ofNullable(dungeonWorld);
    }

    public boolean containsPlayer(Player player) {
        if (dungeonWorld == null) return false;
        return dungeonWorld.getPlayers().contains(player);
    }
}
package me.Eggses.dungeons.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;

public abstract class DungeonInstance {

    private static final Set<String> FILES_TO_IGNORE = Set.of("session.lock", "uid.dat");
    private final Set<BukkitTask> tasks = new HashSet<>();

    private boolean created = false;

    private final JavaPlugin plugin;
    private World dungeonWorld = null;


    set keep inventory, no natural spawning etc stuff? maybe in start dungoen method

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
                    startDungeon();
                    return;
                }

                if (System.currentTimeMillis() > startTime + maxAllowedTime && dungeonWorld == null) {
                    endDungeonCreationTasks();
                    plugin.getLogger().severe("Could not start Dungeon: " + dungeonTemplateName + ".");
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
                    if (FILES_TO_IGNORE.contains(file.getName())) continue;
                    Files.copy(childPath, specificDestinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private void startDungeon() {
        this.created = true;
        openPortal();
        Bukkit.getScheduler().runTaskLater(plugin, this::closePortal, 20 * 120);
    }

    public abstract void openPortal();
    public abstract void closePortal();

    public abstract String produceInstanceName();

    public @Nullable World getDungeonWorld() {
        return dungeonWorld;
    }

    public boolean isInDungeon(Player player) {
        if (dungeonWorld == null) return false;
        return dungeonWorld.getPlayers().contains(player);
    }

    public boolean isInNormalWorldPortalRoom() {
        return false;
        this will be like the checking to work out
                like if someone is in a region
                same thing... store 2 points

            return true ifplayer is inside two points

                atually have a region object that stores maybe 2 locations?
            or your cusotom one actually that just ocntains

                class Region:
        Point p1
                Point p2
                        World
                                public Region(Location location).. .get the idea?
                public boolean inside(Location location)... get the idea?
                go with
            if world = this.word
                then check mroe specific

                use point object...
        becuase if you use location you store world twice...
        and world is big...

        maybe store world name not the world? as qorlds have unique names!
        // fix this
    }

    rmeove sadles + bundles on entry:
    maybe on teleport can do this idk?

    OR dont let people take portal with those items in thier inventory!
    thats how you do it....

    and then the keysotne has a can I enter Dungeon button? click it it says Yes, all good
            or No. due to Item: SADDLE, Item:Bundle_white or Item: Bundle_red etc.
}
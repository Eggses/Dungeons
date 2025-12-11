package me.Eggses.dungeons.dungeon;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class DungeonCreation {

    private static final Set<String> FILES_TO_IGNORE = Set.of("session.lock", "uid.dat");

    private final JavaPlugin plugin;
    private final String fileNameOfTemplate;
    private final String fileNameOfNewInstance;
    private final Consumer<World> onCreation;
    private final Consumer<Exception> onFailure;

    public DungeonCreation(JavaPlugin plugin,
                           String fileNameOfTemplate,
                           String fileNameOfNewInstance,
                           Consumer<World> onCreation,
                           Consumer<Exception> onFailure) {

        this.plugin = plugin;
        this.fileNameOfTemplate = fileNameOfTemplate;
        this.fileNameOfNewInstance = fileNameOfNewInstance;
        this.onCreation = onCreation;
        this.onFailure = onFailure;
    }

    public void attemptToCreateInstance() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::createWorld);
    }

    private void createWorld() {

        File serverFolder = Bukkit.getWorldContainer();

        File sourceTemplateFolder = new File(serverFolder, fileNameOfTemplate);
        if (!(sourceTemplateFolder.exists() && sourceTemplateFolder.isDirectory())) {
            error(new FileNotFoundException(
                    "Dungeon Template Folder not found or not a directory: " + sourceTemplateFolder.getPath() + "."));
            return;
        }

        File destinationOfInstance = new File(serverFolder, fileNameOfNewInstance);
        if (destinationOfInstance.exists()) {
            error(new FileAlreadyExistsException(fileNameOfNewInstance));
            return;
        }

        try {
            copyFolderBFS(sourceTemplateFolder.toPath(), destinationOfInstance.toPath());
        } catch (IOException e) {
            error(e);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator worldCreator = new WorldCreator(destinationOfInstance.getName());
            World world = worldCreator.createWorld();

            if (world != null) {
                onCreation.accept(world);
            } else {
                error(new IllegalArgumentException(
                        "WorldCreator returned null for " + destinationOfInstance.getName() + "."));
            }
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

    private void error(Exception exception) {
        Bukkit.getScheduler().runTask(plugin, () -> onFailure.accept(exception));
    }
}
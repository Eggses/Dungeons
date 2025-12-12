package me.Eggses.dungeons.dungeon;

import org.bukkit.Bukkit;
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
import java.util.*;
import java.util.function.Consumer;

public class DungeonWorldManager {

    private static final Set<String> FILES_TO_IGNORE = Set.of("session.lock", "uid.dat");

    private final JavaPlugin plugin;
    private final String fileNameOfTemplate;
    private final String fileNameOfNewInstance;

    public DungeonWorldManager(JavaPlugin plugin,
                               String fileNameOfTemplate,
                               String fileNameOfNewInstance) {

        this.plugin = plugin;
        this.fileNameOfTemplate = fileNameOfTemplate;
        this.fileNameOfNewInstance = fileNameOfNewInstance;
    }

    public void attemptToCreateInstance(Consumer<World> onCreation, Consumer<Exception> onFailure) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> createWorld(onCreation, onFailure));
    }

    private void createWorld(Consumer<World> onCreation, Consumer<Exception> onFailure) {

        File serverFolder = Bukkit.getWorldContainer();

        File sourceTemplateFolder = new File(serverFolder, fileNameOfTemplate);
        if (!(sourceTemplateFolder.exists() && sourceTemplateFolder.isDirectory())) {
            error(new FileNotFoundException(
                    "Dungeon Template Folder not found or not a directory: " + sourceTemplateFolder.getPath() + "."),
                    onFailure);
            return;
        }

        File destinationOfInstance = new File(serverFolder, fileNameOfNewInstance);
        if (destinationOfInstance.exists()) {
            error(new FileAlreadyExistsException(fileNameOfNewInstance), onFailure);
            return;
        }

        try {
            copyFolderBFS(sourceTemplateFolder.toPath(), destinationOfInstance.toPath());
        } catch (IOException e) {
            error(e, onFailure);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator worldCreator = new WorldCreator(destinationOfInstance.getName());
            World world = worldCreator.createWorld();

            if (world != null) {
                onCreation.accept(world);
            } else {
                error(new IllegalArgumentException(
                        "WorldCreator returned null for " + destinationOfInstance.getName() + "."), onFailure);
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

    public void attemptToDeleteInstance(Consumer<Exception> onFailure) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> deleteInstance(onFailure));
    }

    private void deleteInstance(Consumer<Exception> onFailure) {


        File folderToDelete = new File(fileNameOfNewInstance);
        if (!(folderToDelete.exists() && folderToDelete.isDirectory())) {
            error(new FileNotFoundException("Folder to delete could not be found or not a directory: "
                    + folderToDelete.getPath()), onFailure);
        }

        try {
            deleteFolderBFS(folderToDelete);
        } catch (IOException e) {
            error(e, onFailure);
        }
    }

    private void deleteFolderBFS(File startingFolder) throws IOException {

        Queue<File> folders = new LinkedList<>();
        folders.add(startingFolder);

        List<File> foldersToDelete = new ArrayList<>();

        while (!folders.isEmpty()) {

            File folder = folders.poll();
            foldersToDelete.add(folder);
            File[] files = folder.listFiles();
            if (files == null) continue;

            for (File file : files) {
                if (file.isDirectory()) {
                    folders.offer(file);
                } else {
                    Files.delete(file.toPath());
                }
            }
        }

        Collections.reverse(foldersToDelete);
        for (File folder : foldersToDelete) {
            Files.delete(folder.toPath());
        }
    }

    private void error(Exception exception, Consumer<Exception> onFailure) {
        Bukkit.getScheduler().runTask(plugin, () -> onFailure.accept(exception));
    }
}
package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.dungeon.files.DungeonLog;

import java.util.*;

public class InstanceNameManager {

    private static final String FOLDER_NAME = "dungeon_instance_";
    private final Set<String> folderNames = new HashSet<>();

    private final DungeonLog dungeonLog;

    public InstanceNameManager(DungeonLog dungeonLog) {
       this.dungeonLog = dungeonLog;
       folderNames.addAll(dungeonLog.getActiveNameList());
    }

    public synchronized String generateFolderName() {

        int next = 1;
        String name;
        while (folderNames.contains(name = FOLDER_NAME + next)) {
            next++;
        }

        folderNames.add(name);
        dungeonLog.addActiveName(name);
        return name;
    }

    public synchronized void freeFolderName(String folderName) {
        folderNames.remove(folderName);
        dungeonLog.removeActiveName(folderName);
    }

    public static String getInstancePrefix() {
        return FOLDER_NAME;
    }
}
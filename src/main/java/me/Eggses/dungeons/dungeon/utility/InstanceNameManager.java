package me.Eggses.dungeons.dungeon.utility;

import me.Eggses.dungeons.dungeon.files.DungeonLog;
import me.Eggses.dungeons.dungeon.types.DungeonType;

import java.util.*;

public class InstanceNameManager {

    private static final String INSTANCE_INFIX = "_instance_";
    private final Set<String> folderNames = new HashSet<>();

    private final DungeonLog dungeonLog;

    public InstanceNameManager(DungeonLog dungeonLog) {
       this.dungeonLog = dungeonLog;
       folderNames.addAll(dungeonLog.getActiveNameList());
    }

    public synchronized String generateFolderName(DungeonType dungeonType) {

        String folderName = dungeonType.getUniqueKey() + INSTANCE_INFIX;

        int next = 1;
        String name;
        while (folderNames.contains(name = folderName + next)) {
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

    public static String getInstanceInfix() {
        return INSTANCE_INFIX;
    }
}
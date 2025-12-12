package me.Eggses.dungeons.dungeon.utility;

import java.util.*;

public class InstanceNameManager {

    private static final String FOLDER_NAME = "dungeon_instance_";
    private static final int INDEX_OF_INSTANCE_COUNT = 2;

    private final List<String> folderNames = new ArrayList<>();

    public InstanceNameManager() {
    }

    public String generateFolderName() {

        Set<Integer> valuesUsed = new TreeSet<>();

        for (String folderName : folderNames) {
            String[] parts = folderName.split("_");
            if (parts.length <= INDEX_OF_INSTANCE_COUNT) continue;

            String numberAsString = parts[INDEX_OF_INSTANCE_COUNT];

            try {
                valuesUsed.add(Integer.parseInt(numberAsString));
            } catch (NumberFormatException ignored) {

            }
        }

        int next = 1;
        for (int value : valuesUsed) {
            if (next == value) {
                next++;
            }
        }

        String name = FOLDER_NAME + next;
        folderNames.add(name);
        return name;
    }

    public void freeFolderName(String folderName) {
        folderNames.remove(folderName);
    }
}
package me.Eggses.dungeons.entities.nameutility;

import me.Eggses.dungeons.entities.dungeonentity.mobs.DungeonEntity;
import me.Eggses.dungeons.entities.dungeonentity.mobs.MobName;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;

import java.util.Map;
import java.util.TreeMap;

public class NameFormatter {

    private static final Map<Integer, String> levelColourMap = new TreeMap<>();

    static {
        levelColourMap.put(0, "<green>");
        levelColourMap.put(10, "<blue>");
        levelColourMap.put(20, "<gold>");
        levelColourMap.put(30, "<light_purple>");
        levelColourMap.put(40, "<white>");
        levelColourMap.put(50, "<red>");
    }

    private final DungeonEntity dungeonEntity;
    private final MessageCreator messageCreator;

    private Component existingName = null;

    public NameFormatter(DungeonEntity dungeonEntity, MessageCreator messageCreator) {
        this.dungeonEntity = dungeonEntity;
        this.messageCreator = messageCreator;
    }

    public Component createName(int health) {

        Component healthValue = messageCreator.createMessage("<red>" + health + "<dark_gray>hp");

        if (existingName == null) {
            existingName = createLevelPart(dungeonEntity.getDungeonLevel()).append(createNamePart());
        }

        return existingName.append(healthValue);
    }

    private Component createLevelPart(int dungeonLevel) {

        String colour = levelColourMap.get(0);

        for (Map.Entry<Integer, String> entry : levelColourMap.entrySet()) {
            int level = entry.getKey();
            if (dungeonLevel < level) break;
            colour = entry.getValue();
        }

        String levelName = "<dark_gray>Lvl</dark_gray>" + colour + dungeonLevel + " ";

        return messageCreator.createMessage(levelName);
    }

    private Component createNamePart() {

        String name;
        MobName mobName = dungeonEntity.getMobName();

        if (mobName.isOverrideName()) {
            name = mobName.getName() + " ";
        } else {

            String enumName = dungeonEntity.getEntity().getType().name();
            StringBuilder cleanedName = new StringBuilder();

            String[] splitName = enumName.split("_");

            for (String part : splitName) {
                if (part.length() == 1) {
                    cleanedName.append(part).append(" ");
                    continue;
                }

                String restInLowerCase = part.substring(1).toLowerCase();

                String full = part.charAt(0) + restInLowerCase + " ";

                cleanedName.append(full);
            }
            name = cleanedName.toString();
        }
        return messageCreator.createMessage("<white>" + name);
    }
}
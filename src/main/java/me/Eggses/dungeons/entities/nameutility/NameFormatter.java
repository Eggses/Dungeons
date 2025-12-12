package me.Eggses.dungeons.entities.nameutility;

import me.Eggses.dungeons.entities.mobs.DungeonEntity;
import me.Eggses.dungeons.utility.MessageCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NameFormatter {

    private static final Map<Integer, String> LEVEL_COLOUR_MAP = new TreeMap<>();
    private static final Map<EntityType, String> ENTITY_NAME_CACHE = new HashMap<>();

    static {
        LEVEL_COLOUR_MAP.put(0, "<green>");
        LEVEL_COLOUR_MAP.put(10, "<blue>");
        LEVEL_COLOUR_MAP.put(20, "<gold>");
        LEVEL_COLOUR_MAP.put(30, "<light_purple>");
        LEVEL_COLOUR_MAP.put(40, "<white>");
        LEVEL_COLOUR_MAP.put(50, "<red>");
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

        String colour = LEVEL_COLOUR_MAP.get(0);

        for (Map.Entry<Integer, String> entry : LEVEL_COLOUR_MAP.entrySet()) {
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

            EntityType entityType = dungeonEntity.getEntity().getType();
            name = ENTITY_NAME_CACHE.get(entityType);

            if (name == null) {
                name = createNameFromEnum(entityType);
                ENTITY_NAME_CACHE.put(entityType, name);
            }
        }
        return messageCreator.createMessage("<white>" + name);
    }

    private String createNameFromEnum(EntityType entityType) {

        StringBuilder cleanedName = new StringBuilder();

        String[] splitName = entityType.name().split("_");

        for (String part : splitName) {
            if (part.length() == 1) {
                cleanedName.append(part).append(" ");
                continue;
            }

            String restInLowerCase = part.substring(1).toLowerCase();

            String full = part.charAt(0) + restInLowerCase + " ";

            cleanedName.append(full);
        }
        return cleanedName.toString();
    }
}
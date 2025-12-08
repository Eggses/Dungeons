package me.Eggses.dungeons.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class MessageCreator {

    private final LegacyComponentSerializer OLD_MESSAGE = LegacyComponentSerializer.legacyAmpersand();
    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Map<String, String> getPlaceholderMap() {
        return new HashMap<>();
    }

    public Component createMessage(String text) {
        return createMessage(text, getPlaceholderMap());
    }

    public Component createMessage(String text, Map<String, String> placeholders) {

        if (text == null) text = "MISSING TEXT VALUE";
        if (placeholders == null) placeholders = getPlaceholderMap();

       // placeholders.put(Placeholder.PREFIX.getPlaceholderString(), Messages.PREFIX.getMessage());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            String value = entry.getValue();

            text = text.replace(placeholder, value);
        }

        return applyColour(text);
    }

    private Component applyColour(String text) {

        Component message;

        if (text.matches(".*[§&][0-9a-fk-or].*")) {
            text = text.replace("§", "&");
            message = OLD_MESSAGE.deserialize(text);
        } else {
            message = MINI_MESSAGE.deserialize(text);
        }

        return message;
    }
}

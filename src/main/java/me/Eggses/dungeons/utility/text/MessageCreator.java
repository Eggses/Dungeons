package me.Eggses.dungeons.utility.text;

import me.Eggses.dungeons.utility.placeholder.Placeholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageCreator {

    private final LegacyComponentSerializer OLD_MESSAGE = LegacyComponentSerializer.legacyAmpersand();
    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public Component createMessage(String text) {
        return createMessage(text, new Placeholders());
    }

    public Component createMessage(String text, Placeholders placeholders) {

        if (text == null) text = "MISSING TEXT VALUE";
        if (placeholders == null) placeholders = new Placeholders();

        text = placeholders.replace(text);

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

package me.Eggses.dungeons.utility.text;

import me.Eggses.dungeons.configuration.ConfigurationFile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageCreator {

    private final LegacyComponentSerializer OLD_MESSAGE = LegacyComponentSerializer.legacyAmpersand();
    private final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private final FileConfiguration messagesFile;

    public MessageCreator(ConfigurationFile messagesFile) {
        this.messagesFile = messagesFile.getCustomFile();
    }

    public Placeholders placeholders() {
        Placeholders placeholders = new Placeholders();

        String prefixValue = this.messagesFile.getString(Messages.PREFIX_MAIN.getPath());
        placeholders.addPlaceholder(Placeholder.PREFIX_MAIN, prefixValue);

        String errorValue = this.messagesFile.getString(Messages.PREFIX_ERROR.getPath());
        placeholders.addPlaceholder(Placeholder.PREFIX_ERROR, errorValue);

        return placeholders;
    }

    public Component createMessage(Messages message, Placeholders placeholders) {
        return createMessage(messagesFile.getString(message.getPath()), placeholders);
    }

    @Deprecated
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

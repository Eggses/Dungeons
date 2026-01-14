package me.Eggses.dungeons.utility.text;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {

    private final Map<String, String> placeholders = new HashMap<>();

    Placeholders() {
    }

    public void addPlaceholder(Placeholder placeholder, String value) {
        placeholders.put(placeholder.getPlaceholder(), value);
    }

    public void addAll(Placeholders other) {
        placeholders.putAll(other.placeholders);
    }

    public String replace(String text) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }
}

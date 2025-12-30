package me.Eggses.dungeons.utility.placeholder;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {

    private final Map<String, String> placeholders = new HashMap<>();

    public void addPlaceholder(Placeholder placeholder, String value) {
        placeholders.put(placeholder.getPlaceholder(), value);
    }

    public String replace(String text) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }
}
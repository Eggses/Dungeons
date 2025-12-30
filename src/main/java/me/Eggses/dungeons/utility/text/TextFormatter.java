package me.Eggses.dungeons.utility.text;

public class TextFormatter {

    public static final String SPLITTER_UNDERSCORE = "_";
    public static final String SPLITTER_INNER_WORD = "(?<=[a-z])(?=[A-Z])";

    public static final String SEPARATOR_SPACE = " ";
    public static final String SEPARATOR_UNDERSCORE = "_";

    public TextFormatter() {
    }

    public String formatName(String name, String wordSplitter, String wordSeparator) {

        if (name == null || name.isEmpty()) return "";

        StringBuilder cleanedName = new StringBuilder();
        String[] words = name.split(wordSplitter);

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            if (word.length() == 1) {
                cleanedName.append(word);
            } else {
                cleanedName
                        .append(word.charAt(0))
                        .append(word.substring(1).toLowerCase());
            }

            if (i < words.length - 1) {
                cleanedName.append(wordSeparator);
            }
        }
        return cleanedName.toString();
    }
}
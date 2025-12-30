package me.Eggses.dungeons.dungeon.files.reading;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.dungeon.regions.RotationPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ReadingUtility {

    public static final String ARG_POS = "pos";
    public static final String ARG_ROTATION = "rotation";
    public static final String ARG_POS_1 = "pos1";
    public static final String ARG_POS_2 = "pos2";

    public Region stringToRegion(String entryBounds) {

        if (entryBounds == null) return null;

        Map<String, String> valuesMap = createValueMap(entryBounds);

        Position pos1 = stringToPosition(valuesMap.get(ARG_POS_1));
        Position pos2 = stringToPosition(valuesMap.get(ARG_POS_2));
        if (pos1 == null || pos2 == null) return null;

        return new Region(pos1, pos2);
    }

    public Position stringToPosition(String position) {
        if (position == null) return null;

        String[] coordinates = position.split(",");
        if (coordinates.length != 3) return null;

        try {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int z = Integer.parseInt(coordinates[2]);

            return new Position(x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public RotationPosition stringToRotationPosition(String rotationPosition) {

        if (rotationPosition == null) return null;

        Map<String, String> valuesMap = createValueMap(rotationPosition);

        Position position = stringToPosition(valuesMap.get(ARG_POS));
        Float number = toNumber(valuesMap.get(ARG_ROTATION), Float::parseFloat);

        if (position == null || number == null) return null;

        return new RotationPosition(position, number);
    }

    public <T extends Number> T toNumber(String numberString, Function<String, T> parseFunction) {
        if (numberString == null) return null;
        try {
            return parseFunction.apply(numberString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Map<String, String> createValueMap(String command) {

        String[] arguments = command.split("\\s+");

        Map<String, String> valuesMap = new HashMap<>();

        for (String argument : arguments) {

            int indexOfEquals = argument.indexOf('=');
            if (indexOfEquals == -1) continue;

            String key = argument.substring(0, indexOfEquals);
            String value = argument.substring(indexOfEquals + 1);

            valuesMap.put(key, value);
        }
        return valuesMap;
    }

    public <T, U, R> List<R> unknownTwoKeyMapToListR(List<Map<?, ?>> listOfMaps,
                                                     Object key1,
                                                     Object key2,
                                                     Function<Object, T> key1Parse,
                                                     Function<Object, U> key2Parse,
                                                     BiFunction<T, U, R> combiner) {

        if (listOfMaps == null) return List.of();

        List<R> list = new ArrayList<>();

        for (Map<?, ?> map : listOfMaps) {
            T t = key1Parse.apply(map.get(key1));
            U u = key2Parse.apply(map.get(key2));

            if (t == null || u == null) continue;

            list.add(combiner.apply(t, u));
        }

        return list;
    }

    public List<String> unkownListToStringList(List<?> list) {

        List<String> stringList = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof String string) {
                stringList.add(string);
            }
        }
        return stringList;
    }
}
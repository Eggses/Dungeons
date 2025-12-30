package me.Eggses.dungeons.dungeon.files.templates.builders;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class ActionTemplate<T> {

    private final T t;
    private final List<String> commands;

    public ActionTemplate(T t, List<String> commands) {
        this.t = t;
        this.commands = commands;
    }

    public T getT() {
        return t;
    }

    public List<String> getCommands() {
        return commands;
    }
}
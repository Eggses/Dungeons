package me.Eggses.dungeons.dungeon.files.templates.builders;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class AreaTemplate {

    private final Region entryBounds;
    private final List<String> onEntryCommands;
    private final List<String> onClearCommands;
    private final List<ActionTemplate<Position>> interactionsTemplates;
    private final List<ActionTemplate<String>> triggerTemplates;

    public AreaTemplate(Region entryBounds,
                        List<String> onEntryCommands,
                        List<String> onClearCommands,
                        List<ActionTemplate<Position>> interactionsTemplates,
                        List<ActionTemplate<String>> triggerTemplates) {

        this.entryBounds = entryBounds;
        this.onEntryCommands = onEntryCommands;
        this.onClearCommands = onClearCommands;
        this.interactionsTemplates = interactionsTemplates;
        this.triggerTemplates = triggerTemplates;
    }

    public Region getEntryBounds() {
        return entryBounds;
    }

    public List<String> getOnEntryCommands() {
        return onEntryCommands;
    }

    public List<String> getOnClearCommands() {
        return onClearCommands;
    }

    public List<ActionTemplate<Position>> getInteractionsTemplates() {
        return interactionsTemplates;
    }

    public List<ActionTemplate<String>> getTriggerTemplates() {
        return triggerTemplates;
    }
}
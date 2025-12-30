package me.Eggses.dungeons.dungeon.files.templates;

import me.Eggses.dungeons.dungeon.regions.Position;
import me.Eggses.dungeons.dungeon.regions.Region;
import me.Eggses.dungeons.items.ItemStackTemplate;
import org.bukkit.World;

public record NonInstanceDungeonTemplate(String dungeonName,
                                         World dungeonPortalRoomWorld,
                                         Position positionOfKeyStone,
                                         Region generalPortalRoomRegion,
                                         String keystoneName,
                                         ItemStackTemplate itemStackTemplate) {
}
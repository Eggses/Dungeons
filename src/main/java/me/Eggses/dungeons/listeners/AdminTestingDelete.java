package me.Eggses.dungeons.listeners;

import me.Eggses.dungeons.entities.equipment.ArmourEquipment;
import me.Eggses.dungeons.entities.mobs.MobBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AdminTestingDelete implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Location location = event.getBlock().getLocation().add(2, 0, 2);

        MobBuilder builder = new MobBuilder(Skeleton.class, location);

    }



}

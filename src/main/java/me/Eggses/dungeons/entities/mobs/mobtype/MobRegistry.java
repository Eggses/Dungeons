package me.Eggses.dungeons.entities.mobs.mobtype;

import me.Eggses.dungeons.entities.mobs.MobBuilder;
import me.Eggses.dungeons.entities.mobs.mobtype.types.*;
import me.Eggses.dungeons.utility.sound.SoundPlayer;
import me.Eggses.dungeons.utility.text.TextFormatter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class MobRegistry {

    private final JavaPlugin plugin;
    private final MobUtility mobUtility;
    private final TextFormatter textFormatter;
    private final SoundPlayer soundPlayer;

    private final Map<MobType, Consumer<MobBuilder>> mobPresets = new EnumMap<>(MobType.class);

    public MobRegistry(JavaPlugin plugin, MobUtility mobUtility, TextFormatter textFormatter, SoundPlayer soundPlayer) {
        this.plugin = plugin;
        this.mobUtility = mobUtility;
        this.textFormatter = textFormatter;
        this.soundPlayer = soundPlayer;

        registerMobs();
    }

    private void registerMobs() {
        mobPresets.put(MobType.KNIGHT, new Knight(mobUtility, textFormatter).getBuilderConsumer());
        mobPresets.put(MobType.FIEND, new Fiend(mobUtility, textFormatter).getBuilderConsumer());
        mobPresets.put(MobType.BRUISER, new Bruiser(mobUtility, textFormatter).getBuilderConsumer());
        mobPresets.put(MobType.ENCHANTER, new Enchanter(mobUtility, textFormatter, soundPlayer).getBuilderConsumer());
        mobPresets.put(MobType.NOXIOUS_CULTIVATOR, new NoxiousCultivator(mobUtility, textFormatter).getBuilderConsumer());
        mobPresets.put(MobType.VILLAGER, new Villager(mobUtility).getBuilderConsumer());
        mobPresets.put(MobType.BEEHIVE_CREEPER, new BeehiveCreeper(textFormatter).getBuilderConsumer());
    }

    public Consumer<MobBuilder> getPreset(String mobType) {
        MobType type = MobType.getMobType(mobType);
        if (type == null) return null;
        return mobPresets.get(type);
    }
}

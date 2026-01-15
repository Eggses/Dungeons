package me.Eggses.dungeons.items;

import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholders;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemHandler {

    public static final Consumer<ItemMeta> NO_DISPLAY = (itemMeta -> itemMeta.setHideTooltip(true));

    private final ItemKey itemKey;
    private final MessageCreator messageCreator;

    public ItemHandler(ItemKey itemKey, MessageCreator messageCreator) {
        this.itemKey = itemKey;
        this.messageCreator = messageCreator;
    }

    public ItemStack createItem(ItemTemplate itemTemplate, Placeholders placeholders) {
        return createItem(itemTemplate, placeholders, null);
    }

    public ItemStack createItem(ItemTemplate itemTemplate,
                                Placeholders placeholders,
                                Consumer<ItemMeta> itemMetaTransformer) {

        Component name = messageCreator.createMessage(itemTemplate.name(), placeholders);
        Material material = Material.matchMaterial(itemTemplate.material());
        if (material == null) material = Material.BARRIER;

        List<Component> lore = new ArrayList<>();
        List<String> stringLore = itemTemplate.lore();
        if (stringLore != null) {
            stringLore.forEach(line -> lore.add(messageCreator.createMessage(line, placeholders)));
        }

        ItemStack item = new ItemStack(material, 1);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(name);
        meta.lore(lore);

        if (itemMetaTransformer != null) itemMetaTransformer.accept(meta);

        item.setItemMeta(meta);

        return item;
    }

    public void applyUniqueKey(ItemStack item, String uniqueValue) {

        if (item == null || item.getType().isAir()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(itemKey.getKey(), PersistentDataType.STRING, uniqueValue);

        item.setItemMeta(meta);
    }
}

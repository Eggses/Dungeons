package me.Eggses.dungeons.items;

import me.Eggses.dungeons.utility.text.MessageCreator;
import me.Eggses.dungeons.utility.text.Placeholders;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemCreator {

    public static final Consumer<ItemMeta> STACK_SIZE_1 = itemMeta -> {
        itemMeta.setMaxStackSize(1);
    };

    private final MessageCreator messageCreator;
    private final ItemKeyManager itemKeyManager;

    public ItemCreator(ItemKeyManager itemKeyManager, MessageCreator messageCreator) {
        this.itemKeyManager = itemKeyManager;
        this.messageCreator = messageCreator;
    }

    public ItemStack createItem(ItemStackTemplate itemStackTemplate, Placeholders placeholders) {
        return createItem(itemStackTemplate, itemMeta -> {}, placeholders);
    }


    public ItemStack createItem(ItemRecord itemRecord) {
        ItemStack item = createItem(itemRecord.itemStackTemplate(), itemRecord.itemMetaConsumer(), itemRecord.placeholders());
        item = applyUniqueKey(item, itemRecord.uniqueKey());
        return item;
    }


    public ItemStack createItem(ItemStackTemplate itemStackTemplate,
                                Consumer<ItemMeta> itemMetaConsumer,
                                Placeholders placeholders) {

        Component name = messageCreator.createMessage(itemStackTemplate.getName(), placeholders);

        List<Component> lore = new ArrayList<>();
        itemStackTemplate.getLore().forEach(stringLine -> lore.add(messageCreator.createMessage(stringLine, placeholders)));

        Material material = getMaterial(itemStackTemplate.getMaterial());

        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        itemMeta.displayName(name);
        itemMeta.lore(lore);
        if (itemStackTemplate.isGlow()) itemMeta.setEnchantmentGlintOverride(true);

        itemMetaConsumer.accept(itemMeta);

        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemStack applyUniqueKey(ItemStack item, String uniqueValue) {

        if (item == null || item.getType().isAir()) return item;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.getPersistentDataContainer().set(itemKeyManager.getKey(), PersistentDataType.STRING, uniqueValue);

        item.setItemMeta(meta);
        return item;
    }

    private Material getMaterial(String materialString) {
        Material material = Material.matchMaterial(materialString);
        return (material == null) ? Material.BARRIER : material;
    }
}
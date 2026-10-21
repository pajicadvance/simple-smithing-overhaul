package me.pajic.simple_smithing_overhaul.util;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.compat.EDCompat;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentInfoExporter {

    public static void export(HolderLookup.Provider registries, Path file) throws IOException {
        List<Holder.Reference<Enchantment>> enchantments = new ArrayList<>(
                registries.lookupOrThrow(Registries.ENCHANTMENT).listElements().toList()
        );
        enchantments.sort(Comparator.comparing(Holder::getRegisteredName));

        Map<List<Holder.Reference<Enchantment>>, List<String>> groups = new LinkedHashMap<>();
        List<Item> items = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(items::add);
        items.sort(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()));
        for (Item item : items) {
            ItemStack stack = item.getDefaultInstance();
            if (!stack.is(ItemTags.DURABILITY_ENCHANTABLE) || !stack.has(DataComponents.MAX_DAMAGE) || stack.getMaxStackSize() != 1) continue;
            List<Holder.Reference<Enchantment>> supported = enchantments.stream().filter(e -> e.value().canEnchant(stack)).toList();
            if (supported.isEmpty()) continue;
            groups.computeIfAbsent(supported, k -> new ArrayList<>()).add(BuiltInRegistries.ITEM.getKey(item).toString());
        }

        List<String> lines = new ArrayList<>();
        lines.add("Simple Smithing Overhaul - enchantments supported by items that can be pinnacle upgraded");
        lines.add("");
        lines.add("An item is \"maxed out\" when it has every enchantment it supports at max level, accounting for exclusive enchantments.");
        lines.add("Enchantments can be excluded from this check with the \"Enchantments excluded from maxed out check\" config option.");
        lines.add("");
        lines.add("Tags:");
        lines.add("  [excluded]     Excluded in the config: not required for an item to be maxed out, and never upgraded.");
        lines.add("  [disabled]     Disabled by Enchantment Disabler: not required for an item to be maxed out, and never upgraded.");
        lines.add("  [curse]        Not required for an item to be maxed out, but cursed items can't be pinnacle upgraded.");
        lines.add("  [max level 1]  Required for an item to be maxed out, but never upgraded.");
        lines.add("");
        lines.add("All enchantments:");
        for (Holder.Reference<Enchantment> enchantment : enchantments) {
            lines.add("  " + describe(enchantment, enchantments));
        }

        groups.forEach((supported, groupItems) -> {
            lines.add("");
            lines.add("=".repeat(80));
            lines.add("Items: " + String.join(", ", groupItems));
            lines.add("Supported enchantments:");
            for (Holder.Reference<Enchantment> enchantment : supported) {
                lines.add("  " + describe(enchantment, supported));
            }
        });

        Files.createDirectories(file.getParent());
        Files.write(file, lines);
    }

    private static String describe(Holder.Reference<Enchantment> enchantment, List<Holder.Reference<Enchantment>> context) {
        StringBuilder line = new StringBuilder(enchantment.getRegisteredName())
                .append(" (").append(Enchantment.getFullname(enchantment, enchantment.value().getMaxLevel()).getString()).append(")");
        if (SSO.CONFIG.pinnacleEnchantment.excludedFromMaxedOutCheck.stream().anyMatch(enchantment::is)) line.append(" [excluded]");
        if (CompatFlags.ED_LOADED && !EDCompat.enchantmentEnabled(enchantment)) line.append(" [disabled]");
        if (enchantment.is(EnchantmentTags.CURSE)) line.append(" [curse]");
        if (enchantment.value().getMaxLevel() == 1) line.append(" [max level 1]");
        List<String> exclusive = context.stream()
                .filter(other -> !other.equals(enchantment) && !Enchantment.areCompatible(enchantment, other))
                .map(Holder::getRegisteredName)
                .toList();
        if (!exclusive.isEmpty()) line.append(" - exclusive with: ").append(String.join(", ", exclusive));
        return line.toString();
    }
}

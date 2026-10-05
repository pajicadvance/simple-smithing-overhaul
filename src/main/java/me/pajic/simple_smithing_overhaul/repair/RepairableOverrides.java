package me.pajic.simple_smithing_overhaul.repair;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

//? <26.1 {
/*import me.pajic.simple_smithing_overhaul.backport.Repairable;
 *///?} else {
import net.minecraft.world.item.enchantment.Repairable;
//?}

public final class RepairableOverrides {

    private static final Map<String, String> VANILLA_REPAIRABLES = Map.ofEntries(
            Map.entry("minecraft:bow", "minecraft:string"),
            Map.entry("minecraft:crossbow", "minecraft:string"),
            Map.entry("minecraft:fishing_rod", "minecraft:string"),
            Map.entry("minecraft:flint_and_steel", "minecraft:iron_ingot"),
            Map.entry("minecraft:shears", "minecraft:iron_ingot"),
            Map.entry("minecraft:brush", "minecraft:feather"),
            Map.entry("minecraft:carrot_on_a_stick", "minecraft:carrot"),
            Map.entry("minecraft:warped_fungus_on_a_stick", "minecraft:warped_fungus")
    );

    private static volatile Map<Item, Repairable> table = new Reference2ObjectOpenHashMap<>();

    public static void computeAndSet(HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Item> lookup = registries.lookupOrThrow(Registries.ITEM);

        Map<String, String> entries = new LinkedHashMap<>();
        if (SSO.CONFIG.streamlinedRepairs.vanillaRepairables.get()) {
            entries.putAll(VANILLA_REPAIRABLES);
            if (!CompatFlags.BETTER_TRIDENTS_LOADED) entries.put("minecraft:trident", "minecraft:prismarine_shard");
        }
        entries.putAll(SSO.CONFIG.streamlinedRepairs.modRepairableItems.get());

        Map<String, Optional<Repairable>> byMaterial = new HashMap<>();
        Map<Item, Repairable> fromTags = new HashMap<>(), fromItems = new HashMap<>();

        entries.forEach((target, material) -> byMaterial.computeIfAbsent(
                material, m -> resolveMaterial(lookup, m)
        ).ifPresent(repairable -> {
            if (target.startsWith("#")) lookup.get(TagKey.create(Registries.ITEM, Identifier.parse(target.substring(1))))
                    .ifPresent(set -> set.forEach(h -> fromTags.put(h.value(), repairable)));
            else lookup.get(ResourceKey.create(Registries.ITEM, Identifier.parse(target)))
                    .ifPresent(h -> fromItems.put(h.value(), repairable));
        }));

        clear();
        table.putAll(fromTags);
        table.putAll(fromItems);
    }

    private static Optional<Repairable> resolveMaterial(HolderLookup.RegistryLookup<Item> lookup, String material) {
        try {
            HolderSet<Item> set = material.startsWith("#")
                    ? lookup.getOrThrow(TagKey.create(Registries.ITEM, Identifier.parse(material.substring(1))))
                    : HolderSet.direct(lookup.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(material))));
            return Optional.of(new Repairable(set));
        } catch (Exception e) {
            SSO.LOGGER.warn("Unable to resolve repair material {}, skipping: {}", material, e.getMessage());
            return Optional.empty();
        }
    }

    public static @Nullable Repairable get(Item item) {
        return table.get(item);
    }

    public static Map<Item, Repairable> snapshot() {
        return table;
    }

    public static void set(Map<Item, Repairable> m) {
        table = new Reference2ObjectOpenHashMap<>(m);
    }

    public static void clear() {
        table = new Reference2ObjectOpenHashMap<>();
    }
}

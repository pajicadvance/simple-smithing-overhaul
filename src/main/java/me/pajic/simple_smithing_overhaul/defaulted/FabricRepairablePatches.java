package me.pajic.simple_smithing_overhaul.defaulted;

//? fabric && >=26.3 {
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FabricRepairablePatches {
    public static void apply(DefaultItemComponentEvents.ModifyContext context, Map<String, String> mappings,
                             BiConsumer<String, RuntimeException> onError) {
        if (mappings.isEmpty()) return;
        context.modify(item -> true, new DefaultItemComponentEvents.ModifyConsumer() {
            private Map<Item, Repairable> patches;

            @Override
            public void modify(DataComponentMap.Builder builder, HolderLookup.Provider registries, Item item) {
                // The event fires after reload tags and vanilla item components are applied.
                if (patches == null) patches = resolve(registries.lookupOrThrow(Registries.ITEM), mappings, onError);
                Repairable repairable = patches.get(item);
                if (repairable != null) builder.set(DataComponents.REPAIRABLE, repairable);
            }
        });
    }

    static Map<Item, Repairable> resolve(HolderLookup<Item> lookup, Map<String, String> mappings,
                                         BiConsumer<String, RuntimeException> onError) {
        Map<Item, Repairable> patches = new IdentityHashMap<>();
        mappings.forEach((target, material) -> {
            try {
                List<Item> items = target.startsWith("#")
                        ? lookup.get(TagKey.create(Registries.ITEM, Identifier.parse(target.substring(1))))
                            .map(tag -> tag.stream().map(Holder::value).toList()).orElseGet(List::of)
                        : lookup.get(ResourceKey.create(Registries.ITEM, Identifier.parse(target)))
                            .map(holder -> List.of(holder.value())).orElseGet(List::of);
                // Optional mod targets may be absent; do not resolve their materials.
                if (items.isEmpty()) return;
                HolderSet<Item> materials = material.startsWith("#")
                        ? lookup.getOrThrow(TagKey.create(Registries.ITEM, Identifier.parse(material.substring(1))))
                        : HolderSet.direct(lookup.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(material))));
                Repairable repairable = new Repairable(materials);
                items.forEach(item -> patches.put(item, repairable));
            } catch (RuntimeException error) {
                onError.accept(target + " -> " + material, error);
            }
        });
        return patches;
    }
}
//?}

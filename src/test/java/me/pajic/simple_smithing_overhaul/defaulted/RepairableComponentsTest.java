package me.pajic.simple_smithing_overhaul.defaulted;

//? fabric && >=26.3 {
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/** Tests reload tag snapshots without requiring a game server or modifying item defaults. */
public class RepairableComponentsTest {
    public static void main(String[] args) {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(VanillaRegistries.createWorldLookup()).forEach(pending -> pending.apply());
        TagKey<Item> targets = TagKey.create(Registries.ITEM, Identifier.parse("sso_test:targets"));
        TagKey<Item> materials = TagKey.create(Registries.ITEM, Identifier.parse("sso_test:materials"));
        var firstReload = BuiltInRegistries.ITEM.prepareTagReload(new TagLoader.LoadResult<>(Registries.ITEM, Map.of(
                targets, List.<Holder<Item>>of(Items.BOW.builtInRegistryHolder(), Items.CROSSBOW.builtInRegistryHolder()),
                materials, List.<Holder<Item>>of(Items.IRON_INGOT.builtInRegistryHolder(), Items.DIAMOND.builtInRegistryHolder())
        )));
        require(BuiltInRegistries.ITEM.get(targets).isEmpty(), "Fixture must use pending tags");
        // Server reload applies pending tags before it applies item components and fires MODIFY.
        firstReload.apply();
        Map<String, String> mappings = new LinkedHashMap<>();
        mappings.put("#sso_test:targets", "#sso_test:materials");
        mappings.put("minecraft:crossbow", "minecraft:string");
        mappings.put("missing_mod:optional_item", "invalid material");
        mappings.put("#missing_mod:optional_tag", "invalid material");
        mappings.put("bad target", "minecraft:diamond");
        mappings.put("minecraft:brush", "missing_mod:material");
        mappings.put("minecraft:shears", "#missing_mod:material_tag");
        List<String> errors = new ArrayList<>();
        Context first = new Context(HolderLookup.Provider.create(Stream.of(firstReload.lookup())));
        FabricRepairablePatches.apply(first, mappings, (mapping, error) -> errors.add(mapping));
        var bow = first.components.get(Items.BOW).get(DataComponents.REPAIRABLE);
        require(bow != null && bow.items().unwrapKey().orElseThrow().equals(materials), "Repair material tag not preserved");
        require(bow.items().stream().map(Holder::value).toList().equals(List.of(Items.IRON_INGOT, Items.DIAMOND)), "Pending material tag contents lost");
        require(first.components.get(Items.CROSSBOW).get(DataComponents.REPAIRABLE).items().get(0).value() == Items.STRING, "Explicit item override lost");
        require(first.components.get(Items.BOW).get(DataComponents.MAX_DAMAGE).equals(Items.BOW.components().get(DataComponents.MAX_DAMAGE)), "Other components changed");
        require(errors.size() == 3, "Invalid mappings must log once; missing optional targets must be ignored");
        require(first.components.get(Items.BRUSH).get(DataComponents.REPAIRABLE) == Items.BRUSH.components().get(DataComponents.REPAIRABLE), "Invalid material changed item");

        var secondReload = BuiltInRegistries.ITEM.prepareTagReload(new TagLoader.LoadResult<>(Registries.ITEM, Map.of(
                targets, List.<Holder<Item>>of(Items.BOW.builtInRegistryHolder()),
                materials, List.<Holder<Item>>of(Items.GOLD_INGOT.builtInRegistryHolder())
        )));
        secondReload.apply();
        Context second = new Context(HolderLookup.Provider.create(Stream.of(secondReload.lookup())));
        FabricRepairablePatches.apply(second, Map.of("#sso_test:targets", "#sso_test:materials"), (mapping, error) -> { throw error; });
        require(second.components.get(Items.BOW).get(DataComponents.REPAIRABLE).items().get(0).value() == Items.GOLD_INGOT, "Reload reused stale material tags");
        require(second.components.get(Items.CROSSBOW).get(DataComponents.REPAIRABLE) == Items.CROSSBOW.components().get(DataComponents.REPAIRABLE), "Reload reused stale target membership");

        Context disabled = new Context(first.registries);
        FabricRepairablePatches.apply(disabled, Map.of(), (mapping, error) -> { throw error; });
        require(disabled.components.isEmpty(), "Empty configuration must not change defaults");
        System.out.println("Repairable component regression checks passed");
    }

    private static class Context implements DefaultItemComponentEvents.ModifyContext {
        private final HolderLookup.Provider registries;
        private final Map<Item, DataComponentMap> components = new IdentityHashMap<>();

        private Context(HolderLookup.Provider registries) {
            this.registries = registries;
        }

        @Override
        public void modify(Predicate<Item> predicate, DefaultItemComponentEvents.ModifyConsumer consumer) {
            for (Item item : List.of(Items.BOW, Items.CROSSBOW, Items.BRUSH, Items.SHEARS)) {
                if (!predicate.test(item)) continue;
                var builder = DataComponentMap.builder().addAll(item.components());
                consumer.modify(builder, registries, item);
                components.put(item, builder.build());
            }
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
//?}

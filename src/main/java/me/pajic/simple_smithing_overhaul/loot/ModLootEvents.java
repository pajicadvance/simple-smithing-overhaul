package me.pajic.simple_smithing_overhaul.loot;

import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;

public class ModLootEvents {

    public static void init() {
        LootTableEvents.MODIFY.register((resourceKey, builder, lootTableSource, provider) -> {
            if (lootTableSource.isBuiltin()) {
                if (BuiltInLootTables.END_CITY_TREASURE.equals(resourceKey)) {
                    builder.pool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE).setWeight(10))
                            .add(EmptyLootItem.emptyItem().setWeight(90)).build()
                    );
                }
                if (BuiltInLootTables.ANCIENT_CITY.equals(resourceKey)) {
                    builder.pool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE).setWeight(10))
                            .add(EmptyLootItem.emptyItem().setWeight(90)).build()
                    );
                }
            }
        });
    }
}

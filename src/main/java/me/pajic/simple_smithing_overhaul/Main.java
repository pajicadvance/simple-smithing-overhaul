package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("simple_smithing_overhaul")
public class Main {

    public static int cost = 0;

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SERVER_SPEC);
        modEventBus.addListener(this::registerItems);
        modEventBus.addListener(this::addCreative);
        NeoForge.EVENT_BUS.addListener(this::addEndCityLoot);
        NeoForge.EVENT_BUS.addListener(this::onAnvilUse);
        modEventBus.addListener(this::onInitialize);
    }

    private void registerItems(RegisterEvent event) {
        event.register(Registries.ITEM, registry -> {
            registry.register(ResourceLocation.parse(
                    "simple_smithing_overhaul:enchantment_upgrade"),
                    ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
            );
            registry.register(
                    ResourceLocation.parse("simple_smithing_overhaul:whetstone"),
                    ModItems.WHETSTONE
            );
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),
                    ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
        else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(
                    Items.NETHERITE_HOE.getDefaultInstance(),
                    ModItems.WHETSTONE.getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    private void addEndCityLoot(LootTableLoadEvent event) {
        if (event.getName().equals(BuiltInLootTables.END_CITY_TREASURE.location())) {
            event.getTable().addPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE).setWeight(10))
                    .add(EmptyLootItem.emptyItem().setWeight(90)).build());
        }
    }

    private void onAnvilUse(AnvilRepairEvent event) {
        event.setBreakChance((float) ModServerConfig.degradationChance / 100);
    }

    public void onInitialize(FMLCommonSetupEvent event) {
        ModUtil.initAdditionalRepairables();
    }
}

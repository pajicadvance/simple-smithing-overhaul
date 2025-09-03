package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class ModItems {

    //? if <= 1.21.1 {
    public static final Item ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "upgrade",
                    ResourceLocation.parse("simple_smithing_overhaul.enchantment_upgrade")
            )).withStyle(ChatFormatting.GRAY),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.additions_slot_description")
            )),
            List.of(
                    Main.withModNamespace("item/empty_slot_enchanted_book"),
                    Main.withModNamespace("item/empty_slot_whetstone"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe")
            ),
            List.of(ResourceLocation.parse("item/empty_slot_lapis_lazuli")),
            Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()
    );

    public static final Item PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "upgrade",
                    ResourceLocation.parse("simple_smithing_overhaul.pinnacle_enchantment")
            )).withStyle(ChatFormatting.GRAY),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.additions_slot_description")
            )),
            List.of(
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe")
            ),
            List.of(ResourceLocation.parse("simple_smithing_overhaul:item/empty_slot_echo_shard")),
            Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );
    //?}

    //? if > 1.21.1 {
    /*public static final Item ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.additions_slot_description")
            )),
            //? if < 1.21.4 {
            List.of(
                    ResourceLocation.parse("simple_smithing_overhaul:item/empty_slot_enchanted_book"),
                    ResourceLocation.parse("simple_smithing_overhaul:item/empty_slot_whetstone"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe")
            ),
            List.of(ResourceLocation.parse("item/empty_slot_lapis_lazuli")),
            //?}
            //? if >= 1.21.4 {
            /^List.of(
                    Main.withModNamespace("container/slot/enchanted_book"),
                    Main.withModNamespace("container/slot/whetstone"),
                    ResourceLocation.withDefaultNamespace("container/slot/helmet"),
                    ResourceLocation.withDefaultNamespace("container/slot/chestplate"),
                    ResourceLocation.withDefaultNamespace("container/slot/leggings"),
                    ResourceLocation.withDefaultNamespace("container/slot/boots"),
                    ResourceLocation.withDefaultNamespace("container/slot/hoe"),
                    ResourceLocation.withDefaultNamespace("container/slot/axe"),
                    ResourceLocation.withDefaultNamespace("container/slot/sword"),
                    ResourceLocation.withDefaultNamespace("container/slot/shovel"),
                    ResourceLocation.withDefaultNamespace("container/slot/pickaxe")
            ),
            List.of(ResourceLocation.parse("container/slot/lapis_lazuli")),
            ^///?}
            new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(
                    Registries.ITEM,
                    Main.withModNamespace("enchantment_upgrade")
            )),
            Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()
    );

    public static final Item PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.additions_slot_description")
            )),
            //? if < 1.21.4 {
            List.of(
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
                    ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
                    ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe")
            ),
            List.of(ResourceLocation.parse("simple_smithing_overhaul:item/empty_slot_echo_shard")),
            //?}
            //? if >= 1.21.4 {
            /^List.of(
                    ResourceLocation.withDefaultNamespace("container/slot/helmet"),
                    ResourceLocation.withDefaultNamespace("container/slot/chestplate"),
                    ResourceLocation.withDefaultNamespace("container/slot/leggings"),
                    ResourceLocation.withDefaultNamespace("container/slot/boots"),
                    ResourceLocation.withDefaultNamespace("container/slot/hoe"),
                    ResourceLocation.withDefaultNamespace("container/slot/axe"),
                    ResourceLocation.withDefaultNamespace("container/slot/sword"),
                    ResourceLocation.withDefaultNamespace("container/slot/shovel"),
                    ResourceLocation.withDefaultNamespace("container/slot/pickaxe")
            ),
            List.of(Main.withModNamespace("container/slot/echo_shard")),
            ^///?}
            new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(
                    Registries.ITEM,
                    Main.withModNamespace("pinnacle_enchantment")
            )),
            Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );
    *///?}

    public static final Item WHETSTONE = new WhetstoneItem(
            new Item.Properties()
                    .durability(6)
                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    //? if > 1.21.1 {
                    /*.repairable(Items.QUARTZ)
                    .enchantable(22)
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            Main.withModNamespace("whetstone")
                    ))
                    *///?}
    );

    public static final Item BROKEN_ANVIL = new BlockItem(
            ModBlocks.BROKEN_ANVIL, new Item.Properties()
            //? if > 1.21.1 {
            /*.setId(ResourceKey.create(
                    Registries.ITEM,
                    Main.withModNamespace("broken_anvil")
            ))
            *///?}
    );

    public static void init() {
        Registry.register(
                BuiltInRegistries.ITEM,
                Main.withModNamespace("enchantment_upgrade"),
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                Main.withModNamespace("pinnacle_enchantment"),
                PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE,
                PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                Main.withModNamespace("whetstone"),
                WHETSTONE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(contents -> contents.addAfter(
                Items.NETHERITE_HOE,
                WHETSTONE
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                Main.withModNamespace("broken_anvil"),
                BROKEN_ANVIL
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(contents -> contents.addAfter(
                Items.DAMAGED_ANVIL,
                BROKEN_ANVIL
        ));
    }
}

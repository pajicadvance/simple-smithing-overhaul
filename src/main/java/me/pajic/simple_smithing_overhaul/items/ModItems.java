package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.Main;
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
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
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
            Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading()
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
            Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment()
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
            //? if <= 1.21.3 {
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
            //? if 1.21.4 {
            /^List.of(
                    ResourceLocation.parse("simple_smithing_overhaul:container/slot/enchanted_book"),
                    ResourceLocation.parse("simple_smithing_overhaul:container/slot/whetstone"),
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
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "enchantment_upgrade")
            )),
            Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading()
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
            //? if <= 1.21.3 {
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
            //? if 1.21.4 {
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
            List.of(ResourceLocation.parse("simple_smithing_overhaul:container/slot/echo_shard")),
            ^///?}
            new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "pinnacle_enchantment")
            )),
            Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment()
    );
    *///?}

    public static final Item WHETSTONE = new WhetstoneItem(
            new Item.Properties()
                    .durability(12)
                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    //? if > 1.21.1 {
                    /*.repairable(Items.QUARTZ)
                    .enchantable(1)
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "whetstone")
                    ))
                    *///?}
    );

    public static void init() {
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.parse("simple_smithing_overhaul:enchantment_upgrade"),
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
                Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.parse("simple_smithing_overhaul:pinnacle_enchantment"),
                PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
                ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE,
                PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.parse("simple_smithing_overhaul:whetstone"),
                WHETSTONE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(contents -> contents.addAfter(
                Items.NETHERITE_HOE,
                WHETSTONE
        ));
    }
}

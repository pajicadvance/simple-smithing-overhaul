package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;
//? if > 1.21.1 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
//?}

import java.util.List;

public class ModItems {

    //? if <= 1.21.1 {
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
					SSO.id("item/empty_slot_enchanted_book"),
					SSO.id("item/empty_slot_whetstone"),
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
            SSO.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()
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
            SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );
    *///?} else {
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
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    ResourceLocation.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.additions_slot_description")
            )),
            List.of(
                    SSO.id("container/slot/enchanted_book"),
                    SSO.id("container/slot/whetstone"),
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
            new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("enchantment_upgrade")
            )),
            SSO.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()
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
            List.of(
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
            List.of(SSO.id("container/slot/echo_shard")),
            new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("pinnacle_enchantment")
            )),
            SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );
    //?}

    public static final Item WHETSTONE = new WhetstoneItem(
            new Item.Properties()
                    .durability(6)
                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    //? if > 1.21.1 {
                    .repairable(Items.QUARTZ)
                    .enchantable(22)
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            SSO.id("whetstone")
                    ))
                    //?}
    );

    public static final Item BROKEN_ANVIL = new BlockItem(
            ModBlocks.BROKEN_ANVIL, new Item.Properties()
            //? if > 1.21.1 {
            .setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("broken_anvil")
            ))
            //?}
    );

    public static void init() {}
}

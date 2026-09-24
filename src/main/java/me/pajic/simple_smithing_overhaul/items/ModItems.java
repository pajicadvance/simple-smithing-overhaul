package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

//? >=26.1 {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
//?}

public class ModItems {

    public static final Item ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "upgrade",
                    Identifier.parse("simple_smithing_overhaul.enchantment_upgrade")
            )).withStyle(ChatFormatting.GRAY),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.additions_slot_description")
            )),
            List.of(
                    SSO.id(ModUtil.emptySlotTexturePath("enchanted_book")),
                    SSO.id(ModUtil.emptySlotTexturePath("whetstone")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("helmet")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("chestplate")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("leggings")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("boots")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("hoe")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("axe")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("sword")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("shovel")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("pickaxe"))
            ),
            List.of(Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("lapis_lazuli"))),
            new Item.Properties().rarity(Rarity.RARE)
                    //? >=26.1
                    .setId(ResourceKey.create(Registries.ITEM, SSO.id("enchantment_upgrade")))
			, ModUtil.enchantmentUpgradingEnabled()
    );

    public static final Item PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE = new SmithingTemplateFoilItem(
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.applies_to")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.ingredients")
            )).withStyle(ChatFormatting.BLUE),
            Component.translatable(Util.makeDescriptionId(
                    "upgrade",
                    Identifier.parse("simple_smithing_overhaul.pinnacle_enchantment")
            )).withStyle(ChatFormatting.GRAY),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.additions_slot_description")
            )),
            List.of(
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("helmet")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("chestplate")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("leggings")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("boots")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("hoe")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("axe")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("sword")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("shovel")),
                    Identifier.withDefaultNamespace(ModUtil.emptySlotTexturePath("pickaxe"))
            ),
            List.of(SSO.id(ModUtil.emptySlotTexturePath("echo_shard"))),
            new Item.Properties().rarity(Rarity.EPIC)
                    //? >=26.1
                    .setId(ResourceKey.create(Registries.ITEM, SSO.id("pinnacle_enchantment")))
            , SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );

    public static final Item WHETSTONE = new WhetstoneItem(
            new Item.Properties()
                    .durability(6)
                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    //? >=26.1 {
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
            //? >=26.1
            .setId(ResourceKey.create(Registries.ITEM, SSO.id("broken_anvil")))
    );

	public static final Item INFO_ENCHANTMENT_UPGRADE = new Item(new Item.Properties()
            //? >=26.1
            .setId(ResourceKey.create(Registries.ITEM, SSO.id("info_enchantment_upgrade")))
    );

	public static final Item INFO_PINNACLE_ENCHANTMENT = new Item(new Item.Properties()
            //? >=26.1
            .setId(ResourceKey.create(Registries.ITEM, SSO.id("info_pinnacle_enchantment")))
    );

    public static void init() {}
}

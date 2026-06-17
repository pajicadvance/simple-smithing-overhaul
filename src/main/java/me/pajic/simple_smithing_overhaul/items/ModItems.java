package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

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
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.enchantment_upgrade.additions_slot_description")
            )),
            List.of(
                    SSO.id("container/slot/enchanted_book"),
                    SSO.id("container/slot/whetstone"),
                    Identifier.withDefaultNamespace("container/slot/helmet"),
                    Identifier.withDefaultNamespace("container/slot/chestplate"),
                    Identifier.withDefaultNamespace("container/slot/leggings"),
                    Identifier.withDefaultNamespace("container/slot/boots"),
                    Identifier.withDefaultNamespace("container/slot/hoe"),
                    Identifier.withDefaultNamespace("container/slot/axe"),
                    Identifier.withDefaultNamespace("container/slot/sword"),
                    Identifier.withDefaultNamespace("container/slot/shovel"),
                    Identifier.withDefaultNamespace("container/slot/pickaxe")
            ),
            List.of(Identifier.parse("container/slot/lapis_lazuli")),
            new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("enchantment_upgrade")
            )),
			ModUtil.enchantmentUpgradingEnabled()
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
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.base_slot_description")
            )),
            Component.translatable(Util.makeDescriptionId(
                    "item",
                    Identifier.parse("simple_smithing_overhaul.smithing_template.pinnacle_enchantment.additions_slot_description")
            )),
            List.of(
                    Identifier.withDefaultNamespace("container/slot/helmet"),
                    Identifier.withDefaultNamespace("container/slot/chestplate"),
                    Identifier.withDefaultNamespace("container/slot/leggings"),
                    Identifier.withDefaultNamespace("container/slot/boots"),
                    Identifier.withDefaultNamespace("container/slot/hoe"),
                    Identifier.withDefaultNamespace("container/slot/axe"),
                    Identifier.withDefaultNamespace("container/slot/sword"),
                    Identifier.withDefaultNamespace("container/slot/shovel"),
                    Identifier.withDefaultNamespace("container/slot/pickaxe")
            ),
            List.of(SSO.id("container/slot/echo_shard")),
            new Item.Properties().rarity(Rarity.EPIC).setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("pinnacle_enchantment")
            )),
            SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()
    );

    public static final Item WHETSTONE = new WhetstoneItem(
            new Item.Properties()
                    .durability(6)
                    .component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                    .repairable(Items.QUARTZ)
                    .enchantable(22)
                    .setId(ResourceKey.create(
                            Registries.ITEM,
                            SSO.id("whetstone")
                    ))
    );

    public static final Item BROKEN_ANVIL = new BlockItem(
            ModBlocks.BROKEN_ANVIL, new Item.Properties()
            .setId(ResourceKey.create(
                    Registries.ITEM,
                    SSO.id("broken_anvil")
            ))
    );

	public static final Item INFO_ENCHANTMENT_UPGRADE = new Item(new Item.Properties().setId(ResourceKey.create(
			Registries.ITEM, SSO.id("info_enchantment_upgrade")
	)));

	public static final Item INFO_PINNACLE_ENCHANTMENT = new Item(new Item.Properties().setId(ResourceKey.create(
			Registries.ITEM, SSO.id("info_pinnacle_enchantment")
	)));

    public static void init() {}
}

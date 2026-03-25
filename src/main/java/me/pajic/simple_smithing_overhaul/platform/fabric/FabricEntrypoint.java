package me.pajic.simple_smithing_overhaul.platform.fabric;

//? fabric {

import me.pajic.simple_smithing_overhaul.SSO;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.DataPatches;
import me.pajic.simple_smithing_overhaul.recipe.ModRecipeSerializers;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		DataPatches.init();
		SSO.onInitialize();
		ModBlocks.init();
		ModItems.init();
		ModDataComponents.init();
		ModCriteria.init();
		ModRecipeSerializers.init();
		initRegistry();
		initCreativeTabs();
		initEvents();
	}

	private void initRegistry() {
		Registry.register(
				BuiltInRegistries.BLOCK,
				SSO.id("broken_anvil"),
				ModBlocks.BROKEN_ANVIL
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("enchantment_upgrade"),
				ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("pinnacle_enchantment"),
				ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("whetstone"),
				ModItems.WHETSTONE
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("broken_anvil"),
				ModItems.BROKEN_ANVIL
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("info_enchantment_upgrade"),
				ModItems.INFO_ENCHANTMENT_UPGRADE
		);
		Registry.register(
				BuiltInRegistries.ITEM,
				SSO.id("info_pinnacle_enchantment"),
				ModItems.INFO_PINNACLE_ENCHANTMENT
		);
		Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				SSO.id("repair_count"),
				ModDataComponents.REPAIR_COUNT
		);
		Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				SSO.id("pinnacle_count"),
				ModDataComponents.PINNACLE_COUNT
		);
		Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				SSO.id("broken"),
				ModDataComponents.BROKEN
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("repair_item"), ModCriteria.REPAIR_ITEM
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("repair_item_whetstone"), ModCriteria.REPAIR_ITEM_WHETSTONE
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("repair_item_whetstone_enchanted"), ModCriteria.REPAIR_ITEM_WHETSTONE_ENCHANTED
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("max_whetstone"), ModCriteria.MAX_WHETSTONE
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("item_repair_count"), ModCriteria.ITEM_REPAIR_COUNT
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("item_repair_count_big"), ModCriteria.ITEM_REPAIR_COUNT_BIG
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("disenchant_item"), ModCriteria.DISENCHANT_ITEM
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("reduce_repair_cost"), ModCriteria.REDUCE_REPAIR_COST
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("apply_enchantment_upgrade"), ModCriteria.APPLY_ENCHANTMENT_UPGRADE
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("apply_pinnacle_enchantment"), ModCriteria.APPLY_PINNACLE_ENCHANTMENT
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("bad_rng"), ModCriteria.BAD_RNG
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("maxed_out"), ModCriteria.MAXED_OUT
		);
		Registry.register(
				BuiltInRegistries.TRIGGER_TYPES,
				SSO.id("anvil_enchant_combine"), ModCriteria.ANVIL_ENCHANT_COMBINE
		);
		Registry.register(
				BuiltInRegistries.RECIPE_SERIALIZER,
				SSO.id("portable_repair"),
				ModRecipeSerializers.PORTABLE_ITEM_REPAIR
		);
	}

	private void initCreativeTabs() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.insertAfter(
				Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
				ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
		));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.insertAfter(
				ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE,
				ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
		));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(contents -> contents.insertAfter(
				Items.NETHERITE_HOE,
				ModItems.WHETSTONE
		));
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(contents -> contents.insertAfter(
				Items.DAMAGED_ANVIL,
				ModItems.BROKEN_ANVIL
		));
	}

	private void initEvents() {
		CommonLifecycleEvents.TAGS_LOADED.register((registryAccess, client) -> ModUtil.updateAdditionalRepairables(registryAccess));
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> ModUtil.canUse(player, hand));
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> ModUtil.canUse(player, hand));
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> ModUtil.canUse(player, hand));
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> ModUtil.canUse(player, hand));
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getMainHandItem();
			return ModUtil.isBroken(stack) ? InteractionResult.FAIL : InteractionResult.PASS;
		});
	}
}
//?}

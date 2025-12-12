package me.pajic.simple_smithing_overhaul.platform.fabric;

//? fabric {

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.ModRecipeSerializers;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//? if 1.21.1 {
/*import net.minecraft.world.InteractionResultHolder;
*///?} else {
import net.minecraft.world.InteractionResult;
//?}

@SuppressWarnings("unused")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		ResourceModifications.init();
		SSO.onInitialize();
		ModBlocks.init();
		ModItems.init();
		ModDataComponents.init();
		ModCriteria.init();
		ModRecipeSerializers.init();
		initRegistry();
		initConditionalCommonResources();
		initCreativeTabs();
		initEvents();
	}

	private void initConditionalCommonResources() {
		FabricLoader.getInstance().getModContainer(SSO.MOD_ID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(
					SSO.id(SSO.PACK_VERSION + "/dp"),
					modContainer,
					ResourcePackActivationType.ALWAYS_ENABLED
			);
			//? if > 1.21.1 {
			switch (SSO.CONFIG.streamlinedRepairs.netheriteRepairMaterial.get()) {
				case DIAMOND -> ResourceManagerHelper.registerBuiltinResourcePack(
						SSO.id(SSO.PACK_VERSION + "/netherite_repair_diamond"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
				case NETHERITE_SCRAP -> ResourceManagerHelper.registerBuiltinResourcePack(
						SSO.id(SSO.PACK_VERSION + "/netherite_repair_scrap"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
			}
			//?}
			if (CompatFlags.CHALK_LOADED) ResourceManagerHelper.registerBuiltinResourcePack(
					SSO.id(SSO.PACK_VERSION + "/chalk_item_tags"),
					modContainer,
					ResourcePackActivationType.ALWAYS_ENABLED
			);
		});
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
				Identifier.withDefaultNamespace("crafting_special_portable_repairitem"),
				ModRecipeSerializers.PORTABLE_ITEM_REPAIR
		);
	}

	private void initCreativeTabs() {
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
				Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
				ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
		));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(contents -> contents.addAfter(
				ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE,
				ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
		));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(contents -> contents.addAfter(
				Items.NETHERITE_HOE,
				ModItems.WHETSTONE
		));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(contents -> contents.addAfter(
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
			//? if <= 1.21.1
			//return ModUtil.isBroken(stack) ? InteractionResultHolder.fail(stack) : InteractionResultHolder.pass(stack);
			//? if > 1.21.1
			return ModUtil.isBroken(stack) ? InteractionResult.FAIL : InteractionResult.PASS;
		});
	}
}
//?}

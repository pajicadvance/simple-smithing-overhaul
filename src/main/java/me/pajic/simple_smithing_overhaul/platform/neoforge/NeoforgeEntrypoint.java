package me.pajic.simple_smithing_overhaul.platform.neoforge;

//? neoforge {
/*
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.ModRecipeSerializers;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(SSO.MOD_ID)
@EventBusSubscriber(modid = SSO.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
		SSO.onInitialize();
	}

	@SubscribeEvent
	private static void initCommonResources(AddPackFindersEvent event) {
		event.addPackFinders(
				SSO.id("resourcepacks/" + SSO.PACK_VERSION + "/dp"),
				PackType.SERVER_DATA,
				Component.literal("Mod " + SSO.PACK_VERSION + " Data Pack"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
		//? if > 1.21.1 {
		switch (SSO.CONFIG.streamlinedRepairs.netheriteRepairMaterial.get()) {
			case DIAMOND -> event.addPackFinders(
					SSO.id("resourcepacks/" + SSO.PACK_VERSION + "/netherite_repair_diamond"),
					PackType.SERVER_DATA,
					Component.literal("Diamond repair material for netherite"),
					PackSource.BUILT_IN,
					true,
					Pack.Position.TOP
			);
			case NETHERITE_SCRAP -> event.addPackFinders(
					SSO.id("resourcepacks/" + SSO.PACK_VERSION + "/netherite_repair_scrap"),
					PackType.SERVER_DATA,
					Component.literal("Netherite scrap repair material for netherite"),
					PackSource.BUILT_IN,
					true,
					Pack.Position.TOP
			);
		}
		//?}
		if (CompatFlags.CHALK_LOADED) event.addPackFinders(
				SSO.id("resourcepacks/" + SSO.PACK_VERSION + "/chalk_item_tags"),
				PackType.SERVER_DATA,
				Component.literal("Chalk item tags"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
	}

	@SubscribeEvent
	private static void initRegistry(RegisterEvent event) {
		ResourceModifications.init();
		ModBlocks.init();
		ModItems.init();
		ModDataComponents.init();
		ModCriteria.init();
		ModRecipeSerializers.init();
		event.register(
				Registries.BLOCK,
				registry -> registry.register(SSO.id("broken_anvil"), ModBlocks.BROKEN_ANVIL)
		);
		event.register(
				Registries.ITEM,
				registry -> {
					registry.register(SSO.id("enchantment_upgrade"), ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE);
					registry.register(SSO.id("pinnacle_enchantment"), ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE);
					registry.register(SSO.id("whetstone"), ModItems.WHETSTONE);
					registry.register(SSO.id("broken_anvil"), ModItems.BROKEN_ANVIL);
				}
		);
		event.register(
				Registries.DATA_COMPONENT_TYPE,
				registry -> {
					registry.register(SSO.id("repair_count"), ModDataComponents.REPAIR_COUNT);
					registry.register(SSO.id("pinnacle_count"), ModDataComponents.PINNACLE_COUNT);
					registry.register(SSO.id("broken"), ModDataComponents.BROKEN);
				}
		);
		event.register(
				Registries.TRIGGER_TYPE,
				registry -> {
					registry.register(SSO.id("repair_item"), ModCriteria.REPAIR_ITEM);
					registry.register(SSO.id("repair_item_whetstone"), ModCriteria.REPAIR_ITEM_WHETSTONE);
					registry.register(SSO.id("repair_item_whetstone_enchanted"), ModCriteria.REPAIR_ITEM_WHETSTONE_ENCHANTED);
					registry.register(SSO.id("max_whetstone"), ModCriteria.MAX_WHETSTONE);
					registry.register(SSO.id("item_repair_count"), ModCriteria.ITEM_REPAIR_COUNT);
					registry.register(SSO.id("item_repair_count_big"), ModCriteria.ITEM_REPAIR_COUNT_BIG);
					registry.register(SSO.id("disenchant_item"), ModCriteria.DISENCHANT_ITEM);
					registry.register(SSO.id("reduce_repair_cost"), ModCriteria.REDUCE_REPAIR_COST);
					registry.register(SSO.id("apply_enchantment_upgrade"), ModCriteria.APPLY_ENCHANTMENT_UPGRADE);
					registry.register(SSO.id("apply_pinnacle_enchantment"), ModCriteria.APPLY_PINNACLE_ENCHANTMENT);
					registry.register(SSO.id("bad_rng"), ModCriteria.BAD_RNG);
					registry.register(SSO.id("maxed_out"), ModCriteria.MAXED_OUT);
					registry.register(SSO.id("anvil_enchant_combine"), ModCriteria.ANVIL_ENCHANT_COMBINE);
				}
		);
		event.register(
				Registries.RECIPE_SERIALIZER,
				registry -> registry.register(
						ResourceLocation.withDefaultNamespace("crafting_special_portable_repairitem"),
						ModRecipeSerializers.PORTABLE_ITEM_REPAIR
				)
		);
	}

	@SubscribeEvent
	private static void initCreativeTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			event.insertAfter(
					Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),
					ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
			);
			event.insertAfter(
					ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),
					ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE.getDefaultInstance(),
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
		else if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			event.insertAfter(
					Items.DAMAGED_ANVIL.getDefaultInstance(),
					ModItems.BROKEN_ANVIL.getDefaultInstance(),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
			);
		}
	}

	@SubscribeEvent
	private static void initUpdateRepairablesEvent(TagsUpdatedEvent event) {
		ModUtil.updateAdditionalRepairables(event./^? if 1.21.1 {^//^getRegistryAccess()^//^?} else {^/getLookupProvider()/^?}^/);
	}

	@SubscribeEvent
	private static void handleBrokenItemAttack(AttackEntityEvent event) {
		if (ModUtil.canUse(event.getEntity(), event.getEntity().getUsedItemHand()) == InteractionResult.FAIL) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	private static void handleBrokenItemUseEntity(PlayerInteractEvent.EntityInteract event) {
		if (ModUtil.canUse(event.getEntity(), event.getEntity().getUsedItemHand()) == InteractionResult.FAIL) {
			event.setCancellationResult(InteractionResult.FAIL);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	private static void handleBrokenItemAttackBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (ModUtil.canUse(event.getEntity(), event.getEntity().getUsedItemHand()) == InteractionResult.FAIL) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	private static void handleBrokenItemUseBlock(PlayerInteractEvent.RightClickBlock event) {
		if (ModUtil.canUse(event.getEntity(), event.getEntity().getUsedItemHand()) == InteractionResult.FAIL) {
			event.setCancellationResult(InteractionResult.FAIL);
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	private static void handleBrokenItemUse(PlayerInteractEvent.RightClickItem event) {
		if (ModUtil.canUse(event.getEntity(), event.getEntity().getUsedItemHand()) == InteractionResult.FAIL) {
			event.setCancellationResult(InteractionResult.FAIL);
			event.setCanceled(true);
		}
	}
}
*///?}

package me.pajic.simple_smithing_overhaul;

import com.mojang.serialization.Codec;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.WhetstoneRepairItemRecipe;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.AnvilRepairEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
//? if <= 1.21.1
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
//? if > 1.21.1 {
/*import me.pajic.simple_smithing_overhaul.datapacks.NetheriteRepairMaterial;
import net.minecraft.world.item.crafting.CustomRecipe;
*///?}

@Mod("simple_smithing_overhaul")
public class Main {

    public static final RecipeSerializer<WhetstoneRepairItemRecipe> WHETSTONE_REPAIR_ITEM =
            //? if <= 1.21.1
            new SimpleCraftingRecipeSerializer<>(WhetstoneRepairItemRecipe::new);
            //? if > 1.21.1
            /*new CustomRecipe.Serializer<>(WhetstoneRepairItemRecipe::new);*/
    public static final DataComponentType<Integer> REPAIR_COUNT = DataComponentType.<Integer>builder().persistent(Codec.INT).build();
    public static final DataComponentType<Integer> PINNACLE_COUNT = DataComponentType.<Integer>builder().persistent(Codec.INT).build();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SERVER_SPEC);
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::addCreative);
        //? if > 1.21.1
        /*modEventBus.addListener(NetheriteRepairMaterial::registerDatapacks);*/
        NeoForge.EVENT_BUS.addListener(this::onAnvilUse);
        modEventBus.addListener(this::onInitialize);
    }

    private void registerData(RegisterEvent event) {
        event.register(Registries.ITEM, registry -> {
            registry.register(ResourceLocation.parse(
                            "simple_smithing_overhaul:enchantment_upgrade"),
                    ModItems.ENCHANTMENT_UPGRADE_SMITHING_TEMPLATE
            );
            registry.register(ResourceLocation.parse(
                            "simple_smithing_overhaul:pinnacle_enchantment"),
                    ModItems.PINNACLE_ENCHANTMENT_SMITHING_TEMPLATE
            );
            registry.register(
                    ResourceLocation.parse("simple_smithing_overhaul:whetstone"),
                    ModItems.WHETSTONE
            );
        });
        event.register(Registries.RECIPE_SERIALIZER, helper -> helper.register(
                ResourceLocation.withDefaultNamespace("crafting_special_whetstone_repairitem"),
                WHETSTONE_REPAIR_ITEM
        ));
        event.register(Registries.DATA_COMPONENT_TYPE, helper -> {
            helper.register(
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "repair_count"),
                    REPAIR_COUNT
            );
            helper.register(
                    ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "pinnacle_count"),
                    PINNACLE_COUNT
            );
        });
        event.register(Registries.TRIGGER_TYPE, helper -> {
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:repair_item"),
                    ModCriteria.REPAIR_ITEM
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:repair_item_whetstone"),
                    ModCriteria.REPAIR_ITEM_WHETSTONE
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:repair_item_whetstone_enchanted"),
                    ModCriteria.REPAIR_ITEM_WHETSTONE_ENCHANTED
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:max_whetstone"),
                    ModCriteria.MAX_WHETSTONE
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:item_repair_count"),
                    ModCriteria.ITEM_REPAIR_COUNT
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:item_repair_count_big"),
                    ModCriteria.ITEM_REPAIR_COUNT_BIG
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:disenchant_item"),
                    ModCriteria.DISENCHANT_ITEM
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:reduce_repair_cost"),
                    ModCriteria.REDUCE_REPAIR_COST
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:apply_enchantment_upgrade"),
                    ModCriteria.APPLY_ENCHANTMENT_UPGRADE
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:apply_pinnacle_enchantment"),
                    ModCriteria.APPLY_PINNACLE_ENCHANTMENT
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:bad_rng"),
                    ModCriteria.BAD_RNG
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:maxed_out"),
                    ModCriteria.MAXED_OUT
            );
            helper.register(
                    ResourceLocation.parse("simple_smithing_overhaul:anvil_enchant_combine"),
                    ModCriteria.ANVIL_ENCHANT_COMBINE
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
    }

    private void onAnvilUse(AnvilRepairEvent event) {
        event.setBreakChance((float) ModServerConfig.degradationChance / 100);
    }

    public void onInitialize(FMLCommonSetupEvent event) {
        ResourceModifications.init();
        ModUtil.initAdditionalRepairables();
    }
}

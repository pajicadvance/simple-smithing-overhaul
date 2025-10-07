package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.datapacks.ChalkItemTags;
import me.pajic.simple_smithing_overhaul.event.ModEvents;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.PortableItemRepairRecipe;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
//? if > 1.21.1 {
/*import net.minecraft.world.item.enchantment.Repairable;
import me.pajic.simple_smithing_overhaul.datapacks.NetheriteRepairMaterial;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
*///?} else {
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.client.renderer.item.ItemProperties;
//?}

public class Initializer {
    // Static initializers
    private static final Logger LOGGER = LoggerFactory.getLogger("Simple Smithing Overhaul");
    public static RecipeSerializer<PortableItemRepairRecipe> PORTABLE_ITEM_REPAIR = RecipeSerializer.register(
            "crafting_special_portable_repairitem",
            //? if <= 1.21.1
            new SimpleCraftingRecipeSerializer<>(PortableItemRepairRecipe::new)
            //? if > 1.21.1
            /*new CustomRecipe.Serializer<>(PortableItemRepairRecipe::new)*/
    );

    // Main initializer method
    public static void init() {
        // Data components
        ModDataComponents.init();
        // Events
        ModEvents.init();
        // Blocks (broken anvil)
        ModBlocks.init();
        // Items
        ModItems.init();
        // Loot and language patches
        ResourceModifications.init();
        // Datapacks overriding the netherite repair material
        // On 1.21.1 this is handled by ArmorMaterialsMixin and TiersMixin
        //? if > 1.21.1
        /*NetheriteRepairMaterial.init();*/
        // Achievement criteria
        ModCriteria.init();
        // Item tags for Chalk mod so that I don't have to put 64 entries inside the config
        if (CompatFlags.CHALK_LOADED) ChalkItemTags.init();
        // Item properties (whetstone stages)
        initItemProperties();
        // Repair recipes
        CommonLifecycleEvents.TAGS_LOADED.register((registryAccess, client) -> updateAdditionalRepairables(registryAccess));
    }

    //? if > 1.21.1
    /*@SuppressWarnings("deprecation")*/
    private static void updateAdditionalRepairables(RegistryAccess registryAccess) {
        // Custom repairs 101
        // 2 maps, item to ingredient and ingredient to ingredient, populated in the initializer
        // In 1.21.1, ModUtil.hasAdditionalRepairables contains logic to determine if an item has a custom repair based on the two maps
        // This method is injected via mixin to vanilla methods which determine if an item has a repair recipe
        // In 1.21.1+, the components of each item are patched to add a repairable component containing the repair materials,
        // so ModUtil.hasAdditionalRepairables isn't required and is not injected anywhere
        ModUtil.additionalRepairables.clear();
        Registry<Item> registry = registryAccess./*? if <= 1.21.1 {*/registryOrThrow/*?} else {*//*lookupOrThrow*//*?}*/(Registries.ITEM);

        // Vanilla repairs
        ModUtil.additionalRepairables.put(Ingredient.of(Items.BOW), Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.CROSSBOW), Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.FISHING_ROD), Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.IRON_INGOT));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.SHEARS), Ingredient.of(Items.IRON_INGOT));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.BRUSH), Ingredient.of(Items.FEATHER));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.CARROT_ON_A_STICK), Ingredient.of(Items.CARROT));
        ModUtil.additionalRepairables.put(Ingredient.of(Items.WARPED_FUNGUS_ON_A_STICK), Ingredient.of(Items.WARPED_FUNGUS));
        if (!CompatFlags.BETTER_TRIDENTS_LOADED) ModUtil.additionalRepairables.put(Ingredient.of(Items.TRIDENT), Ingredient.of(Items.PRISMARINE_SHARD));
        // Modded repairs
        Main.CONFIG.streamlinedRepairs.modRepairableItems.forEach((repairItem, repairMaterial) -> {
            try {
                if (repairItem.startsWith("#")) {
                    if (repairMaterial.startsWith("#")) {
                        ModUtil.additionalRepairables.put(
                                ingredientFromItemTag(repairItem, registry),
                                ingredientFromItemTag(repairMaterial, registry)
                        );
                    } else {
                        registry.getOptional(ResourceLocation.tryParse(repairMaterial)).ifPresent(value ->
                                ModUtil.additionalRepairables.put(
                                        ingredientFromItemTag(repairItem, registry),
                                        Ingredient.of(value)
                                )
                        );
                    }
                } else {
                    Optional<Item> item = registry.getOptional(ResourceLocation.parse(repairItem));
                    if (item.isPresent()) {
                        if (repairMaterial.startsWith("#")) {
                            ModUtil.additionalRepairables.put(
                                    Ingredient.of(item.get()),
                                    ingredientFromItemTag(repairMaterial, registry)
                            );
                        } else {
                            registry.getOptional(ResourceLocation.parse(repairMaterial)).ifPresent(value ->
                                    ModUtil.additionalRepairables.put(Ingredient.of(item.get()), Ingredient.of(value))
                            );
                        }
                    }
                }
            // Catch anything that explodes above because I cannot be bothered
            } catch (Throwable t) {
                LOGGER.warn("Unable to load additional repair {} with {}, skipping: {}", repairItem, repairMaterial, t.getMessage());
            }
        });
        // Patch item components to add the repairable component
        //? if > 1.21.1 {
        /*ModUtil.additionalRepairables.forEach((itemIngredient, materialIngredient) ->
                itemIngredient.items().forEach(itemHolder ->
                        itemHolder.value().components = PatchedDataComponentMap.fromPatch(
                                itemHolder.value().components,
                                DataComponentPatch.builder().set(
                                        DataComponents.REPAIRABLE,
                                        new Repairable(HolderSet.direct(materialIngredient.items().toList()))
                                ).build()
                        )
                )
        );
        *///?}
    }

    private static void initItemProperties() {
        //? if <= 1.21.1 {
        ItemProperties.register(
                ModItems.WHETSTONE,
                Main.withModNamespace("damage_state"),
                (stack, level, entity, i) -> (float) stack.getDamageValue() / stack.getMaxDamage()
        );
        //?}
    }

    private static Ingredient ingredientFromItemTag(String s, Registry<Item> registry) {
        return Ingredient.of(/*? if > 1.21.1 {*//*registry.get(*//*?}*/TagKey.create(registry.key(), ResourceLocation.tryParse(s.substring(1)))/*? if > 1.21.1 {*//*).orElseThrow()*//*?}*/);
    }
}

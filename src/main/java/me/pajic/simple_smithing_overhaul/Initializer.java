package me.pajic.simple_smithing_overhaul;

import com.mojang.serialization.Codec;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.datapacks.ChalkItemTags;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.WhetstoneRepairItemRecipe;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.core.component.DataComponentType;
import java.util.Optional;
//? if > 1.21.1 {
/*import net.minecraft.world.item.enchantment.Repairable;
import me.pajic.simple_smithing_overhaul.datapacks.NetheriteRepairMaterial;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
*///?}
//? if 1.21.1
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class Initializer {
    // Static initializers
    public static final DataComponentType<Integer> REPAIR_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "repair_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static final DataComponentType<Integer> PINNACLE_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "pinnacle_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    private static final Logger LOGGER = LoggerFactory.getLogger("Simple Smithing Overhaul");
    public static RecipeSerializer<WhetstoneRepairItemRecipe> WHETSTONE_REPAIR_ITEM = RecipeSerializer.register(
            "crafting_special_whetstone_repairitem",
            //? if <= 1.21.1
            new SimpleCraftingRecipeSerializer<>(WhetstoneRepairItemRecipe::new)
            //? if > 1.21.1
            /*new CustomRecipe.Serializer<>(WhetstoneRepairItemRecipe::new)*/
    );

    // Main initializer method
    public static void init() {
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
        // Repair recipes
        initAdditionalRepairables();
        // Item tags for Chalk mod so that I don't have to put 64 entries inside the config
        if (CompatFlags.CHALK_LOADED) ChalkItemTags.init();
        // Populate enchantment suggestions used by the config
        // Runs once for each registered enchantment
        DynamicRegistrySetupCallback.EVENT.register(registryView -> registryView.registerEntryAdded(
                Registries.ENCHANTMENT,
                (rawId, id, object) -> ModUtil.enchantmentSuggestions.add(id)
        ));
        // Populate item and item tag suggestions used by the config
        // Runs after every world change, clears the list and renews it
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((server, world) -> {
            ModUtil.itemSuggestions.clear();
            ModUtil.itemSuggestions.addAll(BuiltInRegistries.ITEM.keySet().stream().map(ResourceLocation::toString).toList());
            BuiltInRegistries.ITEM./*? if > 1.21.1 {*//*listTagIds()*//*?}*//*? if 1.21.1 {*/getTagNames()/*?}*/.forEach(tag -> ModUtil.itemSuggestions.add("#" + tag.location()));
        });
    }

    @SuppressWarnings("deprecation")
    private static void initAdditionalRepairables() {
        // Custom repairs 101
        // 2 maps, item to ingredient and ingredient to ingredient, populated in the initializer
        // In 1.21.1, ModUtil.hasAdditionalRepairables contains logic to determine if an item has a custom repair based on the two maps
        // This method is injected via mixin to vanilla methods which determine if an item has a repair recipe
        // In 1.21.1+, the components of each item are patched to add a repairable component containing the repair materials,
        // so ModUtil.hasAdditionalRepairables isn't required and is not injected anywhere

        // Vanilla repairs
        ModUtil.additionalRepairables.put(Items.BOW, Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Items.CROSSBOW, Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Items.FISHING_ROD, Ingredient.of(Items.STRING));
        ModUtil.additionalRepairables.put(Items.FLINT_AND_STEEL, Ingredient.of(Items.IRON_INGOT));
        ModUtil.additionalRepairables.put(Items.SHEARS, Ingredient.of(Items.IRON_INGOT));
        ModUtil.additionalRepairables.put(Items.BRUSH, Ingredient.of(Items.FEATHER));
        ModUtil.additionalRepairables.put(Items.CARROT_ON_A_STICK, Ingredient.of(Items.CARROT));
        ModUtil.additionalRepairables.put(Items.WARPED_FUNGUS_ON_A_STICK, Ingredient.of(Items.WARPED_FUNGUS));
        if (!CompatFlags.BETTER_TRIDENTS_LOADED) ModUtil.additionalRepairables.put(Items.TRIDENT, Ingredient.of(Items.PRISMARINE_SHARD));
        // Modded repairs
        Main.CONFIG.streamlinedRepairs.modRepairableItems.forEach((repairItem, repairMaterial) -> {
            try {
                if (repairItem.startsWith("#")) {
                    if (repairMaterial.startsWith("#")) {
                        //? if 1.21.1 {
                        ModUtil.additionalTagRepairables.put(
                                Ingredient.of(TagKey.create(
                                        Registries.ITEM,
                                        ResourceLocation.parse(repairItem.replace("#", ""))
                                )),
                                Ingredient.of(TagKey.create(
                                        Registries.ITEM,
                                        ResourceLocation.parse(repairMaterial.replace("#", ""))
                                ))
                        );
                        //?}
                        //? if > 1.21.1 {
                        /*ModUtil.additionalTagRepairables.put(
                                Ingredient.of(BuiltInRegistries.ITEM.get(TagKey.create(
                                        Registries.ITEM,
                                        ResourceLocation.parse(repairItem.replace("#", "")))).orElseThrow()
                                ),
                                Ingredient.of(BuiltInRegistries.ITEM.get(TagKey.create(
                                        Registries.ITEM,
                                        ResourceLocation.parse(repairMaterial.replace("#", "")))).orElseThrow()
                                )
                        );
                        *///?}
                    } else {
                        BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(repairMaterial)).ifPresent(value ->
                            //? if 1.21.1 {
                            ModUtil.additionalTagRepairables.put(
                                    Ingredient.of(TagKey.create(
                                            Registries.ITEM,
                                            ResourceLocation.parse(repairItem.replace("#", ""))
                                    )),
                                    Ingredient.of(value)
                            )
                            //?}
                            //? if > 1.21.1 {
                            /*ModUtil.additionalTagRepairables.put(
                                    Ingredient.of(BuiltInRegistries.ITEM.get(TagKey.create(
                                            Registries.ITEM,
                                            ResourceLocation.parse(repairItem.replace("#", "")))).orElseThrow()
                                    ),
                                    Ingredient.of(value)
                            )
                            *///?}
                        );
                    }
                } else {
                    Optional<Item> item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(repairItem));
                    if (item.isPresent()) {
                        if (repairMaterial.startsWith("#")) {
                            //? if 1.21.1 {
                            ModUtil.additionalRepairables.put(
                                    item.get(),
                                    Ingredient.of(TagKey.create(
                                            Registries.ITEM,
                                            ResourceLocation.parse(repairMaterial.replace("#", ""))
                                    ))
                            );
                            //?}
                            //? if > 1.21.1 {
                            /*ModUtil.additionalRepairables.put(
                                    item.get(),
                                    Ingredient.of(BuiltInRegistries.ITEM.get(TagKey.create(
                                            Registries.ITEM,
                                            ResourceLocation.parse(repairMaterial.replace("#", "")))).orElseThrow()
                                    )
                            );
                            *///?}
                        } else {
                            BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(repairMaterial)).ifPresent(value ->
                                    ModUtil.additionalRepairables.put(item.get(), Ingredient.of(value))
                            );
                        }
                    }
                }
            // Minecraft doesn't like creating tag keys from resource locations which don't exist so handle that
            } catch (IllegalStateException e) {
                LOGGER.warn("Unable to load additional repair, skipping: {}", e.getMessage());
            }
        });
        // Patch item components to add the repairable component
        //? if > 1.21.1 {
        /*BuiltInRegistries.ITEM.entrySet().forEach(entry -> {
            List<Holder<Item>> items = new ArrayList<>();
            for (Map.Entry<Ingredient, Ingredient> repair : ModUtil.additionalTagRepairables.entrySet()) {
                if (repair.getKey().items().anyMatch(itemHolder -> itemHolder.is(entry.getKey()))) {
                    items.addAll(repair.getValue().items().toList());
                    break;
                }
            }
            for (Map.Entry<Item, Ingredient> repair : ModUtil.additionalRepairables.entrySet()) {
                if (repair.getKey().equals(entry.getValue())) {
                    items.addAll(repair.getValue().items().toList());
                    break;
                }
            }
            if (!items.isEmpty()) {
                entry.getValue().components = PatchedDataComponentMap.fromPatch(entry.getValue().components, DataComponentPatch.builder().set(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(items))).build());
            }
        });
        *///?}
    }
}

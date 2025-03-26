package me.pajic.simple_smithing_overhaul;

import com.mojang.serialization.Codec;
import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import me.pajic.simple_smithing_overhaul.recipe.WhetstoneRepairItemRecipe;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
//? if <= 1.21.1
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
//? if > 1.21.1 {
/*import me.pajic.simple_smithing_overhaul.datapacks.NetheriteRepairMaterial;
import net.minecraft.world.item.crafting.CustomRecipe;
*///?}

public class Main implements ModInitializer {

    public static ModConfig CONFIG = ModConfig.createAndLoad();
    public static RecipeSerializer<WhetstoneRepairItemRecipe> WHETSTONE_REPAIR_ITEM = RecipeSerializer.register(
            "crafting_special_whetstone_repairitem",
            //? if <= 1.21.1
            new SimpleCraftingRecipeSerializer<>(WhetstoneRepairItemRecipe::new)
            //? if > 1.21.1
            /*new CustomRecipe.Serializer<>(WhetstoneRepairItemRecipe::new)*/
    );
    public static final DataComponentType<Integer> REPAIR_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "repair_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static final DataComponentType<Integer> PINNACLE_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath("simple_smithing_overhaul", "pinnacle_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    @Override
    public void onInitialize() {
        ModItems.init();
        ModUtil.initAdditionalRepairables();
        ResourceModifications.init();
        //? if > 1.21.1
        /*NetheriteRepairMaterial.init();*/
        ModCriteria.init();
    }
}

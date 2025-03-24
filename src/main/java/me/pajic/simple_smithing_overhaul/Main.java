package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.loot.ModLootEvents;
import me.pajic.simple_smithing_overhaul.recipe.WhetstoneRepairItemRecipe;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;
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

    @Override
    public void onInitialize() {
        ModItems.init();
        ModUtil.initAdditionalRepairables();
        ModLootEvents.init();
        //? if > 1.21.1
        /*NetheriteRepairMaterial.init();*/
    }
}

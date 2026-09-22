package me.pajic.simple_smithing_overhaul.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;

//? <26.1
//import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipeSerializers {

	public static RecipeSerializer<PortableItemRepairRecipe> PORTABLE_ITEM_REPAIR =
            //? <26.1 {
            /*new SimpleCraftingRecipeSerializer<>(c -> new PortableItemRepairRecipe());
            *///?} else {
            new RecipeSerializer<>(PortableItemRepairRecipe.MAP_CODEC, PortableItemRepairRecipe.STREAM_CODEC);
            //?}

	public static void init() {}
}

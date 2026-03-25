package me.pajic.simple_smithing_overhaul.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {

	public static RecipeSerializer<PortableItemRepairRecipe> PORTABLE_ITEM_REPAIR = new RecipeSerializer<>(PortableItemRepairRecipe.MAP_CODEC, PortableItemRepairRecipe.STREAM_CODEC);

	public static void init() {}
}

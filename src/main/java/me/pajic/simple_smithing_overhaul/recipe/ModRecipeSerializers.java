package me.pajic.simple_smithing_overhaul.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
//? if 1.21.1 {
/*import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
*///?} else {
import net.minecraft.world.item.crafting.CustomRecipe;
//?}

public class ModRecipeSerializers {
	public static RecipeSerializer<PortableItemRepairRecipe> PORTABLE_ITEM_REPAIR =
			//? if <= 1.21.1
			//new SimpleCraftingRecipeSerializer<>(PortableItemRepairRecipe::new)
			//? if > 1.21.1
			new CustomRecipe.Serializer<>(PortableItemRepairRecipe::new)
	;

	public static void init() {}
}

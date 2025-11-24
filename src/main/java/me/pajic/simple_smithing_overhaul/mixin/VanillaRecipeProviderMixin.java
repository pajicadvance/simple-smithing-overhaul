package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.recipe.PortableItemRepairRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if > 1.21.1 {
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeProvider;
//?}

@Mixin(VanillaRecipeProvider.class)
public abstract class VanillaRecipeProviderMixin /*? if > 1.21.1 {*/extends RecipeProvider/*?}*/{

    @Inject(
            method = "buildRecipes",
            at = @At("TAIL")
    )
    //? if <= 1.21.1 {
    /*private void buildWhetstoneRepairItemRecipe(RecipeOutput recipeOutput, CallbackInfo ci) {
        SpecialRecipeBuilder.special(PortableItemRepairRecipe::new).save(recipeOutput, "whetstone_repair_item");
    }
    *///?} else {
    private void buildWhetstoneRepairItemRecipe(CallbackInfo ci) {
        SpecialRecipeBuilder.special(PortableItemRepairRecipe::new).save(output, "whetstone_repair_item");
    }

    protected VanillaRecipeProviderMixin(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }
    //?}
}

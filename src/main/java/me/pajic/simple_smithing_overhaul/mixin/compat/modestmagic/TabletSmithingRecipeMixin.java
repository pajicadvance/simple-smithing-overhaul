package me.pajic.simple_smithing_overhaul.mixin.compat.modestmagic;

import com.baisylia.modestmagic.recipe.custom.TabletSmithingRecipe;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("modestmagic")
@Mixin(TabletSmithingRecipe.class)
public class TabletSmithingRecipeMixin {

    //~ if >=26.1 'Lnet/minecraft/world/item/Item;' -> 'Ljava/lang/Object;'
    @Definition(id = "is", method = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z")
    @Definition(id = "ENCHANTED_BOOK", field = "Lnet/minecraft/world/item/Items;ENCHANTED_BOOK:Lnet/minecraft/world/item/Item;")
    @Expression("?.is(ENCHANTED_BOOK)")
    @ModifyExpressionValue(
            //~ if >=26.1 'Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;' -> ';)Lnet/minecraft/world/item/ItemStack;'
            method = "assemble(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean allowWhetstoneEnchantingAssemble(boolean original, @Local ItemStack stack) {
        return check(stack, original);
    }

    @Definition(id = "test", method = "Lnet/minecraft/world/item/crafting/Ingredient;test(Lnet/minecraft/world/item/ItemStack;)Z")
    @Definition(id = "baseStack", local = @Local(type = ItemStack.class, ordinal = 1))
    @Expression("?.?.test(baseStack)")
    @ModifyExpressionValue(
            method = "matches(Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean allowWhetstoneEnchantingMatches(boolean original, @Local(ordinal = 1) ItemStack stack) {
        return check(stack, original);
    }

    //? <26.1 {
    /*@ModifyReturnValue(
            method = "isBaseIngredient",
            at = @At("RETURN")
    )
    private boolean whetstoneIsBase(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return check(stack, original);
    }
    *///?}

    @Unique private boolean check(ItemStack stack, boolean original) {
        if (SSO.CONFIG.portableItemRepair.enableWhetstone.get()) {
            return original || stack.is(ModItems.WHETSTONE);
        }
        return original;
    }
}

package me.pajic.simple_smithing_overhaul.mixin.compat.emi;

/*
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import dev.emi.emi.recipe.EmiAnvilRecipe;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("emi")
@Mixin(value = EmiAnvilRecipe.class, remap = false)
public class EmiAnvilRecipeMixin {

    // Divide stack max damage by the new unit costs instead of 4 in anvil repair recipes.
    // Makes anvil repair recipes in EMI consistent with changes made by the mod.
    @ModifyExpressionValue(
            method = "getTool",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=4"
            )
    )
    private int modifyUnitCost(int original, @Local ItemStack stack) {
        return ModUtil.determineUnitCost(stack);
    }
}*/

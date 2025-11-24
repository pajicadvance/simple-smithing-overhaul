package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.recipe.UpgradeRecipeHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Shadow @Final public NonNullList<Slot> slots;

    @ModifyArg(
			//? fabric
            method = "method_34249",
			//? neoforge && 1.21.1
			/*method = "lambda$doClick$3",*/
			//? neoforge && > 1.21.1
			/*method = "lambda$doClick$4",*/
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private ItemStack upgradeItemAfterTake(final ItemStack original, @Local(argsOnly = true) Slot slot) {
        return UpgradeRecipeHandler.applyPinnacleUpgrade(original, slot, (AbstractContainerMenu) (Object) this, slots);
    }
}

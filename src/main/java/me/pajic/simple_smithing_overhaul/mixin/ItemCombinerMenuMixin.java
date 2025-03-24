package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//? if > 1.21.1 {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import net.minecraft.world.entity.player.Player;
*///?}

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin extends AbstractContainerMenu {

    protected ItemCombinerMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @ModifyArg(
            method = "quickMoveStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/ItemCombinerMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z",
                    ordinal = 0
            ),
            index = 0
    )
    private ItemStack upgradeItemAfterQuickMove(ItemStack original, @Local Slot slot) {
        return ModUtil.applyPinnacleUpgrade(original, slot, (ItemCombinerMenu) (Object) this, slots);
    }

    //? if > 1.21.1 {
    /*@WrapMethod(method = "mayPickup")
    private boolean modifyMayPickup(Player player, boolean hasStack, Operation<Boolean> original) {
        if (
                ModCommonConfig.enableEnchantmentUpgrading &&
                ModServerConfig.upgradingHasExperienceCost &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= ModUtil.cost) && ModUtil.cost > 0;
        }
        return hasStack;
    }
    *///?}
}

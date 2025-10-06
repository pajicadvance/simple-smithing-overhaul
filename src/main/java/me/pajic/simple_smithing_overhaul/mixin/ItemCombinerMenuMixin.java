package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.recipe.UpgradeRecipeHandler;
import me.pajic.simple_smithing_overhaul.util.CostAccess;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//? if > 1.21.1 {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.entity.player.Player;
*///?}

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin extends AbstractContainerMenu implements CostAccess {

    protected ItemCombinerMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Unique private int cost = 0;

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
        return UpgradeRecipeHandler.applyPinnacleUpgrade(original, slot, (ItemCombinerMenu) (Object) this, slots);
    }

    //? if > 1.21.1 {
    /*@WrapMethod(method = "mayPickup")
    private boolean modifyMayPickup(Player player, boolean hasStack, Operation<Boolean> original) {
        if (
                Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get() &&
                Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= cost) && cost > 0;
        }
        if (
                Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= cost) && cost > 0;
        }
        return original.call(player, hasStack);
    }
    *///?}

    @Override
    public int sso$getCost() {
        return cost;
    }

    @Override
    public void sso$setCost(int cost) {
        this.cost = cost;
    }
}

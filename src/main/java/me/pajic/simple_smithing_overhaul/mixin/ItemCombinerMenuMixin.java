package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.recipe.UpgradeRecipeHandler;
import me.pajic.simple_smithing_overhaul.util.CostAccess;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin extends AbstractContainerMenu implements CostAccess {

    protected ItemCombinerMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Unique private int sso$cost = 0;

    @ModifyArg(
            method = "quickMoveStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/ItemCombinerMenu;moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z",
                    ordinal = 0
            ),
            index = 0
    )
    private ItemStack upgradeItemAfterQuickMove(ItemStack original, @Local(name = "slot") Slot slot) {
        return UpgradeRecipeHandler.applyPinnacleUpgrade(original, slot, (ItemCombinerMenu) (Object) this, slots);
    }

    @WrapMethod(method = "mayPickup")
    private boolean modifyMayPickup(Player player, boolean hasItem, Operation<Boolean> original) {
        if (
				ModUtil.enchantmentUpgradingEnabled() &&
                SSO.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= sso$cost) && sso$cost > 0;
        }
        if (
                SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= sso$cost) && sso$cost > 0;
        }
        return original.call(player, hasItem);
    }

    @Override
    public int sso$getCost() {
        return sso$cost;
    }

    @Override
    public void sso$setCost(int cost) {
        this.sso$cost = cost;
    }
}

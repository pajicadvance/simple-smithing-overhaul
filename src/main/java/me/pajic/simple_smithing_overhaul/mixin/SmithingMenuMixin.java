package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.recipe.UpgradeRecipeHandler;
import me.pajic.simple_smithing_overhaul.util.CostAccess;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <= 1.21.1
/*import com.llamalad7.mixinextras.injector.ModifyReturnValue;*/

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {

    @Shadow @Final public Level level;

    //? if <= 1.21.1 {
    /*public SmithingMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }
    *///?} else {
    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }
    //?}

    @Inject(
            //? if <= 1.21.1 {
            /*method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;enabledFeatures()Lnet/minecraft/world/flag/FeatureFlagSet;"
            ),
            *///?} else {
			//? if fabric
            method = "method_64653",
			//? if neoforge
			/*method = "lambda$createResult$1",*/
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/SmithingMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;",
                    ordinal = 0
            ),
            //?}
            cancellable = true
    )
    private void handleSmithingUpgradeRecipes(CallbackInfo ci, @Local LocalRef<ItemStack> stack) {
        UpgradeRecipeHandler.handleRecipe(ci, stack, slots, resultSlots, level, this);
    }

    //? if <= 1.21.1 {
    /*@ModifyReturnValue(
            method = "mayPickup",
            at = @At("RETURN")
    )
    private boolean modifyMayPickup(boolean original) {
        if (
                SSO.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get() &&
				SSO.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= ((CostAccess) this).sso$getCost()) && ((CostAccess) this).sso$getCost() > 0;
        }
        if (
				SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= ((CostAccess) this).sso$getCost()) && ((CostAccess) this).sso$getCost() > 0;
        }
        return original;
    }
    *///?}

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void hookOnTake(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (
				SSO.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get() &&
				SSO.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            if (!player.getAbilities().instabuild) ModUtil.payXpCost(player, ((CostAccess) this).sso$getCost());
            if (player instanceof ServerPlayer p) ModCriteria.APPLY_ENCHANTMENT_UPGRADE.trigger(p);
        }
        if (
				SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            if (!player.getAbilities().instabuild) ModUtil.payXpCost(player, ((CostAccess) this).sso$getCost());
            if (player instanceof ServerPlayer p) {
                ModCriteria.APPLY_PINNACLE_ENCHANTMENT.trigger(p);
                if (itemStack.getOrDefault(ModDataComponents.PINNACLE_COUNT, 0) == 10) ModCriteria.BAD_RNG.trigger(p);
            }
        }
    }
}

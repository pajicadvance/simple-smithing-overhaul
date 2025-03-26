package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
public abstract class GrindstoneMenuOutputSlotMixin {

    @Shadow @Final GrindstoneMenu field_16780;

    @ModifyExpressionValue(
            method = "getExperienceFromItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMinCost(I)I"
            )
    )
    private int modifyXpCalculation(int original, @Local Object2IntMap.Entry<Holder<Enchantment>> entry) {
        if (Main.CONFIG.grindstoneImprovements.increasedDisenchantXpGain()) {
            return entry.getKey().value().getMaxCost(entry.getIntValue());
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getExperienceAmount",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            )
    )
    private int modifyXpAmount(int original, @Local(ordinal = 0) int i) {
        if (Main.CONFIG.grindstoneImprovements.increasedDisenchantXpGain()) {
            return i;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "getExperienceAmount",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/GrindstoneMenu;repairSlots:Lnet/minecraft/world/Container;",
                    ordinal = 1
            )
    )
    private Container preventXpAwardIfRepairCostReductionRecipe(Container original, @Cancellable CallbackInfoReturnable<Integer> ci) {
        if (Main.CONFIG.grindstoneImprovements.repairCostReductionRecipe() && original.getItem(1).is(Items.NETHERITE_SCRAP)) {
            ci.setReturnValue(0);
        }
        return original;
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void grantAdvancement(Player player, ItemStack stack, CallbackInfo ci) {
        if (player instanceof ServerPlayer p) {
            if (field_16780.slots.get(1).getItem().is(Items.NETHERITE_SCRAP)) ModCriteria.REDUCE_REPAIR_COST.trigger(p);
            else ModCriteria.DISENCHANT_ITEM.trigger(p);
        }
    }
}

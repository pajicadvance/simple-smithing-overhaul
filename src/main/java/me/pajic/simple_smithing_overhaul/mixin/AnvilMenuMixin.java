package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilMenu.class, priority = 2000)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    //? if <= 1.21.1 {
    public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }
    //?}

    //? if > 1.21.1 {
    /*public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }
    *///?}

    @Shadow public int repairItemCountCost;
    @Shadow @Final private DataSlot cost;

    @ModifyArg(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(II)I"),
            index = 1
    )
    private int modifyRepairUnitCost(int original, @Local(ordinal = 1) ItemStack itemStack) {
        return itemStack.getMaxDamage() / ModUtil.determineUnitCost(itemStack);
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void noXPCostIfUnenchanted(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (
                ModServerConfig.freeUnenchantedRepairs &&
                !itemStack.isEnchanted() &&
                itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()
        ) {
            cost.set(0);
        }
    }

    @ModifyArg(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(JJJ)J"
            ),
            index = 0
    )
    private long modifyXPCost(long value, @Local long l, @Local(ordinal = 1) int j) {
        // deduct cost of rename from total cost if option is enabled
        if (ModServerConfig.freeRenames) {
            value -= j;
        }
        // if cost ends up consisting of just prior work cost, ignore it
        if (value == l) {
            value = 0;
        }
        // if there is an "actual" cost, deduct prior work cost from total cost if option is enabled
        if (value != 0 && ModServerConfig.noPriorWorkCost) {
            value -= l;
        }
        return value;
    }

    @IfModLoaded("taxfreelevels")
    @Inject(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal = 2
            )
    )
    private void interceptRenameCostSet(CallbackInfo ci, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j) {
        // tax free levels forcibly sets the rename cost to 1, this injects after it to revert the cost
        if (ModServerConfig.freeRenames && j > 0 && j == i) {
            cost.set(0);
        }
    }

    @ModifyExpressionValue(
            method = "mayPickup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/DataSlot;get()I",
                    ordinal = 1
            )
    )
    private int allowTakingFreeRepairs(int original) {
        return original == 0 && repairItemCountCost >= 0 ? 1 : original;
    }

    @WrapOperation(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;calculateIncreasedRepairCost(I)I"
            )
    )
    private int preventPriorWorkCostIncrease(int oldRepairCost, Operation<Integer> original) {
        if (ModServerConfig.noPriorWorkCost) {
            return oldRepairCost;
        }
        if (
                ModServerConfig.noWorkCostIncreaseOnRepair &&
                inputSlots.getItem(0).isDamageableItem() &&
                //? if <= 1.21.1
                inputSlots.getItem(0).getItem().isValidRepairItem(inputSlots.getItem(0), inputSlots.getItem(1))
                //? if > 1.21.1 {
                /*inputSlots.getItem(0).has(DataComponents.REPAIRABLE) &&
                inputSlots.getItem(0).get(DataComponents.REPAIRABLE).isValidRepairItem(inputSlots.getItem(1))
                *///?}
        ) {
            return oldRepairCost;
        }
        else {
            return original.call(oldRepairCost);
        }
    }

    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=40"
            )
    )
    private int ignoreTooExpensive(int original) {
        if (ModServerConfig.noTooExpensive) {
            return Integer.MAX_VALUE;
        }
        return original;
    }
}

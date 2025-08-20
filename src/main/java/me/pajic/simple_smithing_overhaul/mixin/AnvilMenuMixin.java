package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Shadow private int repairItemCountCost;
    @Shadow @Final private DataSlot cost;

    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 1
            )
    )
    private boolean allowAddingEnchantmentsToWhetstone(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
        if (Main.CONFIG.whetstone.enableWhetstone.get()) {
            return original || itemStack.is(ModItems.WHETSTONE);
        }
        return original;
    }

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

    @ModifyExpressionValue(
            method = "method_24922",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=0.12F"
            )
    )
    private static float modifyDegradationChance(float original) {
        if (Main.CONFIG.anvilImprovements.modifyDegradationChance.get()) {
            return Main.CONFIG.anvilImprovements.degradationChance.get() / 100;
        }
        return original;
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void noXPCostIfUnenchanted(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (
                Main.CONFIG.anvilImprovements.freeUnenchantedRepairs.get() &&
                !itemStack.isEnchanted() &&
                itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()
        ) {
            cost.set(0);
        }
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void grantAdvancements(Player player, ItemStack stack, CallbackInfo ci) {
        if (resultSlots.getItem(0).getDamageValue() < inputSlots.getItem(0).getDamageValue()) {
            if (player instanceof ServerPlayer p) ModCriteria.REPAIR_ITEM.trigger(p);
            int repairCount = stack.getOrDefault(ModDataComponents.REPAIR_COUNT, 0);
            if (player instanceof ServerPlayer p) {
                if (repairCount + 1 == 100) ModCriteria.ITEM_REPAIR_COUNT.trigger(p);
                if (repairCount + 1 == 1000) ModCriteria.ITEM_REPAIR_COUNT_BIG.trigger(p);
            }
            stack.set(ModDataComponents.REPAIR_COUNT, repairCount + 1);
        }
        if (inputSlots.getItem(1).is(Items.ENCHANTED_BOOK) && !inputSlots.getItem(0).is(Items.ENCHANTED_BOOK)) {
            if (player instanceof ServerPlayer p) ModCriteria.ANVIL_ENCHANT_COMBINE.trigger(p);
        }
        if (resultSlots.getItem(0).is(ModItems.WHETSTONE)) {
            Set<EnchantmentInstance> allEnchantments = new HashSet<>();
            player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().forEach(ref -> {
                if (ModUtil.enchantmentEligible(ref)) allEnchantments.add(new EnchantmentInstance(ref, ref.value().getMaxLevel()));
            });
            Set<EnchantmentInstance> whetstoneEnchantments = resultSlots.getItem(0).getEnchantments().entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue()))
                    .collect(Collectors.toSet());
            if (whetstoneEnchantments.containsAll(allEnchantments) && player instanceof ServerPlayer p) ModCriteria.MAX_WHETSTONE.trigger(p);
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
        if (Main.CONFIG.anvilImprovements.freeRenames.get()) {
            value -= j;
        }
        // if cost ends up consisting of just prior work cost, ignore it
        if (value == l) {
            value = 0;
        }
        // if there is an "actual" cost, deduct prior work cost from total cost if option is enabled
        if (value != 0 && Main.CONFIG.anvilImprovements.noPriorWorkCost.get()) {
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
        if (Main.CONFIG.anvilImprovements.freeRenames.get() && j > 0 && j == i) {
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
        if (Main.CONFIG.anvilImprovements.noPriorWorkCost.get()) {
            return oldRepairCost;
        }
        if (
                Main.CONFIG.anvilImprovements.noWorkCostIncreaseOnRepair.get() &&
                inputSlots.getItem(0).isDamageableItem() &&
                //? if <= 1.21.1
                (ModUtil.hasAdditionalRepair(inputSlots.getItem(0), inputSlots.getItem(1)) || inputSlots.getItem(0).getItem().isValidRepairItem(inputSlots.getItem(0), inputSlots.getItem(1)))
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
        if (Main.CONFIG.anvilImprovements.noTooExpensive.get()) {
            return Integer.MAX_VALUE;
        }
        return original;
    }

    //? if <= 1.21.1 {
    @ModifyExpressionValue(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean checkAdditionalRepair(boolean original) {
        return ModUtil.hasAdditionalRepair(inputSlots.getItem(0), inputSlots.getItem(1)) || original;
    }
    //?}
}

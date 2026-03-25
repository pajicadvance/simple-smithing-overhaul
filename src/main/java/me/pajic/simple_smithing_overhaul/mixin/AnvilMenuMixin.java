package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.blocks.ModBlocks;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(value = AnvilMenu.class, priority = 2000)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

    @Shadow /*? if fabric {*/private /*?} else {*//*public*//*?}*/ int repairItemCountCost;
    @Shadow @Final private DataSlot cost;

    @WrapMethod(method = "createResult")
    private void nonFunctionalIfBroken(Operation<Void> original) {
        Optional<Boolean> bl = access.evaluate((level, blockPos) -> level.getBlockState(blockPos).is(ModBlocks.BROKEN_ANVIL));
        if (bl.isPresent() && !bl.get()) original.call();
    }

    @ModifyArg(
			method = "lambda$onTake$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V",
                    ordinal = 1
            ),
            index = 0
    )
    private static int breakSoundIfBroken(int original, @Local(name = "newBlockState") BlockState newBlockState) {
        return newBlockState.is(ModBlocks.BROKEN_ANVIL) ? 1029 : original;
    }

    @ModifyExpressionValue(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
					//? if fabric {
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 1
					//?} else {
					/*target = "Lnet/minecraft/world/item/ItemStack;supportsEnchantment(Lnet/minecraft/core/Holder;)Z"
					*///?}
            )
    )
    private boolean allowAddingEnchantmentsToWhetstone(boolean original, @Local(name = "input") ItemStack input) {
        if (SSO.CONFIG.whetstone.enableWhetstone.get()) {
            return original || input.is(ModItems.WHETSTONE);
        }
        return original;
    }

    @ModifyArg(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(II)I"),
            index = 1
    )
    private int modifyRepairUnitCost(int original, @Local(name = "result") ItemStack result) {
        return Math.round((float) result.getMaxDamage() / ModUtil.determineUnitCost(result));
    }

	//? if fabric {
    @ModifyExpressionValue(
            method = "lambda$onTake$0",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=0.12F"
            )
    )
    private static float modifyDegradationChance(float original) {
        if (SSO.CONFIG.anvilImprovements.modifyDegradationChance.get()) {
            return SSO.CONFIG.anvilImprovements.degradationChance.get() / 100;
        }
        return original;
    }
	//?}

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void noXPCostIfUnenchanted(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (
				SSO.CONFIG.anvilImprovements.freeUnenchantedRepairs.get() &&
                !itemStack.isEnchanted() &&
                itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()
        ) {
            cost.set(0);
        }
    }

    @SuppressWarnings("resource")
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
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(JJJ)J"
            ),
            index = 0
    )
    private long modifyXPCost(long value, @Local(name = "tax") long tax, @Local(name = "namingCost") int namingCost) {
        // deduct cost of rename from total cost if option is enabled
        if (SSO.CONFIG.anvilImprovements.freeRenames.get()) {
            value -= namingCost;
        }
        // if cost ends up consisting of just prior work cost, ignore it
        if (value == tax) {
            value = 0;
        }
        // if there is an "actual" cost, deduct prior work cost from total cost if option is enabled
        if (value != 0 && SSO.CONFIG.anvilImprovements.noPriorWorkCost.get()) {
            value -= tax;
        }
        return value;
    }

    @IfModLoaded("taxfreelevels")
    @Inject(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal = 2
            )
    )
    private void interceptRenameCostSet(CallbackInfo ci, @Local(name = "price") int price, @Local(name = "namingCost") int namingCost) {
        // tax free levels forcibly sets the rename cost to 1, this injects after it to revert the cost
        if (SSO.CONFIG.anvilImprovements.freeRenames.get() && namingCost > 0 && namingCost == price) {
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

    @SuppressWarnings("DataFlowIssue")
	@WrapOperation(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AnvilMenu;calculateIncreasedRepairCost(I)I"
            )
    )
    private int preventPriorWorkCostIncrease(int oldRepairCost, Operation<Integer> original) {
        if (SSO.CONFIG.anvilImprovements.noPriorWorkCost.get()) {
            return oldRepairCost;
        }
        if (
				SSO.CONFIG.anvilImprovements.noWorkCostIncreaseOnRepair.get() &&
                inputSlots.getItem(0).isDamageableItem() &&
                inputSlots.getItem(0).has(DataComponents.REPAIRABLE) &&
                inputSlots.getItem(0).get(DataComponents.REPAIRABLE).isValidRepairItem(inputSlots.getItem(1))
        ) {
            return oldRepairCost;
        }
        else {
            return original.call(oldRepairCost);
        }
    }

    @ModifyExpressionValue(
			//? if fabric
			method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=40"
            )
    )
    private int ignoreTooExpensive(int original) {
        if (SSO.CONFIG.anvilImprovements.noTooExpensive.get()) {
            return Integer.MAX_VALUE;
        }
        return original;
    }
}

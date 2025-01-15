package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {

    @Shadow @Final private Level level;

    //? if <= 1.21.1 {
    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }
    //?}

    //? if > 1.21.1 {
    /*public SmithingMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }
    *///?}

    @Inject(
            //? if <= 1.21.1 {
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;enabledFeatures()Lnet/minecraft/world/flag/FeatureFlagSet;"
            ),
            //?}
            //? if > 1.21.1 {
            /*method = "lambda$createResult$1",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/SmithingMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;",
                    ordinal = 0
            ),
            *///?}
            cancellable = true
    )
    private void incrementEnchantmentLevel(CallbackInfo ci, @Local LocalRef<ItemStack> stack) {
        if (ModCommonConfig.enableEnchantmentUpgrading) {

            ItemEnchantments itemEnchantments = null;
            if (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots)) {
                itemEnchantments = stack.get().get(DataComponents.STORED_ENCHANTMENTS);
            } else if (ModUtil.isEnchantedItemUpgradeRecipe(slots)) {
                itemEnchantments = stack.get().get(DataComponents.ENCHANTMENTS);
            }

            if (itemEnchantments != null) {
                boolean success = false;
                int lapisAmount = slots.get(2).getItem().getCount();
                if (lapisAmount <= itemEnchantments.entrySet().size()) {
                    ItemStack updatedStack = slots.get(1).getItem().copy();
                    List<Component> enchantmentNames = new ArrayList<>();
                    Consumer<Component> consumer = enchantmentNames::add;
                    itemEnchantments.addToTooltip(Item.TooltipContext.of(level), consumer, TooltipFlag.NORMAL);
                    for (int i = 0; i < enchantmentNames.size(); i++) {
                        if (i + 1 == lapisAmount) {
                            Component enchantmentName = enchantmentNames.get(i);
                            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                                if (
                                        Enchantment.getFullname(entry.getKey(), entry.getIntValue()).equals(enchantmentName) &&
                                        entry.getIntValue() < entry.getKey().value().getMaxLevel()
                                ) {
                                    EnchantmentHelper.updateEnchantments(updatedStack, mutable ->
                                            mutable.upgrade(entry.getKey(), entry.getIntValue() + 1)
                                    );
                                    if (ModServerConfig.upgradingHasExperienceCost) {
                                        int originalRepairCost = stack.get().getOrDefault(DataComponents.REPAIR_COST, 0);
                                        Main.cost = ModServerConfig.upgradingBaseExperienceCost + originalRepairCost;
                                        if (Main.cost < 1 || (!ModServerConfig.ignoreTooExpensive && Main.cost >= 40)) break;
                                        updatedStack.set(DataComponents.REPAIR_COST, AnvilMenu.calculateIncreasedRepairCost(originalRepairCost));
                                    }
                                    stack.set(updatedStack);
                                    success = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!success) {
                    resultSlots.setItem(0, ItemStack.EMPTY);
                    ci.cancel();
                }
            }
        }
    }

    //? if <= 1.21.1 {
    @ModifyReturnValue(
            method = "mayPickup",
            at = @At("RETURN")
    )
    private boolean modifyMayPickup(boolean original) {
        if (
                ModCommonConfig.enableEnchantmentUpgrading &&
                ModServerConfig.upgradingHasExperienceCost &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= Main.cost) && Main.cost > 0;
        }
        return original;
    }
    //?}

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void hookOnTake(Player player, ItemStack itemStack, CallbackInfo ci) {
        if (
                ModCommonConfig.enableEnchantmentUpgrading &&
                ModServerConfig.upgradingHasExperienceCost &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots)) &&
                !player.getAbilities().instabuild
        ) {
            player.giveExperienceLevels(-Main.cost);
        }
    }
}
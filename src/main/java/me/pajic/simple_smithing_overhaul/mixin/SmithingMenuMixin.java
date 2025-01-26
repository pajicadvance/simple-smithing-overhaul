package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
            /*method = "method_64653",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/inventory/SmithingMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;",
                    ordinal = 0
            ),
            *///?}
            cancellable = true
    )
    private void incrementEnchantmentLevel(CallbackInfo ci, @Local LocalRef<ItemStack> stack) {
        if (Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading()) {
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
                                    if (Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost()) {
                                        int originalRepairCost = stack.get().getOrDefault(DataComponents.REPAIR_COST, 0);
                                        ModUtil.cost = Main.CONFIG.enchantmentUpgrading.upgradingBaseExperienceCost() + originalRepairCost;
                                        if (ModUtil.cost < 1 || (!Main.CONFIG.enchantmentUpgrading.ignoreTooExpensive() && ModUtil.cost >= 40)) break;
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
        if (Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment()) {
            if (ModUtil.isPinnacleEnchantmentRecipe(slots)) {
                boolean success = false;
                HolderLookup.RegistryLookup<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                ItemStack itemStack = slots.get(1).getItem().copy();
                Set<EnchantmentInstance> itemEnchantments = itemStack.getEnchantments().entrySet()
                        .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue()))
                        .collect(Collectors.toSet());
                if (itemEnchantments.stream().allMatch(ei -> ei.level == ei.enchantment.value().getMaxLevel() && !registry.getOrThrow(EnchantmentTags.CURSE).contains(ei.enchantment))) {
                    Set<EnchantmentInstance> maxedOutEnchantments = new HashSet<>();
                    registry.listElements().forEach(ref -> {
                        if (ref.value().isSupportedItem(itemStack) && ModUtil.enchantmentEligible(ref))
                            maxedOutEnchantments.add(new EnchantmentInstance(ref, ref.value().getMaxLevel()));
                    });
                    maxedOutEnchantments.removeIf(ei -> registry.getOrThrow(EnchantmentTags.CURSE).contains(ei.enchantment));
                    itemEnchantments.forEach(ei -> maxedOutEnchantments.removeIf(ei1 -> !Enchantment.areCompatible(ei.enchantment, ei1.enchantment)));
                    if (maxedOutEnchantments.isEmpty()) {
                        success = true;
                        ItemStack updatedStack = slots.get(1).getItem().copy();
                        updatedStack.set(DataComponents.CUSTOM_NAME, Component.translatable("text.item.simple_smithing_overhaul.pinnacleCustomName").withStyle(ChatFormatting.LIGHT_PURPLE));
                        stack.set(updatedStack);
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
                Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading() &&
                Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= ModUtil.cost) && ModUtil.cost > 0;
        }
        if (
                Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            return player.hasInfiniteMaterials() || player.experienceLevel >= Main.CONFIG.pinnacleEnchantment.pinnacleExperienceCost();
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
                Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading() &&
                Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots)) &&
                !player.getAbilities().instabuild
        ) {
            player.giveExperienceLevels(-ModUtil.cost);
        }
        if (
                Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots) &&
                !player.getAbilities().instabuild
        ) {
            player.giveExperienceLevels(-Main.CONFIG.pinnacleEnchantment.pinnacleExperienceCost());
        }
    }
}
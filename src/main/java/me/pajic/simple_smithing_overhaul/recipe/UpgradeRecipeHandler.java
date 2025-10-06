package me.pajic.simple_smithing_overhaul.recipe;

import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.CostAccess;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class UpgradeRecipeHandler {

    public static void handleRecipe(
            CallbackInfo ci,
            LocalRef<ItemStack> stack,
            NonNullList<Slot> slots,
            ResultContainer resultSlots,
            Level level,
            ItemCombinerMenu menu
    ) {
        if (Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()) {
            ItemEnchantments itemEnchantments = null;
            if (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots)) {
                itemEnchantments = stack.get().get(DataComponents.STORED_ENCHANTMENTS);
            } else if (ModUtil.isEnchantedItemUpgradeRecipe(slots)) {
                itemEnchantments = stack.get().get(DataComponents.ENCHANTMENTS);
            }
            if (itemEnchantments != null) {
                boolean success = false;
                int lapisAmount = slots.get(2).getItem().getCount();
                if (lapisAmount <= itemEnchantments.entrySet().size() && slots.get(1).getItem().getCount() == 1) {
                    ItemStack updatedStack = slots.get(1).getItem().copy();
                    List<Component> enchantmentNames = new ArrayList<>();
                    Consumer<Component> consumer = enchantmentNames::add;
                    itemEnchantments.addToTooltip(Item.TooltipContext.of(level), consumer, TooltipFlag.NORMAL/*? if > 1.21.1 {*//*, updatedStack.getComponents()*//*?}*/);
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
                                    if (Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get()) {
                                        int originalRepairCost = stack.get().getOrDefault(DataComponents.REPAIR_COST, 0);
                                        ((CostAccess) menu).sso$setCost(Main.CONFIG.enchantmentUpgrading.upgradingBaseExperienceCost.get() + originalRepairCost);
                                        if (((CostAccess) menu).sso$getCost() < 1 || (!Main.CONFIG.enchantmentUpgrading.ignoreTooExpensive.get() && ((CostAccess) menu).sso$getCost() >= 40)) break;
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
        if (Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()) {
            if (ModUtil.isPinnacleEnchantmentRecipe(slots)) {
                boolean success = false;
                if (slots.get(1).getItem().getCount() == 1) {
                    HolderLookup.RegistryLookup<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                    ItemStack itemStack = slots.get(1).getItem().copy();
                    Set<EnchantmentInstance> itemEnchantments = itemStack.getEnchantments().entrySet()
                            .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue()))
                            .collect(Collectors.toSet());
                    if (itemEnchantments.stream().allMatch(ei -> ei.level/*? if > 1.21.1 {*//*()*//*?}*/ >= ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel() && !registry.getOrThrow(EnchantmentTags.CURSE).contains(ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/))) {
                        Set<EnchantmentInstance> maxedOutEnchantments = new HashSet<>();
                        registry.listElements().forEach(ref -> {
                            if (itemStack.canBeEnchantedWith(ref, EnchantingContext.ACCEPTABLE) && ModUtil.enchantmentEligible(ref))
                                maxedOutEnchantments.add(new EnchantmentInstance(ref, ref.value().getMaxLevel()));
                        });
                        maxedOutEnchantments.removeIf(ei -> registry.getOrThrow(EnchantmentTags.CURSE).contains(ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/));
                        itemEnchantments.forEach(ei -> maxedOutEnchantments.removeIf(ei1 -> !Enchantment.areCompatible(ei1.enchantment/*? if > 1.21.1 {*//*()*//*?}*/, ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/)));
                        if (maxedOutEnchantments.isEmpty()) {
                            Set<EnchantmentInstance> possibleUpgrades = itemEnchantments.stream().filter(ei ->
                                    (ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel() > 1) &&
                                    (ei.level/*? if > 1.21.1 {*//*()*//*?}*/ <= ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel()) &&
                                    ModUtil.enchantmentEligible(ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/)
                            ).collect(Collectors.toSet());
                            if (!possibleUpgrades.isEmpty()) {
                                success = true;
                                ((CostAccess) menu).sso$setCost(Main.CONFIG.pinnacleEnchantment.pinnacleBaseExperienceCost.get() + Main.CONFIG.pinnacleEnchantment.pinnacleExperienceCostIncrease.get() * itemStack.getOrDefault(ModDataComponents.PINNACLE_COUNT, 0));
                                ItemStack updatedStack = slots.get(1).getItem().copy();
                                updatedStack.set(DataComponents.CUSTOM_NAME, Component.translatable("text.item.simple_smithing_overhaul.pinnacleCustomName").withStyle(ChatFormatting.LIGHT_PURPLE));
                                stack.set(updatedStack);
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

    public static ItemStack applyPinnacleUpgrade(ItemStack original, Slot slot, AbstractContainerMenu container, NonNullList<Slot> slots) {
        if (container instanceof SmithingMenu sm && ModUtil.isPinnacleEnchantmentRecipe(slots) && slot.equals(slots.get(3))) {
            RandomSource random = sm.level.getRandom();
            Component itemName = slots.get(1).getItem().getOrDefault(DataComponents.CUSTOM_NAME, original.getItem().getName(original));
            original.set(DataComponents.CUSTOM_NAME, itemName.copy().withStyle(ChatFormatting.getByName(Main.CONFIG.pinnacleEnchantment.pinnacleItemNameColor.get().replace(" ", "_").toUpperCase())));
            List<EnchantmentInstance> existingPinnacleEnchantments = original.getEnchantments().entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue()))
                    .filter(ei -> ei.level/*? if > 1.21.1 {*//*()*//*?}*/ > ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel())
                    .toList();
            if (existingPinnacleEnchantments.size() >= Main.CONFIG.pinnacleEnchantment.maxPinnacleEnchantmentsOnItem.get()) {
                int index = random.nextInt(existingPinnacleEnchantments.size());
                EnchantmentInstance toDowngrade = existingPinnacleEnchantments.get(index);
                EnchantmentHelper.updateEnchantments(original, mutable -> mutable.set(
                        toDowngrade.enchantment/*? if > 1.21.1 {*//*()*//*?}*/,
                        toDowngrade.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel()
                ));
            }
            List<EnchantmentInstance> possibleUpgrades = new ArrayList<>(original.getEnchantments().entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue())).toList());
            possibleUpgrades.removeIf(ei ->
                    (ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.value().getMaxLevel() == 1) ||
                    !ModUtil.enchantmentEligible(ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/) ||
                    existingPinnacleEnchantments.stream().anyMatch(ei1 -> ei.enchantment/*? if > 1.21.1 {*//*()*//*?}*/.equals(ei1.enchantment/*? if > 1.21.1 {*//*()*//*?}*/))
            );
            EnchantmentInstance toUpgrade = possibleUpgrades.get(random.nextInt(possibleUpgrades.size()));
            EnchantmentHelper.updateEnchantments(original, mutable ->
                    mutable.upgrade(toUpgrade.enchantment/*? if > 1.21.1 {*//*()*//*?}*/, toUpgrade.level/*? if > 1.21.1 {*//*()*//*?}*/ + 1)
            );
            original.set(ModDataComponents.PINNACLE_COUNT, original.getOrDefault(ModDataComponents.PINNACLE_COUNT, 0) + 1);
        }
        return original;
    }
}
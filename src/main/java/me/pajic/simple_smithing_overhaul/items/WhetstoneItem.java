package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

public class WhetstoneItem extends Item {

    public WhetstoneItem(Properties properties) {
        super(properties);
    }

    //? if <= 1.21.1 {
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Items.QUARTZ);
    }
    //?}

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ModCommonConfig.enableWhetstone;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return this.isFoil(stack) ?
                super.getName(stack).copy().withStyle(ChatFormatting.YELLOW) :
                super.getName(stack);
    }

    //? if <= 1.21.1 {
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return stack.getCount() == 1 && stack.get(DataComponents.STORED_ENCHANTMENTS).isEmpty();
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
    //?}

    @Override
    public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
        return true;
    }
}
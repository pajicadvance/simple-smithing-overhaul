package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.Main;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        return Main.CONFIG.whetstone.enableWhetstone();
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return this.isFoil(stack) ?
                Component.translatable("item.simple_smithing_overhaul.enchanted_whetstone").withStyle(ChatFormatting.LIGHT_PURPLE) :
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
}
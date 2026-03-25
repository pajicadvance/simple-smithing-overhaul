package me.pajic.simple_smithing_overhaul.items;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

public class WhetstoneItem extends Item {

    public WhetstoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return SSO.CONFIG.whetstone.enableWhetstone.get();
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        MutableComponent name = super.getName(stack).copy();
        int damage = stack.getDamageValue();
        if (damage == 2 || damage == 3) {
            name = Component.translatable("item.simple_smithing_overhaul.chipped", name);
        }
        else if (damage == 4 || damage == 5) {
            name = Component.translatable("item.simple_smithing_overhaul.damaged", name);
        }
        return this.isFoil(stack) ? name.withStyle(ChatFormatting.YELLOW) : name;
    }
}

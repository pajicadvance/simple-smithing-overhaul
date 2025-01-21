package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Shadow @Final public NonNullList<Slot> slots;

    @ModifyArg(
            method = "method_34249",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private ItemStack upgradeItemAfterTake(final ItemStack original, @Local(argsOnly = true) Slot slot) {
        AbstractContainerMenu container = (AbstractContainerMenu) (Object) this;
        if (container instanceof SmithingMenu && ModUtil.isPinnacleEnchantmentRecipe(slots) && slot.equals(slots.get(3))) {
            Component itemName = slots.get(1).getItem().getOrDefault(DataComponents.CUSTOM_NAME, original.getItem().getName(original));
            original.set(DataComponents.CUSTOM_NAME, itemName.copy().withStyle(ChatFormatting.LIGHT_PURPLE));
            List<EnchantmentInstance> possibleUpgrades = new ArrayList<>(original.getEnchantments().entrySet()
                    .stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getIntValue())).toList());
            possibleUpgrades.removeIf(ei -> ei.enchantment.value().getMaxLevel() == 1);
            EnchantmentInstance toUpgrade = possibleUpgrades.get(Mth.nextInt(RandomSource.create(), 0, possibleUpgrades.size() - 1));
            EnchantmentHelper.updateEnchantments(original, mutable ->
                    mutable.upgrade(toUpgrade.enchantment, toUpgrade.level + 1)
            );
        }
        return original;
    }
}

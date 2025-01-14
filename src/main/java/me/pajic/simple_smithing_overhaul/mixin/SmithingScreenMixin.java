package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    public SmithingScreenMixin(SmithingMenu itemCombinerMenu, Inventory inventory, Component component, ResourceLocation resourceLocation) {
        super(itemCombinerMenu, inventory, component, resourceLocation);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        if (
                Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading() &&
                Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost() &&
                (ModUtil.isEnchantedItemUpgradeRecipe(menu.slots) || ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(menu.slots)) &&
                menu.slots.get(2).getItem().is(Items.LAPIS_LAZULI) &&
                menu.slots.get(3).hasItem()
        ) {
            ItemStack stack = menu.slots.get(1).getItem();
            int repairCost = Main.CONFIG.enchantmentUpgrading.upgradingBaseExperienceCost() +
                    stack.getOrDefault(DataComponents.REPAIR_COST, 0);
            if (repairCost > 0) {
                Component component = Component.translatable("container.repair.cost", repairCost);
                int textColor = (minecraft.player.hasInfiniteMaterials() || minecraft.player.experienceLevel >= repairCost) && repairCost > 0 ? 8453920 : 0xFF6060;
                if (!Main.CONFIG.enchantmentUpgrading.ignoreTooExpensive() && repairCost >= 40) {
                    component = Component.translatable("container.repair.expensive");
                    textColor = 0xFF6060;
                }
                int x = imageWidth - 8 - font.width(component) - 2;
                guiGraphics.fill(x - 2, 67, imageWidth - 8, 79, 0x4F000000);
                guiGraphics.drawString(font, component, x, 69, textColor);
            }
        }
    }

    @ModifyExpressionValue(
            method = "renderBg",
            at = @At(value = "CONSTANT", args = "intValue=75")
    )
    private int nudgeArmorStandUp(int original) {
        if (
                Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading() &&
                Main.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost()
        ) {
            return original - 10;
        }
        return original;
    }
}
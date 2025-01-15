package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.Main;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin extends AbstractContainerMenu {

    protected ItemCombinerMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    //? if > 1.21.1 {
    /*@WrapMethod(method = "mayPickup")
    private boolean modifyMayPickup(Player player, boolean hasStack, Operation<Boolean> original) {
        if (
                ModCommonConfig.enableEnchantmentUpgrading &&
                ModServerConfig.upgradingHasExperienceCost &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            return (player.hasInfiniteMaterials() || player.experienceLevel >= Main.cost) && Main.cost > 0;
        }
        return hasStack;
    }
    *///?}
}

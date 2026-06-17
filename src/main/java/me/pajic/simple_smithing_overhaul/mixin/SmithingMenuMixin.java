package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.recipe.UpgradeRecipeHandler;
import me.pajic.simple_smithing_overhaul.util.CostAccess;
import me.pajic.simple_smithing_overhaul.util.ModDataComponents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import me.pajic.simple_smithing_overhaul.util.SmithingMenuExtension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu implements SmithingMenuExtension {

    @Shadow @Final public Level level;

    public SmithingMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slotDefinition) {
        super(menuType, containerId, inventory, access, slotDefinition);
    }

    @Inject(
			method = "lambda$createResult$0",
            at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/inventory/SmithingMenu;resultSlots:Lnet/minecraft/world/inventory/ResultContainer;",
					opcode = Opcodes.GETFIELD,
					ordinal = 0
			),
            cancellable = true
    )
    private void handleSmithingUpgradeRecipes(CallbackInfo ci, @Local(name = "result") LocalRef<ItemStack> result) {
        UpgradeRecipeHandler.handleRecipe(ci, result, slots, resultSlots, level, this);
    }

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void hookOnTake(Player player, ItemStack carried, CallbackInfo ci) {
        if (
				ModUtil.enchantmentUpgradingEnabled() &&
				SSO.CONFIG.enchantmentUpgrading.upgradingHasExperienceCost.get() &&
                (ModUtil.isEnchantedBookOrWhetstoneUpgradeRecipe(slots) || ModUtil.isEnchantedItemUpgradeRecipe(slots))
        ) {
            if (!player.getAbilities().instabuild) ModUtil.payXpCost(player, ((CostAccess) this).sso$getCost());
            if (player instanceof ServerPlayer p) ModCriteria.APPLY_ENCHANTMENT_UPGRADE.trigger(p);
        }
        if (
				SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get() &&
                ModUtil.isPinnacleEnchantmentRecipe(slots)
        ) {
            if (!player.getAbilities().instabuild) ModUtil.payXpCost(player, ((CostAccess) this).sso$getCost());
            if (player instanceof ServerPlayer p) {
                ModCriteria.APPLY_PINNACLE_ENCHANTMENT.trigger(p);
                if (carried.getOrDefault(ModDataComponents.PINNACLE_COUNT, 0) == 10) ModCriteria.BAD_RNG.trigger(p);
            }
        }
    }

	@Override
	public Level sso$getLevel() {
		return level;
	}
}

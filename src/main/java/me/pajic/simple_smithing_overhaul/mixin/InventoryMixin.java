package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

	@Shadow @Final public Player player;
	@Shadow public abstract ItemStack getItem(int slot);
	@Shadow public abstract int getContainerSize();

	@Inject(
			method = "tick",
			at = @At("TAIL")
	)
	private void repairItemUsingInventoryMaterials(CallbackInfo ci) {
		if (SSO.CONFIG.mendingRework.enabled.get() && SSO.CONFIG.mendingRework.autoRepairOnBreak.get() && !player.level().isClientSide()) {
			boolean repaired = false;
			for (int i = 0; i < getContainerSize(); i++) {
				ItemStack stack = getItem(i);
				if (ModUtil.isBroken(stack) && ModUtil.tryRepairItem(stack, player, player.level())) repaired = true;
			}
			if (repaired) player.inventoryMenu.broadcastChanges();
		}
	}
}

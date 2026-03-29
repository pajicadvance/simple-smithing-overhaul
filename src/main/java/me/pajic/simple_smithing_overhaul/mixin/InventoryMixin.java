package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.core.component.DataComponents;
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

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;inventoryTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
			)
	)
	private void repairItemUsingInventoryMaterials(CallbackInfo ci, @Local(name = "i") int i) {
		if (SSO.CONFIG.mendingRework.enabled.get() && SSO.CONFIG.mendingRework.autoRepairOnBreak.get()) {
			ItemStack stack = getItem(i);
			if (ModUtil.isBroken(stack) && stack.has(DataComponents.REPAIRABLE)) {
				ModUtil.tryRepairItem(stack, player, player.level());
			}
		}
	}
}

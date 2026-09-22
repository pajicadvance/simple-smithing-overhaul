package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {

	@WrapMethod(method = "repairPlayerItems")
	private int noRepairIfMendingRework(ServerPlayer player, int amount, Operation<Integer> original) {
		if (SSO.CONFIG.mendingRework.enabled.get() && !SSO.CONFIG.mendingRework.enableRegularMendingBehavior.get()) return amount;
		return original.call(player, amount);
	}
}

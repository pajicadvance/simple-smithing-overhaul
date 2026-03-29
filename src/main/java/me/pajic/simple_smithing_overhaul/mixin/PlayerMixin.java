package me.pajic.simple_smithing_overhaul.mixin;

import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

	@Inject(
			method = "aiStep",
			at = @At("TAIL")
	)
	private void onPlayerEndAiStep(CallbackInfo ci) {
		ModUtil.tryApplyAnvilSlowness((Player) (Object) this);
	}
}

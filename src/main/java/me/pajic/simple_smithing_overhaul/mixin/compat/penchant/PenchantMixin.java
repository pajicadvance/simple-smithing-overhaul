package me.pajic.simple_smithing_overhaul.mixin.compat.penchant;

//? fabric {

import archives.tater.penchant.Penchant;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.simple_smithing_overhaul.SSO;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@IfModLoaded("penchant")
@Mixin(Penchant.class)
public class PenchantMixin {

	@WrapMethod(method = "registerPack(Lnet/minecraft/resources/Identifier;Lnet/fabricmc/fabric/api/resource/v1/pack/PackActivationType;)V")
    private void controlPackActivationType(Identifier id, PackActivationType activationType, Operation<Void> original) {
		if (SSO.CONFIG.modIntegration.penchant.get()) {
			if (id.equals(Penchant.DURABILITY_REWORK)) original.call(id, PackActivationType.NORMAL);
			else if (id.equals(Penchant.REDUCED_CURSES)) original.call(id, PackActivationType.DEFAULT_ENABLED);
			else original.call(id, activationType);
		}
        else original.call(id, activationType);
    }
}
//?}

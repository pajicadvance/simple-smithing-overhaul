package me.pajic.simple_smithing_overhaul.mixin;

//? <26.1 {

/*import net.atlas.defaulted.component.backport.Repairable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Repairable.class)
public class DefaultedRepairableMixin {

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;"
            )
    )
    private static String fixCodec(String name) {
        return name.equals("items") ? "repairItems" : name;
    }
}
*///?}

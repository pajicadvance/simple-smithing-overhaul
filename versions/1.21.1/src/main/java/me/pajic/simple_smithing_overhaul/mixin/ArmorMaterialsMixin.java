package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {

    @ModifyExpressionValue(
            method = "lambda$static$13",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;NETHERITE_INGOT:Lnet/minecraft/world/item/Item;"
            )
    )
    private static Item modifyArmorMaterial(Item original) {
        return ModUtil.getNetheriteRepairMaterial();
    }
}

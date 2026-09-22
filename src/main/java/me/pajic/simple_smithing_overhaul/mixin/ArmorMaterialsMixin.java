package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.Item;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? >=26.1 {
import me.pajic.simple_smithing_overhaul.util.ModTags;
import net.minecraft.tags.TagKey;
//?} else {
/*import me.pajic.simple_smithing_overhaul.util.ModUtil;
*///?}

@Mixin(ArmorMaterials.class)
//~ if <26.1 'interface' -> 'class'
public interface ArmorMaterialsMixin {

    //? <26.1 {
    /*@ModifyExpressionValue(
            method = {"method_24356", "lambda$static$13"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;NETHERITE_INGOT:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private static Item modifyArmorMaterial(Item original) {
        return ModUtil.getNetheriteRepairMaterial();
    }
    *///?} else {
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/tags/ItemTags;REPAIRS_NETHERITE_ARMOR:Lnet/minecraft/tags/TagKey;",
					opcode = Opcodes.GETSTATIC
			)
	)
	private static TagKey<Item> modifyRepairMaterial(TagKey<Item> original) {
		return ModTags.REPAIRS_NETHERITE_EQUIPMENT;
	}
    //?}
}

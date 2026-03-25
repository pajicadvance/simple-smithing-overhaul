package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.simple_smithing_overhaul.util.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterials;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsMixin {

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
}

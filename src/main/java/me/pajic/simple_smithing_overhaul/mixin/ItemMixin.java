package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

//? <26.1 {
/*import net.minecraft.world.InteractionResultHolder;
*///?} else {
import net.minecraft.world.InteractionResult;
//?}

@Mixin(Item.class)
public class ItemMixin {

	@WrapMethod(method = "use")
    //~ if <26.1 'InteractionResult' -> 'InteractionResultHolder<ItemStack>'
	private InteractionResult repairItemUsingInventoryMaterials(
			Level level,
			Player player,
			InteractionHand hand,
            //~ if <26.1 'InteractionResult' -> 'InteractionResultHolder<ItemStack>'
			Operation<InteractionResult> original
	) {
		if (SSO.CONFIG.mendingRework.enabled.get() && SSO.CONFIG.mendingRework.repairOnShiftUse.get() && player.isShiftKeyDown()) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.isDamaged() && ModUtil.tryRepairItem(stack, player, level)) {
                //~ if <26.1 'InteractionResult.CONSUME' -> 'InteractionResultHolder.consume(stack)'
				return InteractionResult.CONSUME;
			}
		}
		return original.call(level, player, hand);
	}
}

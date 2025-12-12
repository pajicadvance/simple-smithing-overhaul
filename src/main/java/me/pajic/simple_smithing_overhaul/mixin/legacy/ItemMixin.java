package me.pajic.simple_smithing_overhaul.mixin.legacy;

//? if 1.21.1 {

/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin {

    @WrapMethod(method = "isValidRepairItem")
    private boolean modifyIsValidRepairItem(ItemStack stack, ItemStack repairCandidate, Operation<Boolean> original) {
        return ModUtil.hasAdditionalRepair(stack, repairCandidate) || original.call(stack, repairCandidate);
    }
}
*///?}

package me.pajic.simple_smithing_overhaul.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

//? <26.1 {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.simple_smithing_overhaul.backport.Repairable;
import me.pajic.simple_smithing_overhaul.repair.RepairableOverrides;
import net.minecraft.world.item.ItemStack;
*///?}

@Mixin(Item.class)
public class ItemMixin {

    //? <26.1 {
    /*@WrapMethod(method = "isValidRepairItem")
    private boolean modifyRepairItem(ItemStack itemStack, ItemStack itemStack2, Operation<Boolean> original) {
        Repairable repairable = RepairableOverrides.get(itemStack.getItem());
        return (repairable != null && repairable.isValidRepairItem(itemStack2)) || original.call(itemStack, itemStack2);
    }
    *///?}
}

package me.pajic.simple_smithing_overhaul.mixin;

//? <26.1 {

/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atlas.defaulted.component.backport.PhantomDataComponents;
import net.atlas.defaulted.extension.ItemExtensions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Item.class, priority = 1500)
public class DefaultedItemMixin {

    @WrapMethod(method = "isValidRepairItem")
    private boolean fixRepairCheck(ItemStack itemStack, ItemStack itemStack2, Operation<Boolean> original) {
        return ((ItemExtensions) itemStack.getItem()).defaulted$has(DataComponents.REPAIRABLE) ?
                ((ItemExtensions) itemStack.getItem()).defaulted$get(DataComponents.REPAIRABLE).isValidRepairItem(itemStack2) :
                original.call(itemStack, itemStack2);
    }
}
*///?}

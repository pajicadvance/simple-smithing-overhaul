package me.pajic.simple_smithing_overhaul.mixin;

//? >=26.1 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import me.pajic.simple_smithing_overhaul.extension.ComponentMapOwner;
import me.pajic.simple_smithing_overhaul.repair.RepairableOverrides;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Repairable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

//? <26.3
//import java.util.Optional;

@Mixin(PatchedDataComponentMap.class)
public class PatchedDataComponentMapMixin implements ComponentMapOwner {

    //~ if >26.2 'Optional<?>>' -> 'Object>'
    @Shadow private Reference2ObjectMap<DataComponentType<?>, Object> patch;
    @Unique private @Nullable Item sso$owner;

    @SuppressWarnings({"unchecked"})
    @WrapMethod(method = "get")
    private <T> @Nullable T overrideRepairable(DataComponentType<? extends T> type, Operation<T> original) {
        if (type == DataComponents.REPAIRABLE && sso$owner != null && !patch.containsKey(type)) {
            Repairable override = RepairableOverrides.get(sso$owner);
            if (override != null) return (T) override;
        }
        return original.call(type);
    }

    @Override
    public void sso$setOwner(Item item) {
        this.sso$owner = item;
    }
}
//?}

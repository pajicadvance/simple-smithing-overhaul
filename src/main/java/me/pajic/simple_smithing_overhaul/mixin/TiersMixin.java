package me.pajic.simple_smithing_overhaul.mixin;

//? <26.1 {

/*import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Tiers.class)
public class TiersMixin {

    @Mutable @Shadow @Final private Supplier<Ingredient> repairIngredient;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void modifyTiers(String string, int ordinal, TagKey<Block> incorrectBlockForDrops, int _uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient, CallbackInfo ci) {
        if (ordinal == 5) this.repairIngredient = () -> Ingredient.of(ModUtil.getNetheriteRepairMaterial());
    }
}
*///?}

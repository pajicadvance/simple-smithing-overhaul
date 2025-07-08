package me.pajic.simple_smithing_overhaul.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.simple_smithing_overhaul.Initializer;
import me.pajic.simple_smithing_overhaul.criterion.ModCriteria;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.recipe.WhetstoneRepairItemRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RecipeCraftingHolder.class)
public interface RecipeCraftingHolderMixin {

    @Inject(
            method = "awardUsedRecipes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;triggerRecipeCrafted(Lnet/minecraft/world/item/crafting/RecipeHolder;Ljava/util/List;)V"
            )
    )
    private void grantAdvancement(Player player, List<ItemStack> items, CallbackInfo ci, @Local RecipeHolder<?> recipeHolder) {
        if (recipeHolder.value() instanceof WhetstoneRepairItemRecipe && player instanceof ServerPlayer p) {
            ItemStack repairedItem = items.stream().filter(itemStack -> itemStack.isDamageableItem() && !itemStack.is(ModItems.WHETSTONE)).findFirst().orElse(null);
            if (repairedItem != null) {
                ModCriteria.REPAIR_ITEM_WHETSTONE.trigger(p);
                if (repairedItem.getOrDefault(Initializer.REPAIR_COUNT, 0) == 99) ModCriteria.ITEM_REPAIR_COUNT.trigger(p);
                if (repairedItem.getOrDefault(Initializer.REPAIR_COUNT, 0) == 999) ModCriteria.ITEM_REPAIR_COUNT_BIG.trigger(p);
                if (repairedItem.isEnchanted()) ModCriteria.REPAIR_ITEM_WHETSTONE_ENCHANTED.trigger(p);
            }
        }
    }
}

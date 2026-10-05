package me.pajic.simple_smithing_overhaul.backport;

//? <26.1 {
/*import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record Repairable(HolderSet<Item> items) {
    public static final StreamCodec<RegistryFriendlyByteBuf, Repairable> STREAM_CODEC =
            ByteBufCodecs.holderSet(Registries.ITEM).map(Repairable::new, Repairable::items);

    public boolean isValidRepairItem(ItemStack stack) {
        return this.items.contains(stack.getItemHolder());
    }
}
*///?}

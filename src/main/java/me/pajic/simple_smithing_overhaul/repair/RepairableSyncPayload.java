package me.pajic.simple_smithing_overhaul.repair;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

//? <26.1 {
/*import me.pajic.simple_smithing_overhaul.backport.Repairable;
 *///?} else {
import net.minecraft.world.item.enchantment.Repairable;
//?}

public record RepairableSyncPayload(Map<Item, Repairable> repairables) implements CustomPacketPayload {

    public static final Type<RepairableSyncPayload> TYPE = new Type<>(SSO.id("repairables"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RepairableSyncPayload> CODEC =
        ByteBufCodecs.<RegistryFriendlyByteBuf, Item, Repairable, Map<Item, Repairable>>map(
                HashMap::new, ByteBufCodecs.registry(Registries.ITEM), Repairable.STREAM_CODEC
        ).map(RepairableSyncPayload::new, RepairableSyncPayload::repairables);

    @Override @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

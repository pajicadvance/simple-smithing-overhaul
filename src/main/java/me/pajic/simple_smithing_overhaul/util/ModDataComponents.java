package me.pajic.simple_smithing_overhaul.util;

import com.mojang.serialization.Codec;
import me.pajic.simple_smithing_overhaul.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ModDataComponents {
    public static final DataComponentType<Integer> REPAIR_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "repair_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static final DataComponentType<Integer> PINNACLE_COUNT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "pinnacle_count"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static final DataComponentType<Boolean> BROKEN = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "broken"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static void init() {}
}

package me.pajic.simple_smithing_overhaul.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;

public class ModDataComponents {

    public static final DataComponentType<Integer> REPAIR_COUNT = DataComponentType.<Integer>builder().persistent(Codec.INT).build();
    public static final DataComponentType<Integer> PINNACLE_COUNT = DataComponentType.<Integer>builder().persistent(Codec.INT).build();
    public static final DataComponentType<Boolean> BROKEN = DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build();

    public static void init() {}
}

package me.pajic.simple_smithing_overhaul.util;

import java.util.function.BooleanSupplier;

public class ClientCallbacks {

    private static BooleanSupplier inWorld = () -> false;
    private static Runnable exportEnchantmentInfo = () -> {};

    public static void register(BooleanSupplier inWorld, Runnable exportEnchantmentInfo) {
        ClientCallbacks.inWorld = inWorld;
        ClientCallbacks.exportEnchantmentInfo = exportEnchantmentInfo;
    }

    public static boolean isInWorld() {
        return inWorld.getAsBoolean();
    }

    public static void exportEnchantmentInfo() {
        exportEnchantmentInfo.run();
    }
}

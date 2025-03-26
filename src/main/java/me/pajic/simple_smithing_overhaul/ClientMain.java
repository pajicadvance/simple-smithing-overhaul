package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.mixson.ResourceModifications;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResourceModifications.initClient();
    }
}

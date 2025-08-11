package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Populate item and item tag suggestions used by the config
        // Runs after every world change, clears the list and renews it
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((server, world) -> {
            ModUtil.itemSuggestions.clear();
            ModUtil.itemSuggestions.addAll(BuiltInRegistries.ITEM.keySet().stream().map(ResourceLocation::toString).toList());
            BuiltInRegistries.ITEM./*? if > 1.21.1 {*//*listTagIds()*//*?}*//*? if 1.21.1 {*/getTagNames()/*?}*/.forEach(tag -> ModUtil.itemSuggestions.add("#" + tag.location()));
        });
    }
}

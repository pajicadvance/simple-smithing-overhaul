package me.pajic.simple_smithing_overhaul;

import me.pajic.simple_smithing_overhaul.config.ModConfig;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import me.pajic.simple_smithing_overhaul.loot.ModLootEvents;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static ModConfig CONFIG = ModConfig.createAndLoad();
    public static int cost = 0;

    @Override
    public void onInitialize() {
        ModItems.init();
        ModUtil.initAdditionalRepairables();
        ModLootEvents.init();
    }
}

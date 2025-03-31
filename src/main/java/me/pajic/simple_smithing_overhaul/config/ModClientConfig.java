package me.pajic.simple_smithing_overhaul.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = "simple_smithing_overhaul", bus = EventBusSubscriber.Bus.MOD)
public class ModClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue RENAME_TO_EXPERIENCE_BOTTLE = BUILDER
            .translation("text.config.simple_smithing_overhaul.option.improvedExperienceBottle.renameToExperienceBottle")
            .gameRestart()
            .define("renameToExperienceBottle", true);

    public static final ModConfigSpec CLIENT_SPEC = BUILDER.build();

    public static boolean renameToExperienceBottle;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event);
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig(event);
    }

    private static void updateConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == CLIENT_SPEC) {
            renameToExperienceBottle = RENAME_TO_EXPERIENCE_BOTTLE.get();
        }
    }
}

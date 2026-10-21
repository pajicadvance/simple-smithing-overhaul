package me.pajic.simple_smithing_overhaul.util;

//? <26.1 {
/*import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
*///?}

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Path;

public class ModClientUtil {

    public static void onUpdateConfig() {
        IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) server.execute(() -> ModUtil.onUpdateConfig(server));
    }

    public static boolean isInWorld() {
        return Minecraft.getInstance().level != null;
    }

    public static void exportEnchantmentInfo() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;
        Path file = minecraft.gameDirectory.toPath().resolve("config").resolve(SSO.MOD_ID).resolve("enchantment_info.txt");
        try {
            EnchantmentInfoExporter.export(minecraft.level.registryAccess(), file);
            minecraft.player.sendSystemMessage(Component.translatable("text.simple_smithing_overhaul.enchantmentInfoExported", file.toAbsolutePath().normalize().toString()));
        } catch (IOException e) {
            SSO.LOGGER.error("Unable to export enchantment info", e);
            minecraft.player.sendSystemMessage(Component.translatable("text.simple_smithing_overhaul.enchantmentInfoExportFailed"));
        }
    }

    public static void initItemProperties() {
        //? <26.1 {
		/*ItemProperties.register(
				ModItems.WHETSTONE,
				SSO.id("damage_state"),
				(stack, level, entity, i) -> (float) stack.getDamageValue() / stack.getMaxDamage()
		);
		*///?}
    }
}

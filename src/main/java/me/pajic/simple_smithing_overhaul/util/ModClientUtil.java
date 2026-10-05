package me.pajic.simple_smithing_overhaul.util;

//? <26.1 {
/*import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.items.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
*///?}

import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

public class ModClientUtil {

    public static void onUpdateConfig() {
        IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) server.execute(() -> ModUtil.onUpdateConfig(server));
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

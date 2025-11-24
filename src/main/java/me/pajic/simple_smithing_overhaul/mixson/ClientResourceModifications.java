package me.pajic.simple_smithing_overhaul.mixson;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.util.MixsonUtil;

public class ClientResourceModifications {
	public static void init() {
		// Ridiculous hack to get language patches to support different languages
		// Deletes the original translations just to make sure my overrides win because lang files are stupid and load order is random
		Mixson.registerEvent(
				Mixson.DEFAULT_PRIORITY,
				MixsonUtil.getLocatorFromString("minecraft:lang/*"),
				"minecraft:modify_lang",
				context -> {
					if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
						if (SSO.CONFIG.improvedExperienceBottle.renameToExperienceBottle.get()) {
							context.getFile().getAsJsonObject().remove("entity.minecraft.experience_bottle");
							context.getFile().getAsJsonObject().remove("item.minecraft.experience_bottle");
						}
					}
				},
				true
		);
		// After 1.21.1 the smithing template name format changed so this is needed
		// The override string is the item name for 1.21.1+, normal string is for 1.21.1
		//? if > 1.21.1 {
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                MixsonUtil.getLocatorFromString("simple_smithing_overhaul:lang/*"),
                "simple_smithing_overhaul:modify_lang",
                context -> {
                    if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                        context.getFile().getAsJsonObject().addProperty(
                                "item.simple_smithing_overhaul.enchantment_upgrade",
                                context.getFile().getAsJsonObject().get("item.simple_smithing_overhaul.enchantment_upgrade.override").getAsString()
                        );
                        context.getFile().getAsJsonObject().addProperty(
                                "item.simple_smithing_overhaul.pinnacle_enchantment",
                                context.getFile().getAsJsonObject().get("item.simple_smithing_overhaul.pinnacle_enchantment.override").getAsString()
                        );
                    }
                },
                true
        );
        //?}
	}
}

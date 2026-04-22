package me.pajic.simple_smithing_overhaul.mixson;

import me.pajic.simple_smithing_overhaul.SSO;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.util.Index;

public class AssetPatches {

	public static void init() {
		MixsonHelper.registerMultiJson(
				"Ensure language override",
				index -> index.id().toString().startsWith("minecraft:lang/"),
				context -> {
					if (context.getIndex().id().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
						if (SSO.CONFIG.improvedExperienceBottle.renameToExperienceBottle.get()) {
							context.getFile().getAsJsonObject().remove("entity.minecraft.experience_bottle");
							context.getFile().getAsJsonObject().remove("item.minecraft.experience_bottle");
						}
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Mending description",
				new Index("item_descriptions:lang/en_us"),
				context -> {
					if (SSO.CONFIG.mendingRework.enabled.get()) {
						context.getFile().getAsJsonObject().remove("enchantment.minecraft.mending.description");
					}
				}
		);
	}
}

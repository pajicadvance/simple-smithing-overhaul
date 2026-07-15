package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.ramixin.mixson.util.Index;

public class DataPatches {

	public static void init() {
		MixsonHelper.registerSingleJson(
				"Set netherite repair material",
				new Index("simple_smithing_overhaul:tags/item/repairs_netherite_equipment"),
				context -> {
					String material = switch (SSO.CONFIG.streamlinedRepairs.netheriteRepairMaterial.get()) {
						case NETHERITE_INGOT -> "minecraft:netherite_ingot";
						case NETHERITE_SCRAP -> "minecraft:netherite_scrap";
						case DIAMOND -> "minecraft:diamond";
					};
					context.getFile().getAsJsonObject().getAsJsonArray("values").add(material);
				}
		);

		if (ModUtil.enchantmentUpgradingEnabled()) {
			JsonElement pool = Constants.singleItemChancePool.deepCopy();
			pool.getAsJsonObject()
					.getAsJsonArray("entries").get(0).getAsJsonObject()
					.addProperty("name", "simple_smithing_overhaul:enchantment_upgrade");
			pool.getAsJsonObject()
					.getAsJsonArray("conditions").get(0).getAsJsonObject()
					.addProperty("chance", 0.1);
			MixsonHelper.registerSingleJson(
					"Distribute enchantment upgrade templates to end city loot",
					new Index("minecraft:loot_table/chests/end_city_treasure"),
					context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
			);
		}

		if (SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()) {
			JsonElement pool = Constants.singleItemChancePool.deepCopy();
			pool.getAsJsonObject()
					.getAsJsonArray("entries").get(0).getAsJsonObject()
					.addProperty("name", "simple_smithing_overhaul:pinnacle_enchantment");
			pool.getAsJsonObject()
					.getAsJsonArray("conditions").get(0).getAsJsonObject()
					.addProperty("chance", 0.1);
			MixsonHelper.registerSingleJson(
					"Distribute pinnacle enchantment upgrade templates to ancient city loot",
					new Index("minecraft:loot_table/chests/ancient_city"),
					context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
			);
		}

		if (!CompatFlags.PENCHANT_LOADED && SSO.CONFIG.enchantedBookLootTweaks.additionalChestLoot.get()) {
			SSO.CONFIG.enchantedBookLootTweaks.bookLootLocations.forEach((location, values) -> {
				JsonElement pool = Constants.enchantedBookPool.deepCopy();
				pool.getAsJsonObject()
						.getAsJsonArray("conditions").get(0).getAsJsonObject()
						.addProperty("chance", values.getChance());
				if (values.getCount() > 1) {
					JsonObject function = new JsonObject();
					function.addProperty("function", "minecraft:set_count");
					function.addProperty("count", values.getCount());
					pool.getAsJsonObject()
							.getAsJsonArray("entries").get(0).getAsJsonObject()
							.getAsJsonArray("functions").add(function);
				}
				MixsonHelper.registerSingleJson(
						"Distribute additional enchanted book loot to " + location.toString().replace(":", "_"),
						new Index(location.toString().replace(":", ":loot_table/")),
						context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
				);
			});
		}

		if (SSO.CONFIG.improvedExperienceBottle.additionalChestLoot.get()) {
			SSO.CONFIG.improvedExperienceBottle.bottleLootLocations.forEach((location, values) -> {
				JsonElement pool = Constants.singleItemChancePool.deepCopy();
				pool.getAsJsonObject()
						.getAsJsonArray("entries").get(0).getAsJsonObject()
						.addProperty("name", "minecraft:experience_bottle");
				pool.getAsJsonObject()
						.getAsJsonArray("conditions").get(0).getAsJsonObject()
						.addProperty("chance", values.getChance());
				if (values.getCount() > 1) {
					JsonArray functions = new JsonArray();
					JsonObject function = new JsonObject();
					function.addProperty("function", "minecraft:set_count");
					function.addProperty("count", values.getCount());
					functions.add(function);
					pool.getAsJsonObject()
							.getAsJsonArray("entries").get(0).getAsJsonObject()
							.add("functions", functions);
				}
				MixsonHelper.registerSingleJson(
						"Distribute additional XP bottle loot to " + location.toString().replace(":", "_"),
						new Index(location.toString().replace(":", ":loot_table/")),
						context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
				);
			});
		}
	}
}

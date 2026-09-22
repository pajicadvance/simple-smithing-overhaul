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

		MixsonHelper.registerSingleJson(
				"Set pinnacle template duplication material",
				new Index("simple_smithing_overhaul:tags/item/duplicates_pinnacle_template"),
				context -> {
					String material = switch (SSO.CONFIG.pinnacleEnchantment.duplicationMaterial.get()) {
						case SCULK_CATALYST -> "minecraft:sculk_catalyst";
						case SCULK -> "minecraft:sculk";
						case NETHER_STAR -> "minecraft:nether_star";
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

        //? <26.1 {
        /*MixsonHelper.registerSingleJson(
                "Patch root smithing advancement for pre 26.1",
                new Index("simple_smithing_overhaul:advancement/smithing/root"),
                context -> {
                    JsonObject display = context.getFile().getAsJsonObject().getAsJsonObject("display");
                    display.addProperty("background", display.get("background").getAsString().replace(":", ":textures/") + ".png");
                }
        );

        MixsonHelper.registerMultiJson(
                "Patch recipes for pre 26.1",
                index -> index.id().toString().startsWith("simple_smithing_overhaul:recipe/"),
                context -> {
                    JsonObject file = context.getFile().getAsJsonObject();
                    String type = file.getAsJsonPrimitive("type").getAsString();
                    if (type.equals("minecraft:crafting_shaped")) {
                        JsonObject key = new JsonObject();
                        file.getAsJsonObject("key").asMap().forEach((s, el) -> {
                            JsonObject entry = new JsonObject();
                            String itemOrTag = el.getAsString();
                            if (itemOrTag.startsWith("#")) entry.addProperty("tag", el.getAsString().substring(1));
                            else entry.addProperty("item", el.getAsString());
                            key.add(s, entry);
                        });
                        file.add("key", key);
                    } else if (type.equals("minecraft:smithing_transform")) {
                        JsonObject base = new JsonObject();
                        JsonObject addition = new JsonObject();
                        JsonObject template = new JsonObject();
                        String baseItemOrTag = file.getAsJsonPrimitive("base").getAsString();
                        String additionItemOrTag = file.getAsJsonPrimitive("addition").getAsString();
                        String templateItemOrTag = file.getAsJsonPrimitive("template").getAsString();
                        if (baseItemOrTag.startsWith("#")) base.addProperty("tag", baseItemOrTag.substring(1));
                        else base.addProperty("item", baseItemOrTag);
                        if (additionItemOrTag.startsWith("#")) addition.addProperty("tag", additionItemOrTag.substring(1));
                        else addition.addProperty("item", additionItemOrTag);
                        if (templateItemOrTag.startsWith("#")) template.addProperty("tag", templateItemOrTag.substring(1));
                        else template.addProperty("item", templateItemOrTag);
                        file.add("base", base);
                        file.add("addition", addition);
                        file.add("template", template);
                    }
                }
        );
        *///?}
	}
}

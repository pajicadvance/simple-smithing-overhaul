package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.pajic.simple_smithing_overhaul.SSO;
import me.pajic.simple_smithing_overhaul.util.CompatFlags;
import me.pajic.simple_smithing_overhaul.util.ModUtil;
import net.ramixin.mixson.util.Index;

public class DataPatches {

    private static final JsonElement SINGLE_ITEM_CHANCE_POOL = JsonParser.parseString("""
    {
        "rolls": 1.0,
        "entries": [
            {
                "type": "minecraft:item"
            },
            {
                "type": "minecraft:empty"
            }
        ]
    }
    """);

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
			JsonElement pool = SINGLE_ITEM_CHANCE_POOL.deepCopy();
            JsonObject obj = pool.getAsJsonObject().getAsJsonArray("entries").get(0).getAsJsonObject();
            obj.addProperty("name", "simple_smithing_overhaul:enchantment_upgrade");
            obj.addProperty("weight", 10);
            JsonObject obj1 = pool.getAsJsonObject().getAsJsonArray("entries").get(1).getAsJsonObject();
            obj1.addProperty("weight", 90);
			MixsonHelper.registerSingleJson(
					"Distribute enchantment upgrade templates to end city loot",
					new Index("minecraft:loot_table/chests/end_city_treasure"),
					context -> addPool(context.getFile(), pool)
			);
		}

		if (SSO.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()) {
			JsonElement pool = SINGLE_ITEM_CHANCE_POOL.deepCopy();
            JsonObject obj = pool.getAsJsonObject().getAsJsonArray("entries").get(0).getAsJsonObject();
            obj.addProperty("name", "simple_smithing_overhaul:pinnacle_enchantment");
            obj.addProperty("weight", 10);
            JsonObject obj1 = pool.getAsJsonObject().getAsJsonArray("entries").get(1).getAsJsonObject();
            obj1.addProperty("weight", 90);
			MixsonHelper.registerSingleJson(
					"Distribute pinnacle enchantment upgrade templates to ancient city loot",
					new Index("minecraft:loot_table/chests/ancient_city"),
					context -> addPool(context.getFile(), pool)
			);
		}

		if (!CompatFlags.PENCHANT_LOADED && SSO.CONFIG.enchantedBookLootTweaks.additionalChestLoot.get()) {
			SSO.CONFIG.enchantedBookLootTweaks.bookLootLocations.forEach((location, values) -> {
				JsonElement pool = SINGLE_ITEM_CHANCE_POOL.deepCopy();
                JsonObject obj = pool.getAsJsonObject().getAsJsonArray("entries").get(0).getAsJsonObject();
                obj.addProperty("name", "minecraft:book");
                obj.addProperty("weight", values.getChance());
                JsonArray arr = new JsonArray();
                JsonObject obj3 = new JsonObject();
                //~ if >=26.3 'function' -> 'type'
                obj3.addProperty("type", "minecraft:enchant_randomly");
                obj3.addProperty("options", "#minecraft:on_random_loot");
                arr.add(obj3);
                if (values.getCount() > 1) {
                    JsonObject obj2 = new JsonObject();
                    //~ if >=26.3 'function' -> 'type'
                    obj2.addProperty("type", "minecraft:set_count");
                    obj2.addProperty("count", values.getCount());
                    arr.add(obj2);
                }
                //~ if >=26.3 'functions' -> 'modifier'
                obj.add("modifier", arr);
                JsonObject obj1 = pool.getAsJsonObject().getAsJsonArray("entries").get(1).getAsJsonObject();
                obj1.addProperty("weight", 100 - values.getChance());
				MixsonHelper.registerSingleJson(
						"Distribute additional enchanted book loot to " + location.toString().replace(":", "_"),
						new Index(location.toString().replace(":", ":loot_table/")),
						context -> addPool(context.getFile(), pool)
				);
			});
		}

		if (SSO.CONFIG.improvedExperienceBottle.additionalChestLoot.get()) {
			SSO.CONFIG.improvedExperienceBottle.bottleLootLocations.forEach((location, values) -> {
				JsonElement pool = SINGLE_ITEM_CHANCE_POOL.deepCopy();
                JsonObject obj = pool.getAsJsonObject().getAsJsonArray("entries").get(0).getAsJsonObject();
                obj.addProperty("name", "minecraft:experience_bottle");
                obj.addProperty("weight", values.getChance());
                if (values.getCount() > 1) {
                    JsonArray arr = new JsonArray();
                    JsonObject obj2 = new JsonObject();
                    //~ if >=26.3 'function' -> 'type'
                    obj2.addProperty("type", "minecraft:set_count");
                    obj2.addProperty("count", values.getCount());
                    arr.add(obj2);
                    //~ if >=26.3 'functions' -> 'modifier'
                    obj.add("modifier", arr);
                }
                JsonObject obj1 = pool.getAsJsonObject().getAsJsonArray("entries").get(1).getAsJsonObject();
                obj1.addProperty("weight", 100 - values.getChance());
				MixsonHelper.registerSingleJson(
						"Distribute additional XP bottle loot to " + location.toString().replace(":", "_"),
						new Index(location.toString().replace(":", ":loot_table/")),
						context -> addPool(context.getFile(), pool)
				);
			});
		}

		if (CompatFlags.PENCHANT_LOADED) {
			MixsonHelper.registerSingleJson(
					"Remove anvil enchanted book advancement",
					new Index("simple_smithing_overhaul:advancement/smithing/booksmith_blackworm"),
					context -> context.markForDeletion(true)
			);
			MixsonHelper.registerSingleJson(
					"Replace enchantment upgrade advancement with Penchant level up advancement",
					new Index("simple_smithing_overhaul:advancement/smithing/one_step_at_a_time"),
					context -> replaceAdvancementCriterion(
							context.getFile(),
							"simple_smithing_overhaul:smithing/root",
							"enchantment_level_up",
							"simple_smithing_overhaul:enchantment_level_up",
							"minecraft:experience_bottle",
							"advancements.smithing.one_step_at_a_time.penchant"
					)
			);
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

	private static void addPool(JsonElement file, JsonElement pool) {
		JsonObject obj = file.getAsJsonObject();
		if (!obj.has("pools")) obj.add("pools", new JsonArray());
		obj.getAsJsonArray("pools").add(pool);
	}

	private static void replaceAdvancementCriterion(JsonElement file, String parent, String name, String trigger, String icon, String langKey) {
		JsonObject obj = file.getAsJsonObject();
		obj.addProperty("parent", parent);
		JsonObject criterion = new JsonObject();
		criterion.addProperty("trigger", trigger);
		JsonObject criteria = new JsonObject();
		criteria.add(name, criterion);
		obj.add("criteria", criteria);
		JsonArray requirement = new JsonArray();
		requirement.add(name);
		JsonArray requirements = new JsonArray();
		requirements.add(requirement);
		obj.add("requirements", requirements);
		JsonObject display = obj.getAsJsonObject("display");
		display.getAsJsonObject("icon").addProperty("id", icon);
		display.getAsJsonObject("title").addProperty("translate", langKey + ".title");
		display.getAsJsonObject("description").addProperty("translate", langKey + ".description");
	}
}

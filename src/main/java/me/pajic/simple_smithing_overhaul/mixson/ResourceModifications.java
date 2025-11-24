package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pajic.simple_smithing_overhaul.SSO;
import net.ramixin.mixson.debug.DebugMode;
import net.ramixin.mixson.inline.Mixson;

@SuppressWarnings("removal")
public class ResourceModifications {
	private static boolean initialized = false;

    public static void init() {
		if (initialized) return;
        if (SSO.xplat().isDebug()) Mixson.setDebugMode(DebugMode.EXPORT);

        if (SSO.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()) {
            JsonElement pool = Constants.singleItemChancePool.deepCopy();
            pool.getAsJsonObject()
                    .getAsJsonArray("entries").get(0).getAsJsonObject()
                    .addProperty("name", "simple_smithing_overhaul:enchantment_upgrade");
            pool.getAsJsonObject()
                    .getAsJsonArray("conditions").get(0).getAsJsonObject()
                    .addProperty("chance", 0.1);
            Mixson.registerEvent(
                    Mixson.DEFAULT_PRIORITY,
                    "minecraft:loot_table/chests/end_city_treasure",
                    "simple_smithing_overhaul:enchantment_upgrade_loot",
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
            Mixson.registerEvent(
                    Mixson.DEFAULT_PRIORITY,
                    "minecraft:loot_table/chests/ancient_city",
                    "simple_smithing_overhaul:pinnacle_enchantment_loot",
                    context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
            );
        }

        if (SSO.CONFIG.enchantedBookLootTweaks.additionalChestLoot.get()) {
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
                Mixson.registerEvent(
                        Mixson.DEFAULT_PRIORITY,
                        location.toString().replace(":", ":loot_table/"),
                        "simple_smithing_overhaul:enchanted_book_" + location.toString().replace(":", "_"),
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
                Mixson.registerEvent(
                        Mixson.DEFAULT_PRIORITY,
                        location.toString().replace(":", ":loot_table/"),
                        "simple_smithing_overhaul:xp_bottle_" + location.toString().replace(":", "_"),
                        context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
                );
            });
        }

        // This is stupid, but in vanilla the material that repairs netherite equipment is also
        // used as the material that upgrades it to netherite in the first place,
        // clearly I don't want this as the default repair material for netherite are diamonds in this mod
        //? if > 1.21.1 {
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                rl -> rl.getPath().startsWith("recipe/") && !rl.getNamespace().equals("emi"),
                "simple_smithing_overhaul:modify_recipes",
                context -> {
                    String type = context.getFile().getAsJsonObject().getAsJsonPrimitive("type").getAsString();
                    if (type.equals("minecraft:smithing_transform")) {
                        String addition = context.getFile().getAsJsonObject().getAsJsonPrimitive("addition").getAsString();
                        if (addition.equals("#minecraft:netherite_tool_materials")) {
                            context.getFile().getAsJsonObject().addProperty("addition", "minecraft:netherite_ingot");
                        }
                    }
                },
                true
        );
        //?}
		initialized = true;
    }
}

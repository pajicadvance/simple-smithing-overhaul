package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pajic.simple_smithing_overhaul.Main;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.debug.DebugMode;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.util.MixsonUtil;

@SuppressWarnings("removal")
public class ResourceModifications {

    public static void init() {

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Mixson.setDebugMode(DebugMode.EXPORT);

        if (Main.CONFIG.enchantmentUpgrading.enableEnchantmentUpgrading.get()) {
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

        if (Main.CONFIG.pinnacleEnchantment.enablePinnacleEnchantment.get()) {
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

        if (Main.CONFIG.enchantedBookLootTweaks.additionalChestLoot.get()) {
            Main.CONFIG.enchantedBookLootTweaks.bookLootLocations.forEach((location, values) -> {
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

        if (Main.CONFIG.improvedExperienceBottle.additionalChestLoot.get()) {
            Main.CONFIG.improvedExperienceBottle.bottleLootLocations.forEach((location, values) -> {
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

        //? if >= 1.21.4 {
        /*Mixson.registerEvent(
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
        *///?}

        // Ridiculous hack to get language patches to support different languages
        // Registers an event for all available lang files and runs the patch if the lang file name contains the current client language code
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                MixsonUtil.getLocatorFromString("simple_smithing_overhaul:lang/*"),
                "simple_smithing_overhaul:modify_lang",
                context -> {
                    if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                        if (Main.CONFIG.improvedExperienceBottle.renameToExperienceBottle.get()) {
                            context.getFile().getAsJsonObject().addProperty(
                                    "entity.minecraft.experience_bottle",
                                    context.getFile().getAsJsonObject().get("entity.minecraft.experience_bottle.override").getAsString()
                            );
                            context.getFile().getAsJsonObject().addProperty(
                                    "item.minecraft.experience_bottle",
                                    context.getFile().getAsJsonObject().get("item.minecraft.experience_bottle.override").getAsString()
                            );
                        }
                        // After 1.21.1 the smithing template name format changed so this is needed
                        // The override string is the item name for 1.21.1+, normal string is for 1.21.1
                        //? if > 1.21.1 {
                        /*context.getFile().getAsJsonObject().addProperty(
                                "item.simple_smithing_overhaul.enchantment_upgrade",
                                context.getFile().getAsJsonObject().get("item.simple_smithing_overhaul.enchantment_upgrade.override").getAsString()
                        );
                        context.getFile().getAsJsonObject().addProperty(
                                "item.simple_smithing_overhaul.pinnacle_enchantment",
                                context.getFile().getAsJsonObject().get("item.simple_smithing_overhaul.pinnacle_enchantment.override").getAsString()
                        );
                        *///?}
                    }
                },
                true
        );
    }
}

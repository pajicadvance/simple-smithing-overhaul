package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pajic.simple_smithing_overhaul.config.ModClientConfig;
import me.pajic.simple_smithing_overhaul.config.ModCommonConfig;
import me.pajic.simple_smithing_overhaul.loot.LootAddition;
import me.pajic.simple_smithing_overhaul.loot.LootConstants;
import me.pajic.simple_smithing_overhaul.loot.LootUtil;
import net.neoforged.fml.loading.FMLLoader;
import net.ramixin.mixson.debug.DebugMode;
import net.ramixin.mixson.inline.Mixson;

import java.util.Optional;

public class ResourceModifications {

    public static void init() {

        if (!FMLLoader.isProduction()) Mixson.setDebugMode(DebugMode.EXPORT);

        if (ModCommonConfig.enableEnchantmentUpgrading) {
            Optional<LootAddition> optional = LootUtil.getLootAdditionFromConfigEntry("minecraft:chests/end_city_treasure;10");
            if (optional.isPresent()) {
                LootAddition lootAddition = optional.get();
                JsonElement pool = LootConstants.singleItemChancePool.deepCopy();
                pool.getAsJsonObject()
                        .getAsJsonArray("entries").get(0).getAsJsonObject()
                        .addProperty("name", "simple_smithing_overhaul:enchantment_upgrade");
                pool.getAsJsonObject()
                        .getAsJsonArray("conditions").get(0).getAsJsonObject()
                        .addProperty("chance", lootAddition.getChance());
                Mixson.registerEvent(
                        Mixson.DEFAULT_PRIORITY,
                        lootAddition.getLocation(),
                        "simple_smithing_overhaul:enchantment_upgrade_loot",
                        context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
                );
            }
        }

        if (ModCommonConfig.enablePinnacleEnchantment) {
            Optional<LootAddition> optional = LootUtil.getLootAdditionFromConfigEntry("minecraft:chests/ancient_city;10");
            if (optional.isPresent()) {
                LootAddition lootAddition = optional.get();
                JsonElement pool = LootConstants.singleItemChancePool.deepCopy();
                pool.getAsJsonObject()
                        .getAsJsonArray("entries").get(0).getAsJsonObject()
                        .addProperty("name", "simple_smithing_overhaul:pinnacle_enchantment");
                pool.getAsJsonObject()
                        .getAsJsonArray("conditions").get(0).getAsJsonObject()
                        .addProperty("chance", lootAddition.getChance());
                Mixson.registerEvent(
                        Mixson.DEFAULT_PRIORITY,
                        lootAddition.getLocation(),
                        "simple_smithing_overhaul:pinnacle_enchantment_loot",
                        context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
                );
            }
        }

        if (ModCommonConfig.additionalBookChestLoot) {
            ModCommonConfig.bookLootLocations.forEach(entry -> {
                Optional<LootAddition> optional = LootUtil.getLootAdditionFromConfigEntry(entry);
                if (optional.isPresent()) {
                    LootAddition lootAddition = optional.get();
                    JsonElement pool = LootConstants.enchantedBookPool.deepCopy();
                    pool.getAsJsonObject()
                            .getAsJsonArray("conditions").get(0).getAsJsonObject()
                            .addProperty("chance", lootAddition.getChance());
                    if (lootAddition.getCount() > 1) {
                        JsonObject function = new JsonObject();
                        function.addProperty("function", "minecraft:set_count");
                        function.addProperty("count", lootAddition.getCount());
                        pool.getAsJsonObject()
                                .getAsJsonArray("entries").get(0).getAsJsonObject()
                                .getAsJsonArray("functions").add(function);
                    }
                    Mixson.registerEvent(
                            Mixson.DEFAULT_PRIORITY,
                            lootAddition.getLocation(),
                            "simple_smithing_overhaul:enchanted_book_" + lootAddition.getLocation().replace(":", "_"),
                            context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
                    );
                }
            });
        }

        if (ModCommonConfig.additionalBottleChestLoot) {
            ModCommonConfig.bottleLootLocations.forEach(entry -> {
                Optional<LootAddition> optional = LootUtil.getLootAdditionFromConfigEntry(entry);
                if (optional.isPresent()) {
                    LootAddition lootAddition = optional.get();
                    JsonElement pool = LootConstants.singleItemChancePool.deepCopy();
                    pool.getAsJsonObject()
                            .getAsJsonArray("entries").get(0).getAsJsonObject()
                            .addProperty("name", "minecraft:experience_bottle");
                    pool.getAsJsonObject()
                            .getAsJsonArray("conditions").get(0).getAsJsonObject()
                            .addProperty("chance", lootAddition.getChance());
                    if (lootAddition.getCount() > 1) {
                        JsonArray functions = new JsonArray();
                        JsonObject function = new JsonObject();
                        function.addProperty("function", "minecraft:set_count");
                        function.addProperty("count", lootAddition.getCount());
                        functions.add(function);
                        pool.getAsJsonObject()
                                .getAsJsonArray("entries").get(0).getAsJsonObject()
                                .add("functions", functions);
                    }
                    Mixson.registerEvent(
                            Mixson.DEFAULT_PRIORITY,
                            lootAddition.getLocation(),
                            "simple_smithing_overhaul:xp_bottle_" + lootAddition.getLocation().replace(":", "_"),
                            context -> context.getFile().getAsJsonObject().getAsJsonArray("pools").add(pool)
                    );
                }
            });
        }
    }

    public static void clientInit() {
        if (ModClientConfig.renameToExperienceBottle) {
            Mixson.registerEvent(
                    Mixson.DEFAULT_PRIORITY,
                    "minecraft:lang/en_us",
                    "simple_smithing_overhaul:modify_lang",
                    context -> {
                        context.getFile().getAsJsonObject().addProperty("entity.minecraft.experience_bottle", "Thrown Experience Bottle");
                        context.getFile().getAsJsonObject().addProperty("item.minecraft.experience_bottle", "Experience Bottle");
                    }
            );
        }
    }
}

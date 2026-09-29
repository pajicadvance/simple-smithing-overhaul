package me.pajic.simple_smithing_overhaul.mixson;

//? >=26.3 {
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.SharedConstants;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.storage.loot.LootPool;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Runs against the actual game codecs, which silently discard obsolete loot fields. */
public class LootPoolFormatTest {
    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        RegistryOps<JsonElement> ops = VanillaRegistries.createWorldLookup().createSerializationContext(JsonOps.INSTANCE);

        for (double chance : new double[]{0, 0.1, 1}) {
            JsonObject template = pool(Constants.singleItemChancePool, "minecraft:netherite_upgrade_smithing_template", chance);
            JsonObject encoded = roundTrip(template, ops);
            require(Math.abs(encoded.getAsJsonObject("condition").get("chance").getAsDouble() - chance) < 0.000001, "Template probability lost");
        }

        JsonObject book = pool(Constants.enchantedBookPool, "minecraft:book", 0.25);
        JsonArray functions = entry(book).getAsJsonArray("functions");
        // Bootstrap registries contain enchantments but do not load data-pack tags.
        functions.get(0).getAsJsonObject().addProperty("options", "minecraft:sharpness");
        functions.add(countFunction(3));
        JsonObject encodedBook = roundTrip(book, ops);
        JsonArray modifiers = entry(encodedBook).getAsJsonArray("modifier");
        require(modifiers.size() == 2, "Book modifiers lost");
        require(modifiers.get(0).getAsJsonObject().get("type").getAsString().equals("minecraft:enchant_randomly"), "Book enchantment lost");
        require(modifiers.get(1).getAsJsonObject().get("count").getAsInt() == 3, "Configured book count lost");
        require(encodedBook.getAsJsonObject("condition").get("chance").getAsDouble() == 0.25, "Book probability lost");

        JsonObject bottle = pool(Constants.singleItemChancePool, "minecraft:experience_bottle", 0.5);
        JsonArray bottleFunctions = new JsonArray();
        bottleFunctions.add(countFunction(4));
        entry(bottle).add("functions", bottleFunctions);
        JsonObject encodedBottle = roundTrip(bottle, ops);
        require(entry(encodedBottle).getAsJsonObject("modifier").get("count").getAsInt() == 4, "Configured bottle count lost");
        require(encodedBottle.getAsJsonObject("condition").get("chance").getAsDouble() == 0.5, "Bottle probability lost");

        try (var reader = new InputStreamReader(Objects.requireNonNull(LootPoolFormatTest.class.getResourceAsStream(
                "/data/simple_smithing_overhaul/loot_table/blocks/broken_anvil.json")), StandardCharsets.UTF_8)) {
            JsonObject anvil = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("pools").get(0).getAsJsonObject();
            // Only vanilla items are registered in this isolated codec test.
            entry(anvil).addProperty("name", "minecraft:anvil");
            require(roundTrip(anvil, ops).getAsJsonObject("condition").get("type").getAsString().equals("minecraft:survives_explosion"), "Anvil explosion condition lost");
        }

        JsonObject multiple = pool(Constants.singleItemChancePool, "minecraft:diamond", 0.5);
        multiple.getAsJsonArray("conditions").add(JsonParser.parseString("{\"condition\":\"minecraft:survives_explosion\"}"));
        JsonObject combined = roundTrip(multiple, ops).getAsJsonObject("condition");
        require(combined.get("type").getAsString().equals("minecraft:all_of") && combined.getAsJsonArray("terms").size() == 2, "Multiple conditions lost");
        System.out.println("Loot pool codec regression checks passed");
    }

    private static JsonObject pool(JsonElement template, String item, double chance) {
        JsonObject pool = template.deepCopy().getAsJsonObject();
        entry(pool).addProperty("name", item);
        pool.getAsJsonArray("conditions").get(0).getAsJsonObject().addProperty("chance", chance);
        return pool;
    }

    private static JsonObject entry(JsonObject pool) {
        return pool.getAsJsonArray("entries").get(0).getAsJsonObject();
    }

    private static JsonObject countFunction(int count) {
        JsonObject function = new JsonObject();
        function.addProperty("function", "minecraft:set_count");
        function.addProperty("count", count);
        return function;
    }

    private static JsonObject roundTrip(JsonObject pool, RegistryOps<JsonElement> ops) {
        JsonObject original = pool.deepCopy();
        JsonElement prepared = Constants.prepareLootPool(pool);
        require(pool.equals(original), "Preparing loot mutated the shared template");
        require(Constants.prepareLootPool(prepared).equals(prepared), "Preparing loot twice changed its meaning");
        LootPool decoded = LootPool.CODEC.parse(ops, prepared).getOrThrow();
        return LootPool.CODEC.encodeStart(ops, decoded).getOrThrow().getAsJsonObject();
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
//?}

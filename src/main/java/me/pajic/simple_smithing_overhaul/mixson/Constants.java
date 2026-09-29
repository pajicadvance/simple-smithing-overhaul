package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Constants {

    /** Convert only the loot pools owned by this mod to the current game's format. */
    public static JsonElement prepareLootPool(JsonElement source) {
        JsonObject pool = source.deepCopy().getAsJsonObject();
        //? >=26.3 {
        renameOperations(pool, "conditions", "condition", "condition");
        for (JsonElement entry : pool.getAsJsonArray("entries")) {
            renameOperations(entry.getAsJsonObject(), "functions", "modifier", "function");
        }
        //?}
        return pool;
    }

    //? >=26.3 {
    private static void renameOperations(JsonObject owner, String oldKey, String newKey, String discriminator) {
        JsonElement operations = owner.remove(oldKey);
        if (operations == null) return;
        for (JsonElement operation : operations.getAsJsonArray()) {
            JsonObject object = operation.getAsJsonObject();
            object.add("type", object.remove(discriminator));
        }
        if (newKey.equals("condition")) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "minecraft:all_of");
            condition.add("terms", operations);
            owner.add(newKey, operations.getAsJsonArray().size() == 1 ? operations.getAsJsonArray().get(0) : condition);
        } else {
            owner.add(newKey, operations);
        }
    }
    //?}

    public static final JsonElement singleItemChancePool = JsonParser.parseString("""
    {
        "rolls": 1.0,
         "entries": [
             {
                 "type": "minecraft:item"
             }
         ],
         "conditions": [
             {
                 "condition": "minecraft:random_chance"
             }
         ]
    }
    """);

    public static final JsonElement enchantedBookPool = JsonParser.parseString("""
    {
        "rolls": 1.0,
        "entries": [
            {
                "type": "minecraft:item",
                "name": "minecraft:book",
                "functions": [
                    {
                        "function": "minecraft:enchant_randomly",
                        "options": "#minecraft:on_random_loot"
                    }
                ]
            }
        ],
        "conditions": [
            {
                "condition": "minecraft:random_chance"
            }
        ]
    }
    """);
}

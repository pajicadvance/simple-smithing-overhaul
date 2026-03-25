package me.pajic.simple_smithing_overhaul.mixson;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Constants {

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

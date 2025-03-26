package me.pajic.simple_smithing_overhaul.loot;

public class LootAddition {
    private final String location;
    private final float chance;
    private final int count;

    public LootAddition(String location, float chance, int count) {
        this.location = location;
        this.chance = chance;
        this.count = count;
    }

    public LootAddition(String location, float chance) {
        this(location, chance, 1);
    }

    public String getLocation() {
        return location;
    }

    public float getChance() {
        return chance;
    }

    public int getCount() {
        return count;
    }
}
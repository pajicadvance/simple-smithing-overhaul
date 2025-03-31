package me.pajic.simple_smithing_overhaul.loot;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.pajic.simple_smithing_overhaul.config.ModServerConfig;
import net.minecraft.ResourceLocationException;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LootUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("SimpleSmithingOverhaul-LootUtil");

    public static Optional<LootAddition> getLootAdditionFromConfigEntry(String configEntry) {
        String[] split = configEntry.split(";");
        try {
            if (split.length == 2) {
                String location = split[0].replace(":", ":loot_table/");
                float chance = Integer.parseInt(split[1]);
                return Optional.of(new LootAddition(location, chance / 100));
            } else if (split.length == 3) {
                String location = split[0].replace(":", ":loot_table/");
                float chance = Integer.parseInt(split[1]);
                int count = Integer.parseInt(split[2]);
                return Optional.of(new LootAddition(location, chance / 100, count));
            } else {
                LOGGER.warn("Config entry {} does not match format location;chance or location;chance;count", configEntry);
            }
        } catch (NumberFormatException | ResourceLocationException e) {
            LOGGER.warn("Failed to parse config entry {}: {}", configEntry, e.getMessage());
        }
        return Optional.empty();
    }

    public static int calculateNewEnchantmentLevel(int maxLevel, RandomSource randomSource, int original) {
        if (ModServerConfig.weightedLevels) {
            // fills up a pool with enchantment levels and picks a level randomly
            // for level 5 the pool would look like this
            // 1 x lv5, 9 x lv4, 25 x lv3, 49 x lv2, 81 x lv1
            if (maxLevel == 1) return 1;
            IntList pool = new IntArrayList();
            for (int i = maxLevel, j = 1; i > 0; i--, j += 2) {
                for (int k = 0; k < j * j; k++) {
                    pool.add(i);
                }
            }
            return pool.getInt(randomSource.nextInt(pool.size()));
        }
        return original;
    }
}

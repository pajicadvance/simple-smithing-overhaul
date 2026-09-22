package me.pajic.simple_smithing_overhaul.util;

import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

@Translation(prefix = "simple_smithing_overhaul.config")
public class ChanceAndCount implements Walkable {

    public ValidatedInt chance;
    public ValidatedInt count;

    public ChanceAndCount(int chance, int count) {
        this.chance = new ValidatedInt(chance);
        this.count = new ValidatedInt(count);
    }

    public ChanceAndCount() {
        chance = new ValidatedInt(50, 100, 1);
        count = new ValidatedInt(1, Integer.MAX_VALUE, 1);
    }

    public int getChance() {
        return chance.get();
    }

    public int getCount() {
        return count.get();
    }
}

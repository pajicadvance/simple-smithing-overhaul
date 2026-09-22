package me.pajic.simple_smithing_overhaul.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

//? >=26.3 {
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
//?} else >=26.2 {
/*import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
*///?} else {
/*//~ if <26.1 'criterion' -> 'critereon' {
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
//~}
*///?}

public class ModCriterion extends SimpleCriterionTrigger<ModCriterion.TriggerInstance> {

    @Override
    public @NotNull Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, TriggerInstance::matches);
    }

    //~ if >26.2 'ContextAwarePredicate' -> 'Holder<LootItemCondition>'
    public record TriggerInstance(Optional<Holder<LootItemCondition>> player) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        //~ if >26.2 'EntityPredicate.ADVANCEMENT_CODEC' -> 'LootItemCondition.CODEC'
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
                ).apply(instance, TriggerInstance::new)
        );

        public boolean matches() {
            return true;
        }
    }
}

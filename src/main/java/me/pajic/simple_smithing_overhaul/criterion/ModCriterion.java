package me.pajic.simple_smithing_overhaul.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModCriterion extends SimpleCriterionTrigger<ModCriterion.TriggerInstance> {
    @Override
    public @NotNull Codec<ModCriterion.TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        trigger(player, TriggerInstance::matches);
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<ModCriterion.TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ModCriterion.TriggerInstance::player)
                ).apply(instance, ModCriterion.TriggerInstance::new)
        );

        public boolean matches() {
            return true;
        }
    }
}

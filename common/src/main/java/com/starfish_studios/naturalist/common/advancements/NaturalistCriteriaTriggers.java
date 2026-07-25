package com.starfish_studios.naturalist.common.advancements;

import com.google.common.collect.Maps;
import com.starfish_studios.naturalist.common.advancements.criterion.CaughtEntityTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NaturalistCriteriaTriggers {
    private static final Map<ResourceLocation, CriterionTrigger<?>> CRITERIA = Maps.newHashMap();
    public static final ResourceLocation CAUGHT_ENTITY_ID = new ResourceLocation("naturalist", "caught_entity");
    public static final CaughtEntityTrigger CAUGHT_ENTITY = register(CAUGHT_ENTITY_ID, new CaughtEntityTrigger());

    public void CriteriaTriggers() {
    }

    public static <T extends CriterionTrigger<?>> @NotNull T register(ResourceLocation id, T criterion) {
        if (CRITERIA.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate criterion id " + id);
        } else {
            CRITERIA.put(id, criterion);
            return criterion;
        }
    }

    public static <T extends CriterionTriggerInstance> CriterionTrigger getCriterion(ResourceLocation id) {
        return CRITERIA.get(id);
    }

    public static Iterable<? extends CriterionTrigger<?>> all() {
        return CRITERIA.values();
    }
}

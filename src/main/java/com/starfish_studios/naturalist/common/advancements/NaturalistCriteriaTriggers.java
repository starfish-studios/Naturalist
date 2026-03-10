package com.starfish_studios.naturalist.common.advancements;

import com.starfish_studios.naturalist.common.advancements.criterion.CaughtEntityTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class NaturalistCriteriaTriggers {
    public static final CaughtEntityTrigger CAUGHT_ENTITY = CriteriaTriggers.register("naturalist:caught_entity", new CaughtEntityTrigger());

    public static void init() {
        // Force static initialization
    }
}

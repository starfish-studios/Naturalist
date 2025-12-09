package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import org.jetbrains.annotations.NotNull;

public class BabyPanicGoal extends PanicGoal {
    public BabyPanicGoal(@NotNull PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    protected boolean shouldPanic() {
        return mob.getLastHurtByMob() != null && mob.isBaby() || mob.isOnFire();
    }
}

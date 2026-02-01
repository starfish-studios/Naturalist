package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class CloseMeleeAttackGoal extends MeleeAttackGoal {
    public CloseMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(mob, speedModifier, pFollowingTargetEvenIfNotSeen);
    }

    protected double getAttackReachSqr(@NotNull LivingEntity attackTarget) {
        return Mth.square(this.mob.getBbWidth() * 1.2f);
    }
}

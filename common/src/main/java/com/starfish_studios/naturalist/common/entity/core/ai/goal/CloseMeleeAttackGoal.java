package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class CloseMeleeAttackGoal extends MeleeAttackGoal {
    public CloseMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
    }

    @Override
    protected boolean canPerformAttack(LivingEntity attackTarget) {
        return this.isTimeToAttack()
            && this.mob.distanceToSqr(attackTarget) <= Mth.square(this.mob.getBbWidth() * 1.2f)
            && this.mob.getSensing().hasLineOfSight(attackTarget);
    }
}

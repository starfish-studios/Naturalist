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
    protected boolean canPerformAttack(LivingEntity target) {
        // Use a closer attack range: width * 1.2f squared
        double reachSqr = Mth.square(this.mob.getBbWidth() * 1.2f);
        double distSqr = this.mob.distanceToSqr(target);
        return distSqr <= reachSqr && this.isTimeToAttack();
    }
}

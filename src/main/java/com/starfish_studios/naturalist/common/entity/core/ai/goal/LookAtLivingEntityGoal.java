package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class LookAtLivingEntityGoal extends Goal {
    public static final float DEFAULT_PROBABILITY = 0.02F;
    protected final Mob mob;
    @Nullable
    protected LivingEntity lookAt;
    protected final float lookDistance;
    private int lookTime;
    protected final float probability;
    private final boolean onlyHorizontal;
    protected final TargetingConditions lookAtContext;

    public LookAtLivingEntityGoal(Mob mob, float lookDistance) {
        this(mob, lookDistance, DEFAULT_PROBABILITY);
    }

    public LookAtLivingEntityGoal(Mob mob, float lookDistance, float probability) {
        this(mob, lookDistance, probability, false);
    }

    public LookAtLivingEntityGoal(Mob mob, float lookDistance, float probability, boolean onlyHorizontal) {
        this.mob = mob;
        this.lookDistance = lookDistance;
        this.probability = probability;
        this.onlyHorizontal = onlyHorizontal;
        this.setFlags(EnumSet.of(Flag.LOOK));

        this.lookAtContext = TargetingConditions.forNonCombat()
                .range(lookDistance)
                .selector(entity -> entity instanceof LivingEntity && entity != mob);
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return false;
        }

        this.lookAt = this.mob.level().getNearestEntity(
                this.mob.level().getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.lookDistance), entity -> true),
                this.lookAtContext,
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ()
        );

        return this.lookAt != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.lookAt == null || !this.lookAt.isAlive()) {
            return false;
        }

        double distanceSq = this.mob.distanceToSqr(this.lookAt);
        return distanceSq <= this.lookDistance * this.lookDistance && this.lookTime > 0;
    }

    @Override
    public void start() {
        this.lookTime = 40 + this.mob.getRandom().nextInt(40); // Random duration between 40 and 80 ticks
    }

    @Override
    public void stop() {
        this.lookAt = null;
    }

    @Override
    public void tick() {
        if (this.lookAt != null && this.lookAt.isAlive()) {
            double targetY = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
            this.mob.getLookControl().setLookAt(this.lookAt.getX(), targetY, this.lookAt.getZ());
            --this.lookTime;
        }
    }
}

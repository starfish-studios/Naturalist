package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import org.jetbrains.annotations.NotNull;

public class BabyHurtByTargetGoal extends HurtByTargetGoal {
    public BabyHurtByTargetGoal(PathfinderMob mob, Class<?>... toIgnoreDamage) {
        super(mob, toIgnoreDamage);
    }

    @Override
    public void start() {
        super.start();
        if (this.mob.isBaby()) {
            this.alertOthers();
            this.stop();
        }
    }

    @Override
    protected void alertOther(Mob mob, @NotNull LivingEntity target) {
        if (!mob.isBaby()) {
            super.alertOther(mob, target);
        }
    }
}
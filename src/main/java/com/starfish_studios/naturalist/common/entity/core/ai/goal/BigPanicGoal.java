package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class BigPanicGoal extends PanicGoal {
    private static final int SEARCH_DISTANCE = 15;
    private static final int VERTICAL_CHECK_DISTANCE = 4;

    public BigPanicGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }

    @Override
    protected boolean findRandomPosition() {
        Vec3 randomPos = DefaultRandomPos.getPos(this.mob, SEARCH_DISTANCE, VERTICAL_CHECK_DISTANCE);
        if (randomPos == null) {
            return false;
        } else {
            this.posX = randomPos.x;
            this.posY = randomPos.y;
            this.posZ = randomPos.z;
            return true;
        }
    }

    @Override
    protected BlockPos lookForWater(BlockGetter level, Entity entity, int range) {
        BlockPos entityPos = entity.blockPosition();
        if (!level.getBlockState(entityPos).getCollisionShape(level, entityPos).isEmpty()) {
            return null;
        }
        return BlockPos.findClosestMatch(entityPos, range + 5, 2,
                        (pos) -> level.getFluidState(pos).is(FluidTags.WATER))
                .orElse(null);
    }
}

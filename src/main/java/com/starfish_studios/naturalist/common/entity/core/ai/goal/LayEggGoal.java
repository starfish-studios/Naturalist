package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import com.starfish_studios.naturalist.common.block.AlligatorEggBlock;
import com.starfish_studios.naturalist.common.block.TortoiseEggBlock;
import com.starfish_studios.naturalist.common.entity.Alligator;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import com.starfish_studios.naturalist.common.entity.core.EggLayingAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LayEggGoal<T extends Animal & EggLayingAnimal> extends MoveToBlockGoal {
    private final @NotNull T animal;
    public LayEggGoal(T animal, double speedModifier) {
        super(animal, speedModifier, 16);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        if (this.animal.hasEgg()) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.animal.hasEgg();
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = this.animal.blockPosition();

        if (this.animal instanceof TamableAnimal ta && ta.isInSittingPose()) {
            this.animal.setLayingEgg(false);
            return;
        }

        if (!this.animal.isInWater() && this.isReachedTarget()) {
            if (this.animal.getLayEggCounter() < 1) {
                this.animal.setLayingEgg(true);
            }
            else if (this.animal.getLayEggCounter() > this.adjustedTickDelay(60)) {
                Level level = this.animal.level();
                level.playSound(null, pos, SoundEvents.TURTLE_LAY_EGG,
                        SoundSource.BLOCKS, 0.3f,
                        0.9f + level.random.nextFloat() * 0.2f);

                BlockState eggState;
                int eggCount = this.animal.getRandom().nextInt(4) + 1;

                if (this.animal instanceof Tortoise tortoise) {
                    eggState = this.animal.getEggBlock().defaultBlockState()
                            .setValue(TurtleEggBlock.EGGS, eggCount)
                            .setValue(TortoiseEggBlock.VARIANT, tortoise.getVariant());
                }
                else if (this.animal instanceof Alligator) {
                    eggState = this.animal.getEggBlock().defaultBlockState()
                            .setValue(AlligatorEggBlock.EGGS, eggCount);
                }
                else {
                    eggState = this.animal.getEggBlock().defaultBlockState();
                }

                level.setBlock(this.blockPos.above(), eggState, 3);
                this.animal.setHasEgg(false);
                this.animal.setLayingEgg(false);
                this.animal.setInLoveTime(600);
            }

            if (this.animal.isLayingEgg()) {
                this.animal.setLayEggCounter(this.animal.getLayEggCounter() + 1);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(this.animal.getEggLayableBlockTag());
    }
}

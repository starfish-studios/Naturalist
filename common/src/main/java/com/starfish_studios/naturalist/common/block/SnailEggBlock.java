package com.starfish_studios.naturalist.common.block;

import com.google.common.annotations.VisibleForTesting;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SnailEggBlock extends Block {
    public static float HITBOX_WIDTH = 0.4F;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public SnailEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, getSnailEggHatchDelay(level.getRandom()));
    }

    private static int getSnailEggHatchDelay(RandomSource random) {
        int minHatchTickDelay = 600;
        int maxHatchTickDelay = 2400;
        return random.nextInt(minHatchTickDelay, maxHatchTickDelay);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.hatchSnailEgg(level, pos, random);
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.getType().equals(EntityType.FALLING_BLOCK)) {
            this.destroyBlock(level, pos);
        }

    }

    private void hatchSnailEgg(ServerLevel level, BlockPos pos, RandomSource random) {
        this.destroyBlock(level, pos);
        level.playSound(null, pos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.spawnBabySnails(level, pos, random);
    }

    private void destroyBlock(Level level, BlockPos pos) {
        level.destroyBlock(pos, false);
    }

    private void spawnBabySnails(ServerLevel level, BlockPos pos, RandomSource random) {
        int i = random.nextInt(2, 6);

        for(int j = 1; j <= i; ++j) {
            Snail snail = NaturalistEntityTypes.SNAIL.get().create(level);
            if (snail != null) {
                double d = (double)pos.getX() + this.getRandomSnailPositionOffset(random);
                double e = (double)pos.getZ() + this.getRandomSnailPositionOffset(random);
                int k = random.nextInt(1, 361);
                snail.moveTo(d, pos.getY(), e, (float)k, 0.0F);
                snail.setPersistenceRequired();
                snail.setAge(-6000);
                level.addFreshEntity(snail);
            }
        }

    }

    private double getRandomSnailPositionOffset(RandomSource random) {
        double d = HITBOX_WIDTH / 2.0F;
        return Mth.clamp(random.nextDouble(), d, 1.0 - d);
    }
}

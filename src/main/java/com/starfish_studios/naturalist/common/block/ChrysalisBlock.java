package com.starfish_studios.naturalist.common.block;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

public class ChrysalisBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<ChrysalisBlock> CODEC = simpleCodec(ChrysalisBlock::new);

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    protected static final VoxelShape[] EAST_AABB = new VoxelShape[] {
            Block.box(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D), Block.box(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D),
            Block.box(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D), Block.box(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D) };
    protected static final VoxelShape[] WEST_AABB = new VoxelShape[] { Block.box(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D),
            Block.box(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D), Block.box(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D),
            Block.box(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D) };
    protected static final VoxelShape[] NORTH_AABB = new VoxelShape[] { Block.box(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D),
            Block.box(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D), Block.box(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D),
            Block.box(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D) };
    protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[] {
            Block.box(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D), Block.box(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D),
            Block.box(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D), Block.box(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D) };

    public ChrysalisBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos,
            @NotNull RandomSource random) {
        int age = state.getValue(AGE);
        if (age < 3) {
            if (level.random.nextInt(5) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        } else {
            level.removeBlock(pos, false);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 0.7F,
                    0.9F + random.nextFloat() * 0.2F);
            level.levelEvent(2001, pos, Block.getId(state));
            Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.get().create(level,
                    net.minecraft.world.entity.EntitySpawnReason.BREEDING);
            assert butterfly != null;
            butterfly.setVariant(Butterfly.Variant.getTypeById(random.nextInt(Butterfly.Variant.values().length)));
            butterfly.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            butterfly.setXRot(0.0F);
            butterfly.setYRot(0.0F);
            level.addFreshEntity(butterfly);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockState facingState = level.getBlockState(pos.relative(state.getValue(FACING)));
        return facingState.is(BlockTags.LOGS);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        int age = state.getValue(AGE);
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_AABB[age];
            case WEST -> WEST_AABB[age];
            case EAST -> EAST_AABB[age];
            default -> NORTH_AABB[age];
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                state = state.setValue(FACING, direction);
                if (state.canSurvive(level, pos)) {
                    return state;
                }
            }
        }

        return null;
    }

    /*
     * public @NotNull BlockState updateShape(BlockState state, @NotNull Direction
     * direction,
     * 
     * @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull
     * BlockPos currentPos,
     * 
     * @NotNull BlockPos facingPos) {
     * return direction == state.getValue(FACING) && !state.canSurvive(level,
     * currentPos)
     * ? Blocks.AIR.defaultBlockState()
     * : super.updateShape(state, direction, facingState, level, currentPos,
     * facingPos);
     * }
     */

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, AGE);
    }
}

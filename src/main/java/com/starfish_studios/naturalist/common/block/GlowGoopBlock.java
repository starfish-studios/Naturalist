package com.starfish_studios.naturalist.common.block;


import com.starfish_studios.naturalist.core.registry.NaturalistItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.ToIntFunction;

public class GlowGoopBlock extends Block implements SimpleWaterloggedBlock {
    public static final int MIN_GOOP = 1;
    public static final int MAX_GOOP = 3;
    public static final BooleanProperty WATERLOGGED;
    public static final @NotNull ToIntFunction<BlockState> LIGHT_EMISSION;
    public static IntegerProperty GOOP;

    public GlowGoopBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(GOOP, MIN_GOOP)
            .setValue(WATERLOGGED, false));
    }

    private void decreaseGoop(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        int i = state.getValue(GOOP);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(GOOP, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        this.decreaseGoop(level, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return NaturalistItems.GLOW_GOOP.get().asItem().getDefaultInstance();
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(GOOP, Math.min(MAX_GOOP, blockState.getValue(GOOP) + 1));
        } else {
            FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
            boolean bl = fluidState.getType() == Fluids.WATER;
            return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(WATERLOGGED, bl);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState state, @NotNull BlockPlaceContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        int currentGoop = state.getValue(GOOP);

        if (stack.is(this.asItem()) && !context.isSecondaryUseActive() && currentGoop < MAX_GOOP) {
            return true;
        }

        if (!stack.is(this.asItem())) {
            if (!level.isClientSide()) {
                ItemStack itemStack = new ItemStack(NaturalistItems.GLOW_GOOP.get(), currentGoop);
                popResource(level, pos, itemStack);

                level.removeBlock(pos, false);
            }
            return true;
        }

        return super.canBeReplaced(state, context);
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(GOOP, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, CollisionContext context) {
        return context.isHoldingItem(NaturalistItems.GLOW_GOOP.get()) ? Shapes.block() : Shapes.empty();
    }

    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public float getShadeBrightness(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 1.0F;
    }

    static {
        GOOP = IntegerProperty.create("goop", MIN_GOOP, MAX_GOOP);
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        LIGHT_EMISSION = (blockState) -> blockState.getValue(GOOP) * 5;
    }
}
